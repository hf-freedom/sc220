package com.seckill.entity;

import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    private String activityId;
    private AtomicInteger successCount;
    private AtomicInteger failCount;
    private AtomicInteger paidCount;
    private AtomicInteger totalOrderCount;

    public Statistics() {
        this.successCount = new AtomicInteger(0);
        this.failCount = new AtomicInteger(0);
        this.paidCount = new AtomicInteger(0);
        this.totalOrderCount = new AtomicInteger(0);
    }

    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }
    public AtomicInteger getSuccessCount() { return successCount; }
    public void setSuccessCount(AtomicInteger successCount) { this.successCount = successCount; }
    public AtomicInteger getFailCount() { return failCount; }
    public void setFailCount(AtomicInteger failCount) { this.failCount = failCount; }
    public AtomicInteger getPaidCount() { return paidCount; }
    public void setPaidCount(AtomicInteger paidCount) { this.paidCount = paidCount; }
    public AtomicInteger getTotalOrderCount() { return totalOrderCount; }
    public void setTotalOrderCount(AtomicInteger totalOrderCount) { this.totalOrderCount = totalOrderCount; }
}
