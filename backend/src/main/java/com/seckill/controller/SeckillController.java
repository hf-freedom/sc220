package com.seckill.controller;

import com.seckill.common.Result;
import com.seckill.dto.SeckillRequest;
import com.seckill.entity.Order;
import com.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seckill")
@CrossOrigin
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @PostMapping("/do")
    public Result<Order> seckill(@RequestBody SeckillRequest request) {
        return seckillService.seckill(request);
    }
}
