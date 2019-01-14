package cn.dbw.server;


import org.apache.log4j.Logger;

import cn.dbw.config.PropertyConfigFactory;
import cn.dbw.server.handler.http.HttpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class HttpServer implements Server{

	
	private final Logger LOGGER=Logger.getLogger(HttpServer.class);
	
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private PropertyConfigFactory config=new PropertyConfigFactory();
	
	public final static HttpServer INSTANCE=new HttpServer();
	
	private HttpServer() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void start() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
		.option(ChannelOption.SO_BACKLOG, 1024)
		.channel(NioServerSocketChannel.class)
		.childHandler(new ChannelInitializer<SocketChannel>() {
	 
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
				ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));//将多个消息转化成一个
				ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
				ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//解决大码流的问题
				ch.pipeline().addLast("http-server",new HttpHandler());
			}
		});
		
		try {
			ChannelFuture channelFuture = bootstrap.bind(8888).sync();
			channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
				@Override
				public void operationComplete(Future<? super Void> future) throws Exception {
				   
					if(future.isSuccess()||future.isDone()){
						LOGGER.info("【http-server已启动】ip->"+config.getConfig(null).getHost()+" port->"+8888);
					}else{
						LOGGER.info("【http-server】启动失败");
					}
					
				}
			});
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		
	}

	@Override
	public void stop() {
		if(workerGroup!=null){
			workerGroup.shutdownGracefully();
		}
		if(bossGroup!=null){
			bossGroup.shutdownGracefully();
		}
		
		LOGGER.info("【http-server】关闭成功");
		
	}

}
