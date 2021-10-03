package com.hoang.receiveData.websocket;

import com.neovisionaries.ws.client.WebSocket;

public interface WebSocketListener {
    void onTextMessage(WebSocket webSocket, String message);
}
