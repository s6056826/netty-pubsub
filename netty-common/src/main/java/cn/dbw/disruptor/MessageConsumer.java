package cn.dbw.disruptor;

import java.util.UUID;

import com.lmax.disruptor.WorkHandler;

import cn.dbw.po.MessageWrapper;

public abstract class MessageConsumer implements WorkHandler<MessageWrapper> {
	
	protected String consumerId=UUID.randomUUID().toString();
	

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}
	

}
