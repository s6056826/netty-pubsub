package cn.dbw.server.handler;


import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;


public class ChanelInitializerHandler extends ChannelInitializer {

	@Override
	protected void initChannel(Channel ch) throws Exception {
	       ChannelPipeline pipeline = ch.pipeline();
	       pipeline.addLast(new IdleStateHandler(0,0,35));
	       pipeline.addLast(new IdleStateTrigger());
	       //设计包的格式 1字节固定包头  1字节功能码  1字节（判断是否包含topic字段） 4字节固定长度字段   12字节固定topic（非必须）  剩余字节数据
	       pipeline.addLast(new LengthFieldBasedFrameDecoder(2048, 3, 4, 0, 0));
	       pipeline.addLast(new MessageToPoDecoder());
	       //添加认证的处理器
	       //pipeline.addLast("auth",new AuthenticationHandler());
	       //添加协议处理器
	       pipeline.addLast( "message-process", new MessageProcessHandler());
	       pipeline.addLast(new MessageEncoder());
	       //pipeline.addLast("auth",new AuthenticationHandler());
	       //pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
	}

}
