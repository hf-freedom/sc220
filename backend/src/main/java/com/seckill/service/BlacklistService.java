package com.seckill.service;

import com.seckill.common.Result;
import com.seckill.storage.DataStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BlacklistService {

    public Result<String> addToBlacklist(String userId) {
        DataStorage.BLACK_LIST.add(userId);
        return Result.success("已加入黑名单");
    }

    public Result<String> removeFromBlacklist(String userId) {
        DataStorage.BLACK_LIST.remove(userId);
        return Result.success("已从黑名单移除");
    }

    public Result<List<String>> listBlacklist() {
        return Result.success(new ArrayList<>(DataStorage.BLACK_LIST));
    }

    public Result<Boolean> isInBlacklist(String userId) {
        return Result.success(DataStorage.BLACK_LIST.contains(userId));
    }
}
