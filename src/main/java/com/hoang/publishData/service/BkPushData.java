package com.hoang.publishData.service;

import com.hoang.publishData.dto.StockInfo;
import com.hoang.publishData.service.provider.SocketProvider;
import com.hoang.publishData.service.provider.WebSocketProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

@Service
public class BkPushData extends BaseWebSocketService{

    SecureRandom random = new SecureRandom();
    private final List<String> symbolList = Arrays.asList("ABB", "VIC", "BIL", "SSC", "SCG", "VIB", "VNN");

    @Bean
    public void initBK() {
        new Thread(() -> {
            try {
                while (true) {
                    StockInfo stockInfo = new StockInfo()
                            .setSymbol(symbolList.get(random.nextInt(7)))
                            .setFloorPrice(random.nextInt(10))
                            .setMatchPrice(random.nextInt(70))
                            .setCeilPrice(random.nextInt(50));
                    pushStockRealTime(stockInfo);
                    Thread.sleep(50);
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public SocketProvider getProvider() {
        return SocketProvider.BK;
    }
}
