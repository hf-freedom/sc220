package com.seckill.dto;

public class ActivityRequest {
    private String name;
    private String productName;
    private Integer totalStock;
    private Integer limitPerUser;
    private String startTime;
    private String endTime;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getTotalStock() { return totalStock; }
    public void setTotalStock(Integer totalStock) { this.totalStock = totalStock; }
    public Integer getLimitPerUser() { return limitPerUser; }
    public void setLimitPerUser(Integer limitPerUser) { this.limitPerUser = limitPerUser; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
