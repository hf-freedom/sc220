package com.seckill.controller;

import com.seckill.common.Result;
import com.seckill.entity.Order;
import com.seckill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/pay/{orderId}")
    public Result<Order> payOrder(@PathVariable String orderId) {
        return orderService.payOrder(orderId);
    }

    @GetMapping("/list")
    public Result<List<Order>> listOrders() {
        return orderService.listOrders();
    }

    @GetMapping("/{orderId}")
    public Result<Order> getOrder(@PathVariable String orderId) {
        return orderService.getOrder(orderId);
    }

    @PostMapping("/release/{orderId}")
    public Result<String> releaseOrder(@PathVariable String orderId) {
        return orderService.releaseOrder(orderId);
    }
}
