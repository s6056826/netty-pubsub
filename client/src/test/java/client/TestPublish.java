package client;


import cn.dbw.api.PubAndSubClient.AutuListener;
import cn.dbw.client.ChannelHolder;
import cn.dbw.client.NettyPubAndSubClient;
import cn.dbw.dto.Message;

public class TestPublish {
	
	public static void main(String[] args) {
		NettyPubAndSubClient client = NettyPubAndSubClient.getInstance();
		client.connect("127.0.0.1",9999);
//		client.auth("dbw", "123", new AutuListener() {
//			
//			@Override
//			public void authOk(Message message) {
//				System.out.println("ok");
//				
//			}
//			
//			@Override
//			public void authFail(Message message) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		for(int i=0;i<10000;i++){
//			client.publish("haha","ÄãºÃ°¡°¡°¡°¡"+i);
//		}
		
		//ChannelHolder.getChannel().close();
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		for(int i=0;i<500;i++){
			client.publish("mm","love mm"+i);
		}
		client.shutdown();
	}

}
