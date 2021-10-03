package com.hoang.receiveData.websocket;

import com.neovisionaries.ws.client.WebSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Client {

    private final WebSocketFactory webSocketFactory = new WebSocketFactory();
    private WebSocketClient webSocketClient;

    @Bean
    public void startProvider() {
        webSocketClient = new WebSocketClient(webSocketFactory, getWEbSocketListener());
    }

    private WebSocketListener getWEbSocketListener() {
        return ((webSocket, message) -> {
        });
    }

    @Scheduled(cron = "0 55 8 * * 0-6", zone = "Asia/Saigon")
    public void retryWebsockets() {
        webSocketClient.retryConnect();
    }
}
