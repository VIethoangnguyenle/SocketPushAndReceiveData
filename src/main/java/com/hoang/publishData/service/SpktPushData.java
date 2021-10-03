package com.hoang.publishData.service;

import com.hoang.publishData.dto.StockInfo;
import com.hoang.publishData.service.provider.SocketProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SpktPushData extends BaseWebSocketService {

    SecureRandom random = new SecureRandom();
    private final List<String> symbolList = Arrays.asList("ABI", "BIC", "BID", "FPT", "SSG", "VIC", "VIN");

    @Bean
    public void initSPK() {
        new Thread(() -> {
            try {
                while (true) {
                    StockInfo stockInfo = new StockInfo()
                            .setSymbol(symbolList.get(random.nextInt(7)))
                            .setCeilPrice(random.nextInt(30))
                            .setFloorPrice(random.nextInt(50))
                            .setMatchPrice(random.nextInt(40));
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
        return SocketProvider.SPKT;
    }
}
