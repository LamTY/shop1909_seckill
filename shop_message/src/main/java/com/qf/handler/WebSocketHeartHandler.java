package com.qf.handler;

import com.qf.entity.WsMsgEntity;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class WebSocketHeartHandler extends SimpleChannelInboundHandler<WsMsgEntity> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WsMsgEntity wsMsgEntity) throws Exception {
        if(wsMsgEntity.getType() == 2){
            ctx.writeAndFlush(wsMsgEntity);
        }else {
            ctx.fireChannelRead(wsMsgEntity);
        }
    }
}
