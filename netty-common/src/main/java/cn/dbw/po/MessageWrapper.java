package cn.dbw.po;

import cn.dbw.dto.Message;
import io.netty.channel.ChannelHandlerContext;
//2019/1/14  ÐÞ¸Ä
public class MessageWrapper {
	
	
	
	private ChannelHandlerContext ctx;
	
	private Message message;

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public MessageWrapper(ChannelHandlerContext ctx, Message message) {
		this.ctx = ctx;
		this.message = message;
	}
	

	public MessageWrapper() {
		// TODO Auto-generated constructor stub
	}
	
	
	

}
