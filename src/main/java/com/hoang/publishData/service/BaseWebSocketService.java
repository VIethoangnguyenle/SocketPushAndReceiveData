package com.hoang.publishData.service;

import com.hoang.publishData.dto.StockInfo;
import com.hoang.publishData.service.gateway.PushDataGateway;
import com.hoang.publishData.service.provider.WebSocketProvider;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseWebSocketService implements WebSocketProvider {
    @Autowired
    protected PushDataGateway pushDataGateway;

    void pushStockRealTime(StockInfo stockInfo) {
        pushDataGateway.pushStockRealTime(getProvider(), stockInfo);
    }
}
