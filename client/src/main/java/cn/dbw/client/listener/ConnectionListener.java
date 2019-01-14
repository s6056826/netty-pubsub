package cn.dbw.client.listener;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import cn.dbw.api.PubAndSubClient.AutuListener;
import cn.dbw.client.ChannelHolder;
import cn.dbw.client.NettyClient;
import cn.dbw.client.SubRecorder;
import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;
import cn.dbw.po.LastLoginRecord;
import cn.dbw.util.DateUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import jodd.util.StringUtil;

public class ConnectionListener implements ChannelFutureListener {
	
	    private final  NettyClient CLIENT=NettyClient.INSTANCE;
        
	    private final Logger LOGGER=Logger.getLogger(ChannelFutureListener.class);
	
	    private  static   AtomicInteger retryCount=new AtomicInteger(0);

	    private final int TRY_LIMITE=15; //重连次数最大限制

	    private int dalayTime=1; //定时延期时间

		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
	           if(!future.isSuccess()){
	               EventLoop eventExecutors = future.channel().eventLoop();
	               future.channel().close();
	               if(!eventExecutors.isShuttingDown()){
	               eventExecutors.schedule(()->{
	                   if(retryCount.get()<=TRY_LIMITE) {      
	                	   LOGGER.error("【客户端状态】STATUS=failed,TIME="+ DateUtils.getCurrentDateTime()+",msg=正在尝试重连,retrys="+retryCount.getAndIncrement());
	                	   NettyClient.INSTANCE.start();
	                   }else{
	                	   NettyClient.INSTANCE.stop();
	                       LOGGER.error("【重连警告】已超过最大重连次数，程序关闭");
	                   }
	               },dalayTime,TimeUnit.SECONDS);
	               dalayTime=dalayTime<<1;//重连次数越多，延迟时间越长
	               }
	           }else{
	        	   LOGGER.info("【客户端状态】STATUS=ACTIVE,TIME="+ DateUtils.getCurrentDateTime());
	        	   ChannelHolder.setChannel(future.channel());
//	        	   //判断上次是否登陆
//	        	   if(!StringUtil.isEmpty(LastLoginRecord.INSTANCE().getLastToken())){
//	        		   //向broker发送认证凭证
//	        		   System.out.println("发送登陆凭证");
//	        		   future.channel().writeAndFlush(new Message(FuncodeEnum.AUTH_USER,(byte)0 , null, LastLoginRecord.INSTANCE().getLastToken().getBytes().length, LastLoginRecord.INSTANCE().getLastToken().getBytes()));
//	        	   }
	               //若重连成功恢复重连间隔
	        	   SubRecorder.recover();
	               dalayTime=1;
	               retryCount.set(0);
	           }
			
		}
	    
	    
	
}
