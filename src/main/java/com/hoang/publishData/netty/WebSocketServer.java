package com.hoang.publishData.netty;

import com.hoang.publishData.messages.WebSocketMessage;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;

public interface WebSocketServer {
    ChannelGroup getChannelGroup();
    void broadcast(WebSocketMessage message);
    Map<String, ChannelGroup> getChannelGroupMap();
}
