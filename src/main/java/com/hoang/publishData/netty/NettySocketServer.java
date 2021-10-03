package com.hoang.publishData.netty;

import com.google.gson.Gson;
import com.hoang.publishData.messages.WebSocketMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class NettySocketServer implements WebSocketServer {
    Map<String, ChannelGroup> channelGroupMap = new HashMap<>();
    ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final Gson gson = new GsonBuilder().create();

    @Autowired
    WebSocketHandler webSocketHandler;

    @Value("${socket.port}")
    Integer port;

    @Bean
    public void init() {
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();
        new Thread(() -> {
            try {
                ServerBootstrap server = new ServerBootstrap();
                server.group(mainGroup, subGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64));
                            pipeline.addLast(new WebSocketServerProtocolHandler("/test"));
                            pipeline.addLast(webSocketHandler);
                        }
                    });
                ChannelFuture future = server.bind(port).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mainGroup.shutdownGracefully();
                subGroup.shutdownGracefully();
            }
        }).start();
    }
    @Override
    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    @Override
    public void broadcast(WebSocketMessage message) {
        channelGroup.writeAndFlush(new TextWebSocketFrame(gson.toJson(message)));
    }

    @Override
    public Map<String, ChannelGroup> getChannelGroupMap() {
        return channelGroupMap;
    }
}
