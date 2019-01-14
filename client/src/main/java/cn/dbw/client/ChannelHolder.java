package cn.dbw.client;

import io.netty.channel.Channel;

public class ChannelHolder {

	private volatile static Channel channel;

	public static Channel getChannel() {
		while(channel==null){};
		return channel;
	}

	public static void setChannel(Channel chan) {
		channel = chan;
	}
	
	
	
	
}
