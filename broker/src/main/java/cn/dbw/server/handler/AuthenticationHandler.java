package cn.dbw.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.dbw.config.ConfigFactory;
import cn.dbw.config.FuncodeEnum;
import cn.dbw.config.PropertyConfigFactory;
import cn.dbw.config.ServerConfig;
import cn.dbw.dto.Message;
import cn.dbw.po.LastLoginRecord;
import cn.dbw.po.MessageWrapper;
import cn.dbw.util.MD5Util;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import jodd.json.JsonObject;
import jodd.util.StringUtil;

/**
 * 授权认证
 * @author dbw
 *
 */
public class AuthenticationHandler extends SimpleChannelInboundHandler<Message> {
	
	PropertyConfigFactory configFac=new PropertyConfigFactory();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		//读取用户信息
		byte[] data = msg.getData();
		String res = new String(data,"utf-8");
		//判断是否第一次登陆
		if(res.startsWith("{")){
			JSONObject jsonObject = JSON.parseObject(res);
			System.out.println("收到消息"+jsonObject.toString());
			if(null!=jsonObject){
				String username = jsonObject.getString("username");
				String password=jsonObject.getString("password");
				ServerConfig config = configFac.getConfig(ConfigFactory.filaPath);
				String value = config.getVerifiedAccount().getOrDefault(username, "");
				if(StringUtil.isEmpty(value)||!value.equals(password)){
					//验证失败，发送失败消息断开连接
					//MessageWrapper messageWrapper = new MessageWrapper(FuncodeEnum.ERROR_INFO, "认证失败");
					ChannelFuture channelFuture = ctx.channel().writeAndFlush(new Message(FuncodeEnum.ERROR_INFO, (byte)0,null , "认证失败".getBytes().length, "认证失败".getBytes()));
					channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
						@Override
						public void operationComplete(Future<? super Void> future) throws Exception {
							// TODO Auto-generated method stub
							if(future.isSuccess()){
								ctx.close();
							}
						}
					});   
				}else{
					//认证成功之后，将账号和密码拼接进行MD5加密生成token，返回给客户端
					String token=MD5Util.getPwd(username+password).substring(0, 16);
					//记录登陆表
					LastLoginRecord.INSTANCE().register(username, password);
					ChannelFuture channelFuture = ctx.channel().writeAndFlush(new Message(FuncodeEnum.NOTICE_AUTH_OK, (byte)0,null , token.getBytes().length, token.getBytes()));
					//认证成功建立管道信息
					ctx.pipeline().addLast( "message-process", new MessageProcessHandler());
				}
				
			}	
		}else if(!res.equals("$FF$")){
			System.out.println("上一次登陆:"+res);
			//如果上次已登陆
			if(LastLoginRecord.INSTANCE().isLogin(res)){
				//认证成功建立管道信息
				ctx.pipeline().addLast( "message-process", new MessageProcessHandler());
			}else{
				//认证失败断开连接
				ctx.close();
			}
		}
	
		ctx.pipeline().remove(this);
	}
	
	



}
