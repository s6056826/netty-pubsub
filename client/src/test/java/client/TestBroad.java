package client;

import cn.dbw.client.NettyPubAndSubClient;

public class TestBroad {

	public static void main(String[] args) {
		NettyPubAndSubClient client = NettyPubAndSubClient.getInstance();
		client.connect("127.0.0.1",9999);
        client.broadcast("hello ¹ã²¥");
	}
}
