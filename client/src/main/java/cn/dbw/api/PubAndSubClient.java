package cn.dbw.api;

import cn.dbw.api.PubAndSubClient.SubscribListener;
import cn.dbw.dto.Message;

public interface PubAndSubClient {
	//客户端连接
	void connect(String host,Integer port);

	//客户端订阅消息
	void subscribe(String topic,SubscribListener subscribListener);
	//客户端取消订阅
	void unsubscribe(String topic);
	//客户端发布消息
	void publish(String topic,String str);
	//客户端广播
	void broadcast(String data);
	
	
	public static interface AutuListener extends Listener{
		//认证成功回调
		void  authOk(Message message);
        
		//认证失败回调
		void authFail(Message message);
	}
	
	/**
	 * 订阅回调接口
	 * @author dbw
	 *
	 */
	public static interface SubscribListener extends Listener{
		//订阅成功回调
		default void subOk(Message message){};
		
		//订阅失败回调
//		default void subFail(Message message){};
		
		//消息到达回调
		default void msgActive(Message message){};
		
		//取消订阅回调
		default void unSubOk(Message message){};
	    
	}
	
	
	
	
	
	public static interface Listener{
		
	}





}
