package com.hoang.publishData.service.gateway;

import com.hoang.publishData.dto.StockInfo;
import com.hoang.publishData.service.provider.SocketProvider;
import com.hoang.publishData.messages.WebSocketMessage;
import com.hoang.publishData.netty.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PushDataGatewayImpl implements PushDataGateway{

    @Value("${socket.provider}")
    String socketProvider;

    @Autowired
    WebSocketServer webSocketServer;

    @Override
    public void pushStockRealTime(SocketProvider provider, StockInfo stockInfo) {
        if (isAllow(provider)) {
            webSocketServer.broadcast(WebSocketMessage.buildData(provider, stockInfo));
        }
    }

    public boolean isAllow(SocketProvider provider){
        return socketProvider.equals(provider.name()) || socketProvider.equals("ALL");
    }
}
