package cn.dbw.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.dbw.client.handler.IdleStateTrigger;
import cn.dbw.client.handler.MessageEncoder;
import cn.dbw.client.handler.MessageProcessHandler;
import cn.dbw.client.handler.MessageToPoDecoder;
import cn.dbw.client.listener.ConnectionListener;
import cn.dbw.config.ServerConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyClient {
	
	private NioEventLoopGroup loopGroup=null;
	
	public final static NettyClient INSTANCE=new NettyClient();
	
	private final ConnectionListener connectionListener=new ConnectionListener();
	
	private ExecutorService executorService=Executors.newSingleThreadExecutor();
	
	private ServerConfig serverConfig=null;
	
	private NettyClient() { 
		
	}
	
	
	public NettyClient setConfig(ServerConfig serverConfig){
		this.serverConfig=serverConfig;
		return this;
	}
	
	
	public void start(){
		executorService.submit(()->{
			try {
				 loopGroup=new NioEventLoopGroup();
				 Bootstrap bootstrap = new Bootstrap();
			     bootstrap.group(loopGroup)
			     .channel(NioSocketChannel.class)
			     .option(ChannelOption.SO_KEEPALIVE,true)
			     .handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new IdleStateHandler(30,0,0));
		                pipeline.addLast(new IdleStateTrigger());
		                //设计包的格式 1字节固定包头  1字节功能码  1字节（判断是否包含topic字段） 4字节固定长度字段   12字节固定topic（非必须）  剩余字节数据
		     	        pipeline.addLast(new LengthFieldBasedFrameDecoder(2048, 3, 4, 0, 0));
		                pipeline.addLast(new MessageToPoDecoder());
		                pipeline.addLast(new MessageProcessHandler());
		                pipeline.addLast(new MessageEncoder());
					}
				});
			     //192.168.1.3
			    ChannelFuture channel = bootstrap.connect(serverConfig.getHost(), Integer.parseInt(serverConfig.getPort()));
			    channel.addListener(connectionListener);
				channel.channel().closeFuture().sync();	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
	}
	
	public void stop(){
			if(loopGroup!=null){
				loopGroup.shutdownGracefully();
				executorService.shutdown();
			}
	}

}
