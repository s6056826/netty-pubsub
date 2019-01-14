package cn.dbw.client.handler;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;
import cn.dbw.event.EventBus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageProcessHandler extends SimpleChannelInboundHandler<Message>{	
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		FuncodeEnum funCode = msg.getFunCode();
		EventBus.handler.sendMsg(msg, funCode);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		super.exceptionCaught(ctx, cause);
	}

}
