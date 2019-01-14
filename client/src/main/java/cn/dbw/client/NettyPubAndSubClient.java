package cn.dbw.client;

import com.alibaba.fastjson.JSONObject;

import cn.dbw.api.Authable;
import cn.dbw.api.PubAndSubClient;
import cn.dbw.config.FuncodeEnum;
import cn.dbw.config.ServerConfig;
import cn.dbw.dto.Message;
import cn.dbw.event.EventBus;
import cn.dbw.util.MD5Util;

public class NettyPubAndSubClient implements PubAndSubClient,Authable{
	
	
	private final static NettyPubAndSubClient nettyPubAndSubClient=new NettyPubAndSubClient();
	
	     
	
	
	
	public static NettyPubAndSubClient getInstance(){
		 return nettyPubAndSubClient;
	}
	
	public NettyPubAndSubClient connect(String host,String port){
		NettyClient.INSTANCE.setConfig(new ServerConfig(host,String.valueOf(port))).start();
    	while(null==ChannelHolder.getChannel()){
    	    try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return nettyPubAndSubClient;
	}
	
	
	
	
    private NettyPubAndSubClient() {
    
	}
    
    //是否接收广播消息
    public NettyPubAndSubClient acceptBraodCast(SubscribListener subscribListener){
    	subscribe(FuncodeEnum.MESSAGE_BROAD.name().concat("$"), subscribListener);
    	return this;
    }
	

	@Override
	public void auth(String username, String password, AutuListener autuListener) {
		if(!checkConnect()){
			throw new RuntimeException("请连接后重试");
		}
		EventBus.setAutuListener(autuListener);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", username);
		jsonObject.put("password", password);
		System.out.println(ChannelHolder.getChannel());
		ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.AUTH_USER,(byte)0 , null, jsonObject.toString().getBytes().length, jsonObject.toString().getBytes()));
	}

	@Override
	public void subscribe(String topic, SubscribListener subscribListener) {
	    EventBus.setSubscribListener(MD5Util.getPwd(topic).substring(0, 12),subscribListener);
	    ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.TOPIC_SUBSCRIBE, (byte)1, ecodeTopic(topic),"subscribe".getBytes().length,"subscribe".getBytes()));
	    SubRecorder.record(MD5Util.getPwd(topic).substring(0, 12));
	}

	@Override
	public void unsubscribe(String topic) {
		SubRecorder.remove(MD5Util.getPwd(topic).substring(0, 12));
	    ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.TOPIC_UNSUBSCRIBE, (byte)1, ecodeTopic(topic),"unsubscribe".getBytes().length,"unsubscribe".getBytes()));
	}

	@Override
	public void publish(String topic,String str) {
	    try {
			ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.MESSAGE_SEND, (byte)1, ecodeTopic(topic),str.getBytes("utf-8").length,str.getBytes("utf-8")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void broadcast(String data) {
		  try {
				ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.MESSAGE_BROAD, (byte)0, null,data.getBytes("utf-8").length,data.getBytes("utf-8")));
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public boolean checkConnect(){
		return null==ChannelHolder.getChannel()?false:true;
	}
	
	private byte[] ecodeTopic(String topic){
		return MD5Util.getPwd(topic).substring(0, 12).getBytes();
	}
	
	
	public void shutdown(){
		
		NettyClient.INSTANCE.stop();
	}



	@Override
	public void connect(String host, Integer port) {
		NettyClient.INSTANCE.setConfig(new ServerConfig(host,String.valueOf(port))).start();
    	while(null==ChannelHolder.getChannel()){
    	    try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}







	

}
