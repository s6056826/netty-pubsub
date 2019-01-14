package cn.dbw.server;


import org.apache.log4j.Logger;

import cn.dbw.config.PropertyConfigFactory;
import cn.dbw.config.ServerConfig;
import cn.dbw.server.handler.ChanelInitializerHandler;
import cn.dbw.zk.ZkRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import jodd.util.StringUtil;

public class BrokeServer implements Server {
	
	private NioEventLoopGroup worker=new NioEventLoopGroup();
	private NioEventLoopGroup boss=new NioEventLoopGroup();
	
	private final Logger LOGGER=Logger.getLogger(BrokeServer.class);
	
	public  final static BrokeServer INSTANCE=new BrokeServer();
	
	private PropertyConfigFactory config=new PropertyConfigFactory();
	
	
	private  BrokeServer() {
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void start() {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
		.childHandler(new ChanelInitializerHandler())
		.childOption(ChannelOption.SO_KEEPALIVE, true);
		try {
			ChannelFuture channelFuture;
			if(!StringUtil.isEmpty(config.getConfig().getHost())){
				channelFuture=serverBootstrap.bind(config.getConfig().getHost(),Integer.parseInt(config.getConfig().getPort())).sync();
			}else{
				channelFuture = serverBootstrap.bind(Integer.parseInt(config.getConfig().getPort())).sync();
			} 
			channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
				@Override
				public void operationComplete(Future<? super Void> future) throws Exception {   
					if(future.isSuccess()||future.isDone()){
						LOGGER.info("【broker已启动】ip->"+config.getConfig().getHost()+" port->"+config.getConfig().getPort());
					}else{
						LOGGER.info("【broker】启动失败");
					}
				}
			});
			//判断是否开启集群
			if(config.getConfig().getEnableCluster()){
				//若开启集群模式，则启动注册zookeeper
				ServerConfig serverConfig = config.getConfig();
				ZkRegister.getInstance().register(serverConfig.getZkRootPath()+"/broker_",serverConfig.getHost()+":"+serverConfig.getPort());
			}
			 channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			stop();
		}
	}

	@Override
	public void stop() {
		if(worker!=null){
			worker.shutdownGracefully();
		}
		if(boss!=null){
			boss.shutdownGracefully();
		}
		
		//关闭zk客户端连接
		ZkRegister.getInstance().close();
		
		LOGGER.info("【broker】关闭成功");
	}
	
	

}
