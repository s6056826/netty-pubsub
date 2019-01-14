package cn.dbw.client;

import java.util.HashSet;
import java.util.Set;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;

/**
 * ¶©ÔÄ¼ÇÂ¼Õß
 * @author dbw
 *
 */
public class SubRecorder {
	
    private static Set<String> topicRecorder=new HashSet<>();
    
    
    public static void record(String topic){
    	topicRecorder.add(topic);
    }
    
    public static void remove(String topic){
    	topicRecorder.remove(topic);
    }
    
    public static void recover(){
    	if(topicRecorder.size()>0){
    	topicRecorder.forEach((topic)->{
    		ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.TOPIC_SUBSCRIBE, (byte)1, topic.getBytes(),"subscribe".getBytes().length,"subscribe".getBytes()));
    	});
    	}
    }

}
