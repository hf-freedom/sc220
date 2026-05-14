package com.seckill.controller;

import com.seckill.common.Result;
import com.seckill.dto.ActivityRequest;
import com.seckill.entity.Activity;
import com.seckill.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activity")
@CrossOrigin
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping("/create")
    public Result<Activity> createActivity(@RequestBody ActivityRequest request) {
        return activityService.createActivity(request);
    }

    @PostMapping("/preheat/{activityId}")
    public Result<String> preheatStock(@PathVariable String activityId) {
        return activityService.preheatStock(activityId);
    }

    @GetMapping("/list")
    public Result<List<Activity>> listActivities() {
        return activityService.listActivities();
    }

    @GetMapping("/{activityId}")
    public Result<Activity> getActivity(@PathVariable String activityId) {
        return activityService.getActivity(activityId);
    }

    @GetMapping("/statistics/{activityId}")
    public Result<Map<String, Object>> getStatistics(@PathVariable String activityId) {
        return activityService.getStatistics(activityId);
    }

    @GetMapping("/user-purchase/{activityId}/{userId}")
    public Result<Integer> getUserPurchaseCount(@PathVariable String activityId, @PathVariable String userId) {
        return activityService.getUserPurchaseCount(activityId, userId);
    }
}
