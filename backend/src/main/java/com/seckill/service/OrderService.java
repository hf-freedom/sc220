package com.seckill.service;

import com.seckill.common.Result;
import com.seckill.entity.Activity;
import com.seckill.entity.Order;
import com.seckill.entity.Statistics;
import com.seckill.storage.DataStorage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    public Result<Order> payOrder(String orderId) {
        Order order = DataStorage.ORDER_MAP.get(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        if (!"PENDING_PAYMENT".equals(order.getStatus())) {
            return Result.error("订单状态不正确");
        }

        order.setStatus("PAID");
        order.setPayTime(LocalDateTime.now());

        Statistics statistics = DataStorage.STATISTICS_MAP.get(order.getActivityId());
        if (statistics != null) {
            statistics.getPaidCount().incrementAndGet();
        }

        return Result.success(order);
    }

    public Result<List<Order>> listOrders() {
        return Result.success(new ArrayList<>(DataStorage.ORDER_MAP.values()));
    }

    public Result<Order> getOrder(String orderId) {
        Order order = DataStorage.ORDER_MAP.get(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }

    public Result<String> releaseOrder(String orderId) {
        Order order = DataStorage.ORDER_MAP.get(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        if (!"PENDING_PAYMENT".equals(order.getStatus())) {
            return Result.error("只能释放待支付订单");
        }

        this.doReleaseOrder(order);

        return Result.success("订单已取消，库存已释放");
    }

    public void doReleaseOrder(Order order) {
        Activity activity = DataStorage.ACTIVITY_MAP.get(order.getActivityId());
        if (activity != null) {
            activity.getAvailableStock().addAndGet(order.getQuantity());
        }

        Map<String, Integer> userPurchaseMap = DataStorage.USER_PURCHASE_COUNT.get(order.getActivityId());
        if (userPurchaseMap != null) {
            Integer current = userPurchaseMap.get(order.getUserId());
            if (current != null && current > 0) {
                userPurchaseMap.put(order.getUserId(), Math.max(0, current - order.getQuantity()));
            }
        }

        order.setStatus("CANCELLED");
    }
}
