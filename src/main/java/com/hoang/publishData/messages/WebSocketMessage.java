package com.hoang.publishData.messages;

import com.hoang.publishData.service.provider.SocketProvider;
import lombok.Data;

@Data
public class WebSocketMessage {
    private SocketProvider provider;
    private Object data;

    public WebSocketMessage(SocketProvider provider, Object data) {
        this.provider = provider;
        this.data = data;
    }

    public static WebSocketMessage buildData(SocketProvider provider, Object data) {
        return new WebSocketMessage(provider, data);
    }
}
