package cn.dbw.container;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.dbw.util.StringTools;
import io.netty.channel.Channel;
import jodd.util.StringUtil;

public class MemoryTopicHoder  implements TopicHolder{
	 
	private static volatile MemoryTopicHoder memoryTopicHoder=null;
	//存储一个主题对应的注册channel
	private Map<String,Set<Channel>> topicContainner=null; 
	//存储一个channel对应的注册主题
	private Map<Channel,Set<String>> channelRecordContainner=null;
	
    
	private MemoryTopicHoder() {
		topicContainner=new ConcurrentHashMap<>(16);
		channelRecordContainner=new ConcurrentHashMap<>(16);
	}
	
	protected static MemoryTopicHoder getInstance(){
		synchronized (MemoryTopicHoder.class) {
			if(memoryTopicHoder==null){
				synchronized (MemoryTopicHoder.class) {
					memoryTopicHoder=new MemoryTopicHoder();
				}
			}
			return memoryTopicHoder;
		}
	}
	
	@Override
	public void remove(byte[] topic) {
		if(null!=topic){
		String topicStr=StringTools.toStrUTF(topic);	
		boolean key = topicContainner.containsKey(topicStr);
		 if(key){
			//移除该key
			topicContainner.remove(topicStr);
		  }
		}
	}

	@Override
	public void subscribe(byte[] topic, Channel channel) {
		if(null!=topic){
			String topicStr=StringTools.toStrUTF(topic);
			boolean containsKey = topicContainner.containsKey(topicStr);
			if(containsKey){
			Set<Channel> clist = topicContainner.getOrDefault(topicStr, null);
			clist.add(channel);
		  }else{
			    HashSet<Channel> set = new HashSet<>();
				set.add(channel);
			    topicContainner.put(topicStr, set);
		  }
			
		  //向channelTopic表中添加
		  Set<String> chanTopicSet = channelRecordContainner.getOrDefault(channel, null);
		  if(null==chanTopicSet){
			  HashSet<String> channelTopicSet = new HashSet<>();
			  channelTopicSet.add(topicStr);
			  channelRecordContainner.put(channel, channelTopicSet);
		  }else{
			  chanTopicSet.add(topicStr);
		  }
		  
		}
	}

	@Override
	public void remove(byte[] topic, Channel channel) {
		if(null!=topic){
			String topicStr=StringTools.toStrUTF(topic);
			if(topicContainner.containsKey(topicStr)){
				Set<Channel> set = topicContainner.get(topicStr);
				if(channel!=null){
					if(set.contains(channel)){
						set.remove(channel);
					}
				}
			}
		}	
	}
	
	
	@Override
	public void remove(Channel channel) {
		Set<String> topics = channelRecordContainner.get(channel);
		if(topics!=null&&topics.size()>0){
			for(String s:topics){
				Set<Channel> channels = topicContainner.get(s);
				if(channels!=null)
				if(channels.contains(channel)){
					channels.remove(channel);
				}
			}
		}
	}
	
	
	public Map<String, Set<Channel>> getTopicContainner() {
		return topicContainner;
	}

	@Override
	public Set<Channel> getTopic(byte[] topic) {
		if(null!=topic){
			String topicStr=StringTools.toStrUTF(topic);
			if(topicContainner.containsKey(topicStr)){
				return topicContainner.get(topicStr);
			}
		}
		return null;
	}
     


}
