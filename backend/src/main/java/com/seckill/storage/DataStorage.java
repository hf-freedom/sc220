package com.seckill.storage;

import com.seckill.entity.Activity;
import com.seckill.entity.Order;
import com.seckill.entity.Statistics;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class DataStorage {

    public static final Map<String, Activity> ACTIVITY_MAP = new ConcurrentHashMap<>();

    public static final Map<String, Order> ORDER_MAP = new ConcurrentHashMap<>();

    public static final Set<String> BLACK_LIST = new CopyOnWriteArraySet<>();

    public static final Map<String, Statistics> STATISTICS_MAP = new ConcurrentHashMap<>();

    public static final Map<String, Map<String, Integer>> USER_PURCHASE_COUNT = new ConcurrentHashMap<>();
}
