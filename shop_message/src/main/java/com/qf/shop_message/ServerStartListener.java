package com.qf.shop_message;

import com.qf.handler.WebSocketHeartHandler;
import com.qf.handler.WebSocketMsgHandler;
import com.qf.handler.WebSocketOutHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ServerStartListener implements CommandLineRunner {

    @Autowired
    private WebSocketMsgHandler webSocketMsgHandler;

    @Autowired
    private WebSocketHeartHandler webSocketHeartHandler;

    @Autowired
    private WebSocketOutHandler webSocketOutHandler;

    @Override
    public void run(String... args) throws Exception {

        EventLoopGroup master = new NioEventLoopGroup();
        EventLoopGroup slave = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(master, slave)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));

                        pipeline.addLast(new WebSocketServerProtocolHandler("/msg"));
                        pipeline.addLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS));
                        pipeline.addLast(webSocketOutHandler);
                        pipeline.addLast(webSocketMsgHandler);
                        pipeline.addLast(webSocketHeartHandler);
                    }
                });

    }
}
