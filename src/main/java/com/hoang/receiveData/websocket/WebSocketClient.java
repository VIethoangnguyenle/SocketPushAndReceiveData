package com.hoang.receiveData.websocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
public class WebSocketClient {
    private final WebSocketFactory webSocketFactory;
    private final WebSocketListener listener;
    private WebSocket webSocket;

    public WebSocketClient(WebSocketFactory webSocketFactory, WebSocketListener listener) {
        this.webSocketFactory = webSocketFactory;
        this.listener = listener;
        connect();
    }

    public void retryConnect() {
        if (webSocket != null && webSocket.isOpen()) {
            connect();
        }
    }

    private WebSocketAdapter getWebSocketAdapter() {
        return new WebSocketAdapter(){
            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                websocket.getSocket().setKeepAlive(true);
                websocket.getSocket().setTcpNoDelay(true);
                websocket.getSocket().setPerformancePreferences(0, 1, 2);
            }

            @Override
            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                connect();
            }

            @Override
            public void onTextMessage(WebSocket websocket, String message) throws Exception {
                listener.onTextMessage(webSocket, message);
                log.info("receive: {} ", message);
            }
        };
    }

    private void connect() {
        try {
            webSocket = webSocketFactory.createSocket("ws://localhost:6667/test");
            webSocket.addListener(getWebSocketAdapter());
            webSocket.setAutoFlush(true);
            webSocket.setPingInterval(3000);
            webSocket.setPongInterval(3000);
            webSocket.connect();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
