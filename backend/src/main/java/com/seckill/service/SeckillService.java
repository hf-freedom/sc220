package com.seckill.service;

import com.seckill.common.Result;
import com.seckill.dto.SeckillRequest;
import com.seckill.entity.Activity;
import com.seckill.entity.Order;
import com.seckill.entity.Statistics;
import com.seckill.storage.DataStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SeckillService {

    @Value("${order.timeout:30}")
    private Integer orderTimeout;

    public Result<Order> seckill(SeckillRequest request) {
        String activityId = request.getActivityId();
        String userId = request.getUserId();
        Integer quantity = request.getQuantity();

        if (quantity == null || quantity <= 0) {
            quantity = 1;
        }

        Statistics statistics = DataStorage.STATISTICS_MAP.get(activityId);
        if (statistics == null) {
            statistics = new Statistics();
        }

        if (DataStorage.BLACK_LIST.contains(userId)) {
            statistics.getFailCount().incrementAndGet();
            return Result.error("您已被加入黑名单，无法参与秒杀");
        }

        Activity activity = DataStorage.ACTIVITY_MAP.get(activityId);
        if (activity == null) {
            statistics.getFailCount().incrementAndGet();
            return Result.error("活动不存在");
        }

        if (!activity.getIsPreheated()) {
            statistics.getFailCount().incrementAndGet();
            return Result.error("活动库存未预热");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getStartTime())) {
            statistics.getFailCount().incrementAndGet();
            return Result.error("活动未开始");
        }
        if (now.isAfter(activity.getEndTime())) {
            statistics.getFailCount().incrementAndGet();
            return Result.error("活动已结束");
        }

        Map<String, Integer> userPurchaseMap = DataStorage.USER_PURCHASE_COUNT
                .computeIfAbsent(activityId, k -> new java.util.concurrent.ConcurrentHashMap<>());
        
        Integer purchased = userPurchaseMap.getOrDefault(userId, 0);
        if (purchased + quantity > activity.getLimitPerUser()) {
            statistics.getFailCount().incrementAndGet();
            return Result.error("超出限购数量，您已购买" + purchased + "件，最多可购买" + activity.getLimitPerUser() + "件");
        }

        AtomicInteger availableStock = activity.getAvailableStock();
        while (true) {
            int currentStock = availableStock.get();
            if (currentStock < quantity) {
                statistics.getFailCount().incrementAndGet();
                return Result.error("库存不足");
            }
            if (availableStock.compareAndSet(currentStock, currentStock - quantity)) {
                break;
            }
        }

        Order order = new Order();
        order.setId(UUID.randomUUID().toString().replace("-", ""));
        order.setActivityId(activityId);
        order.setUserId(userId);
        order.setQuantity(quantity);
        order.setStatus("PENDING_PAYMENT");
        order.setCreateTime(LocalDateTime.now());

        DataStorage.ORDER_MAP.put(order.getId(), order);

        userPurchaseMap.put(userId, purchased + quantity);

        statistics.getSuccessCount().incrementAndGet();
        statistics.getTotalOrderCount().incrementAndGet();

        return Result.success(order);
    }
}
