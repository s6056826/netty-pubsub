package cn.dbw.server.handler;

import org.apache.log4j.Logger;

import cn.dbw.server.BrokeServer;
import cn.dbw.util.DateUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public class IdleStateTrigger extends ChannelInboundHandlerAdapter {
	
	private final Logger LOGGER=Logger.getLogger(IdleStateTrigger.class);


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event= (IdleStateEvent) evt;
            switch (event.state()){
                case READER_IDLE:
                	 //服务端触发事件，说明客户端已经掉线，关闭失效连接
                	LOGGER.error("【客户端已断开】"+ctx.channel().remoteAddress()+"  "+DateUtils.getCurrentDateTime());
                    ctx.close();
                    break;
            }
        }
        super.userEventTriggered(ctx, evt);
    }
   
}
