package cn.dbw.container;

import java.util.Map;
import java.util.Set;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;
import cn.dbw.util.MD5Util;
import cn.dbw.util.StringTools;
import io.netty.channel.Channel;

public class TopicManager implements TopicHolder ,TopicBroadcastable{
	
	private TopicHolder topicHolder;
	
	public TopicManager(TopicHolderType topicHolderType) {
		switch (topicHolderType) {
		case MemoryTopicHolder:
			topicHolder=MemoryTopicHoder.getInstance();
			break;
		}
	}
	
	public void publish(byte[] topic,byte[] data){
		Set<Channel> set = topicHolder.getTopic(topic);
		if(set!=null&&set.size()>0){
		  set.parallelStream().forEach((c)->{
			  c.writeAndFlush(new Message(FuncodeEnum.MESSAGE_SEND, (byte)1, topic,data.length, data));
		  }); 
	    }
    }

	@Override
	public void remove(byte[] topic) {
		topicHolder.remove(topic);
		
	}

	@Override
	public void remove(byte[] topic, Channel channel) {
		topicHolder.remove(topic,channel);
	}

	@Override
	public void subscribe(byte[] topic, Channel channel) {
		topicHolder.subscribe(topic, channel);
	}

	@Override
	public Set<Channel> getTopic(byte[] topic) {
		return topicHolder.getTopic(topic);
	}

	@Override
	public Map<String, Set<Channel>> getTopicContainner() {
		return topicHolder.getTopicContainner();
	}
	
	
	public static enum TopicHolderType{
       MemoryTopicHolder;
	}


	@Override
	public void remove(Channel channel) {
	    topicHolder.remove(channel);
	}

	@Override
	public void brodcast(byte[] data) {
		publish(TopicBroadcastable.BROAD_TOPIC,data);
	}
    

}
