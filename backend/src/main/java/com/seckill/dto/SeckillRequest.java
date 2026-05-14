package com.seckill.dto;

public class SeckillRequest {
    private String activityId;
    private String userId;
    private Integer quantity;

    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
