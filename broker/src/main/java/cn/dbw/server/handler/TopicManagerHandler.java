package cn.dbw.server.handler;



import org.apache.log4j.Logger;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.container.TopicManager;
import cn.dbw.container.TopicManager.TopicHolderType;
import cn.dbw.disruptor.MessageProducer;
import cn.dbw.disruptor.RingBufferWorkerPoolFactory;
import cn.dbw.dto.Message;
import cn.dbw.server.BrokeServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TopicManagerHandler extends SimpleChannelInboundHandler<Message> {
    
    private final static TopicManager topicManager=new TopicManager(TopicHolderType.MemoryTopicHolder);
    
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		    //交给disruptor生产者进行管理
    	    //自已的应用服务应该有一个ID生成规则
         	String producerId = "product:sessionId:001";
    	    MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
    	    messageProducer.onData(ctx, msg);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		topicManager.remove(ctx.channel());
		ctx.channel().close();
		super.channelInactive(ctx);
	}

	

}
