package com.seckill.entity;

import java.time.LocalDateTime;

public class Order {
    private String id;
    private String activityId;
    private String userId;
    private Integer quantity;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime payTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getPayTime() { return payTime; }
    public void setPayTime(LocalDateTime payTime) { this.payTime = payTime; }
}
