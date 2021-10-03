package com.hoang.publishData.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    WebSocketServer webSocketServer;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        webSocketServer.getChannelGroup().add(ctx.channel());
        addSubscriber(ctx.channel(), Collections.singletonList(Command.SUBSCRIBE.name()));
        log.info("client_connected {} : {}", webSocketServer.getChannelGroup().size(), ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("client_disconnected {} : {}", webSocketServer.getChannelGroup().size(), ctx.channel().id().asLongText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("channel read {}", msg.text());
    }

    private void addSubscriber(Channel channel, List<String> topics) {
        topics.forEach(topic -> {
            if (!webSocketServer.getChannelGroupMap().containsKey(topic)) {
                log.info("Add topic {}", topic);
                webSocketServer.getChannelGroupMap().put(topic, new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
            }
            webSocketServer.getChannelGroupMap().get(topic).add(channel);
        });
    }

    private void removeSubscriber(Channel channel) {
        for (Map.Entry<String, ChannelGroup> entry : webSocketServer.getChannelGroupMap().entrySet()) {
            entry.getValue().remove(channel);
        }
    }
}
