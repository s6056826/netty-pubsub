package cn.dbw.server;

import org.apache.log4j.Logger;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.container.TopicManager;
import cn.dbw.container.TopicManager.TopicHolderType;
import cn.dbw.disruptor.MessageConsumer;
import cn.dbw.dto.Message;
import cn.dbw.po.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

//disruptor的消费者
public class MessageConsumerImpl4Server extends MessageConsumer{

	private final Logger LOGGER=Logger.getLogger(MessageConsumerImpl4Server.class);
	private final static TopicManager topicManager=new TopicManager(TopicHolderType.MemoryTopicHolder);
	

	@Override
	public void onEvent(MessageWrapper event) throws Exception {
		ChannelHandlerContext ctx = event.getCtx();
		Message msg = event.getMessage();
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

}
