package cn.dbw.client.handler;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.disruptor.MessageProducer;
import cn.dbw.disruptor.RingBufferWorkerPoolFactory;
import cn.dbw.dto.Message;
import cn.dbw.event.EventBus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageProcessHandler extends SimpleChannelInboundHandler<Message>{	
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		//∑≈»Îdisruptor»›∆˜÷–
		String producerId = "product:sessionId:001";
	    MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
	    messageProducer.onData(ctx, msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		super.exceptionCaught(ctx, cause);
	}

}
