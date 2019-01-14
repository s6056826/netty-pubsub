package cn.dbw.client;

import org.apache.log4j.Logger;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.disruptor.MessageConsumer;
import cn.dbw.dto.Message;
import cn.dbw.event.EventBus;
import cn.dbw.po.MessageWrapper;
//disruptor的消费者
public class MessageConsumerImpl4Client extends MessageConsumer{

	private final Logger LOGGER=Logger.getLogger(MessageConsumerImpl4Client.class);
	

	@Override
	public void onEvent(MessageWrapper event) throws Exception {
		Message msg = event.getMessage();
		FuncodeEnum funCode = msg.getFunCode();
		EventBus.handler.sendMsg(msg, funCode);
	}

}
