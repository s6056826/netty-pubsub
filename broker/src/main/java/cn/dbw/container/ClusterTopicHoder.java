package cn.dbw.container;

import java.util.Map;
import java.util.Set;

import cn.dbw.util.StringTools;
import cn.dbw.zk.ZkRegister;
import io.netty.channel.Channel;
import jodd.util.StringUtil;

/**
 *zk·Ö²¼Ê½ÈÝÆ÷
 * @author dbw
 *
 */
public class ClusterTopicHoder  implements TopicHolder{
	
	
	private TopicHolder memoryTopicHoder=null;
	
	public ClusterTopicHoder(TopicHolder topicHolder) {
		memoryTopicHoder=topicHolder;
	}
	

	@Override
	public void remove(byte[] topic) {
		 String strUTF = StringTools.toStrUTF(topic);
		 ZkRegister.getInstance().removeZkTopicRecord(ZkRegister.getInstance().getCurrentZKPath(), strUTF);
         memoryTopicHoder.remove(topic);
	}

	@Override
	public void remove(byte[] topic, Channel channel) {
		memoryTopicHoder.remove(topic, channel);
		if(memoryTopicHoder.getTopic(topic).size()==0){
			String strUTF = StringTools.toStrUTF(topic);
			ZkRegister.getInstance().removeZkTopicRecord(ZkRegister.getInstance().getCurrentZKPath(), strUTF);
		}
	}

	@Override
	public void remove(Channel channel) {
		memoryTopicHoder.remove(channel);
	}

	@Override
	public void subscribe(byte[] topic, Channel channel) {
		memoryTopicHoder.subscribe(topic, channel);
		String strUTF = StringTools.toStrUTF(topic);
		ZkRegister.getInstance().recordZkTopic(ZkRegister.getInstance().getCurrentZKPath(), strUTF);
	}

	@Override
	public Set<Channel> getTopic(byte[] topic) {
		return memoryTopicHoder.getTopic(topic);
	}

	@Override
	public Map<String, Set<Channel>> getTopicContainner() {
		return memoryTopicHoder.getTopicContainner();
	}

}
