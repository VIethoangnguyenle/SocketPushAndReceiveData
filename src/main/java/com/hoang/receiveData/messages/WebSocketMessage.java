package com.hoang.receiveData.messages;

import lombok.Data;

@Data
public class WebSocketMessage {
    private String provider;
    private Object data;

}
