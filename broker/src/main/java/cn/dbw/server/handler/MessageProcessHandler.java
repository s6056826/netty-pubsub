package cn.dbw.server.handler;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageProcessHandler extends SimpleChannelInboundHandler<Message> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		FuncodeEnum funCode = msg.getFunCode();
		switch (funCode) {
		case HEART_BEAT:
			//若是心跳包则不处理
			msg=null;	
			break;
		case TOPIC_SUBSCRIBE:
		case TOPIC_UNSUBSCRIBE:
		case MESSAGE_SEND:
		case MESSAGE_BROAD:
			if(!isHaveChanelCheck(ctx, "topic-manage")){
			ctx.pipeline().addLast("topic-manage",new TopicManagerHandler());
			}
			ctx.fireChannelRead(msg);
			break;
		default:
			//其它情况则直接断开连接
			ctx.close();
			break;	
		}
		
	}
	
	private boolean isHaveChanelCheck(ChannelHandlerContext ctx,String name){
		ChannelHandler channelHandler = ctx.pipeline().get(name);
		return channelHandler==null?false:true;
	}
	
//	case AUTH_USER:
//	if(!isHaveChanelCheck(ctx,"auth")){
//	ctx.pipeline().addLast("auth",new AuthenticationHandler());
//	}
//	ctx.fireChannelRead(msg);
//	break;

}
