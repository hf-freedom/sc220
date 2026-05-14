package com.seckill.service;

import com.seckill.common.Result;
import com.seckill.dto.ActivityRequest;
import com.seckill.entity.Activity;
import com.seckill.entity.Statistics;
import com.seckill.storage.DataStorage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ActivityService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Result<Activity> createActivity(ActivityRequest request) {
        Activity activity = new Activity();
        activity.setId(UUID.randomUUID().toString().replace("-", ""));
        activity.setName(request.getName());
        activity.setProductName(request.getProductName());
        activity.setTotalStock(request.getTotalStock());
        activity.setAvailableStock(new AtomicInteger(request.getTotalStock()));
        activity.setLimitPerUser(request.getLimitPerUser());
        activity.setStartTime(LocalDateTime.parse(request.getStartTime(), FORMATTER));
        activity.setEndTime(LocalDateTime.parse(request.getEndTime(), FORMATTER));
        activity.setIsPreheated(false);

        DataStorage.ACTIVITY_MAP.put(activity.getId(), activity);

        Statistics statistics = new Statistics();
        statistics.setActivityId(activity.getId());
        DataStorage.STATISTICS_MAP.put(activity.getId(), statistics);

        return Result.success(activity);
    }

    public Result<String> preheatStock(String activityId) {
        Activity activity = DataStorage.ACTIVITY_MAP.get(activityId);
        if (activity == null) {
            return Result.error("活动不存在");
        }

        activity.setIsPreheated(true);
        return Result.success("库存预热成功");
    }

    public Result<List<Activity>> listActivities() {
        return Result.success(new ArrayList<>(DataStorage.ACTIVITY_MAP.values()));
    }

    public Result<Activity> getActivity(String activityId) {
        Activity activity = DataStorage.ACTIVITY_MAP.get(activityId);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        return Result.success(activity);
    }

    public Result<Integer> getUserPurchaseCount(String activityId, String userId) {
        Map<String, Integer> userPurchaseMap = DataStorage.USER_PURCHASE_COUNT.get(activityId);
        Integer count = 0;
        if (userPurchaseMap != null) {
            count = userPurchaseMap.getOrDefault(userId, 0);
        }
        return Result.success(count);
    }

    public Result<Map<String, Object>> getStatistics(String activityId) {
        Activity activity = DataStorage.ACTIVITY_MAP.get(activityId);
        if (activity == null) {
            return Result.error("活动不存在");
        }

        Statistics statistics = DataStorage.STATISTICS_MAP.get(activityId);
        if (statistics == null) {
            statistics = new Statistics();
        }

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("activityId", activityId);
        result.put("activityName", activity.getName());
        result.put("totalStock", activity.getTotalStock());
        result.put("availableStock", activity.getAvailableStock().get());
        result.put("successCount", statistics.getSuccessCount().get());
        result.put("failCount", statistics.getFailCount().get());
        result.put("paidCount", statistics.getPaidCount().get());
        result.put("totalOrderCount", statistics.getTotalOrderCount().get());
        
        double paymentRate = statistics.getTotalOrderCount().get() > 0 
            ? (statistics.getPaidCount().get() * 100.0 / statistics.getTotalOrderCount().get()) 
            : 0;
        result.put("paymentRate", String.format("%.2f%%", paymentRate));

        return Result.success(result);
    }
}
