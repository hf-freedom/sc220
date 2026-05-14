package com.seckill.scheduler;

import com.seckill.entity.Order;
import com.seckill.service.OrderService;
import com.seckill.storage.DataStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class OrderReleaseScheduler {

    private static final Logger logger = LoggerFactory.getLogger(OrderReleaseScheduler.class);

    @Autowired
    private OrderService orderService;

    @Value("${order.timeout:30}")
    private Integer orderTimeout;

    @Scheduled(fixedRateString = "${scheduler.order-release:10000}")
    public void releaseTimeoutOrders() {
        LocalDateTime now = LocalDateTime.now();
        
        for (Map.Entry<String, Order> entry : DataStorage.ORDER_MAP.entrySet()) {
            Order order = entry.getValue();
            
            if ("PENDING_PAYMENT".equals(order.getStatus())) {
                LocalDateTime expireTime = order.getCreateTime().plusSeconds(orderTimeout);
                if (now.isAfter(expireTime)) {
                    logger.info("订单超时释放，订单ID: {}, 用户ID: {}, 数量: {}", 
                        order.getId(), order.getUserId(), order.getQuantity());
                    
                    orderService.doReleaseOrder(order);
                }
            }
        }
    }
}
