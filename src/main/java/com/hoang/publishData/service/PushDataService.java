package com.hoang.publishData.service;

import com.hoang.publishData.dto.StockInfo;

public interface PushDataService {
    void pushStockRealTime(StockInfo stockInfo);
}
