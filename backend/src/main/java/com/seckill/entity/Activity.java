package com.seckill.entity;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Activity {
    private String id;
    private String name;
    private String productName;
    private Integer totalStock;
    private AtomicInteger availableStock;
    private Integer limitPerUser;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isPreheated = false;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getTotalStock() { return totalStock; }
    public void setTotalStock(Integer totalStock) { this.totalStock = totalStock; }
    public AtomicInteger getAvailableStock() { return availableStock; }
    public void setAvailableStock(AtomicInteger availableStock) { this.availableStock = availableStock; }
    public Integer getLimitPerUser() { return limitPerUser; }
    public void setLimitPerUser(Integer limitPerUser) { this.limitPerUser = limitPerUser; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Boolean getIsPreheated() { return isPreheated; }
    public void setIsPreheated(Boolean isPreheated) { this.isPreheated = isPreheated; }
}
