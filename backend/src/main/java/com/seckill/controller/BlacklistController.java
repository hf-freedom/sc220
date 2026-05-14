package com.seckill.controller;

import com.seckill.common.Result;
import com.seckill.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blacklist")
@CrossOrigin
public class BlacklistController {

    @Autowired
    private BlacklistService blacklistService;

    @PostMapping("/add/{userId}")
    public Result<String> addToBlacklist(@PathVariable String userId) {
        return blacklistService.addToBlacklist(userId);
    }

    @PostMapping("/remove/{userId}")
    public Result<String> removeFromBlacklist(@PathVariable String userId) {
        return blacklistService.removeFromBlacklist(userId);
    }

    @GetMapping("/list")
    public Result<List<String>> listBlacklist() {
        return blacklistService.listBlacklist();
    }

    @GetMapping("/check/{userId}")
    public Result<Boolean> isInBlacklist(@PathVariable String userId) {
        return blacklistService.isInBlacklist(userId);
    }
}
