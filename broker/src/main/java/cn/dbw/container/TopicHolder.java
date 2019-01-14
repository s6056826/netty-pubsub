package cn.dbw.container;

import java.util.Map;
import java.util.Set;

import io.netty.channel.Channel;

public interface TopicHolder {
	
	//删除主题
    void remove(byte[] topic);
    //取消单个订阅主题对象
    void remove(byte[] topic,Channel channel);
    //通过channel 移除从所有主题中移除
    void remove(Channel channel);
    
    //订阅主题
    void subscribe(byte[] topic,Channel channel);
    
    
    //获取主题
    Set<Channel> getTopic(byte [] topic);
    
    //获取订阅容器
    public Map<String, Set<Channel>> getTopicContainner();
    
    
	

}
