package cn.dbw.server.handler;



import org.apache.log4j.Logger;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.container.TopicManager;
import cn.dbw.container.TopicManager.TopicHolderType;
import cn.dbw.dto.Message;
import cn.dbw.server.BrokeServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TopicManagerHandler extends SimpleChannelInboundHandler<Message> {
    
	private final Logger LOGGER=Logger.getLogger(BrokeServer.class);
	private final static TopicManager topicManager=new TopicManager(TopicHolderType.MemoryTopicHolder);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
	        FuncodeEnum funCode = msg.getFunCode();
	        switch (funCode) {
			case TOPIC_SUBSCRIBE:
				topicManager.subscribe(msg.getTopic(),ctx.channel());
				ctx.channel().writeAndFlush(new Message(FuncodeEnum.NOTICE_SUBSCRIBE_OK, (byte)1, msg.getTopic(),"SUBOK".getBytes().length , "SUBOK".getBytes()));
				LOGGER.info("【主题订阅】"+ctx.channel().remoteAddress().toString()+" topic-》"+new String(msg.getTopic(),"utf-8"));
				break;
			case TOPIC_UNSUBSCRIBE:
				topicManager.remove(msg.getTopic(), ctx.channel());
				ctx.channel().writeAndFlush(new Message(FuncodeEnum.NOTICE_UNSUBSCRIBE_OK, (byte)1, msg.getTopic(),"UNSUBOK".getBytes().length , "UNSUBOK".getBytes()));
				LOGGER.info("【主题取消订阅】"+ctx.channel().remoteAddress().toString()+" topic-》"+new String(msg.getTopic()));
				break;
			case MESSAGE_SEND:
				//LOGGER.info("【发布消息】"+ctx.channel().remoteAddress().toString()+" topic-》"+new String(msg.getTopic(),"utf-8")+" msg-》"+new String(msg.getData(),"utf-8"));
			    topicManager.publish(msg.getTopic(),msg.getData());
				break;
			case MESSAGE_BROAD:
				//广播消息
				topicManager.brodcast(msg.getData());
				break;
			}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		topicManager.remove(ctx.channel());
		ctx.channel().close();
		super.channelInactive(ctx);
	}

	

}
