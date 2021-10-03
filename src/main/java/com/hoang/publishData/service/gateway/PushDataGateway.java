package com.hoang.publishData.service.gateway;

import com.hoang.publishData.dto.StockInfo;
import com.hoang.publishData.service.provider.SocketProvider;

public interface PushDataGateway {
    void pushStockRealTime(SocketProvider provider, StockInfo stockInfo);
}
