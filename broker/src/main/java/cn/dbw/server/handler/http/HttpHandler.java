package cn.dbw.server.handler.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.dbw.container.TopicManager;
import cn.dbw.container.TopicManager.TopicHolderType;
import cn.dbw.util.MD5Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
		
	private final static TopicManager topicManager=new TopicManager(TopicHolderType.MemoryTopicHolder);

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
			// TODO Auto-generated method stub
			try {
				ByteBuf content = msg.content();
				byte[] bts = new byte[content.readableBytes()];
				content.readBytes(bts);
				String result = null;
				if(msg.getMethod() == HttpMethod.GET) {
					String url = msg.getUri().toString();
					result =JSON.toJSONString(UrlUtil.parse(url).params);
					doGet(result);
					
				}else if(msg.getMethod() == HttpMethod.POST) {
					//result = "post method and paramters is "+ new String(bts);
					doPost(new String(bts,"utf-8"));
				}
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
				response.headers().set("content-Type","text/html;charset=UTF-8");
				StringBuilder sb = new StringBuilder();
				sb.append("OK");
				ByteBuf responseBuf = Unpooled.copiedBuffer(sb,CharsetUtil.UTF_8);
				response.content().writeBytes(responseBuf);
				responseBuf.release();
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
				}catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		
		private void doGet(String jsonStr){
			doPost(jsonStr);
		}
		
		
		private void doPost(String jsonStr){
			JSONObject res = JSON.parseObject(jsonStr);
			String topic = res.getString("topic");
			String data=res.getString("data");
			System.out.println("topic->"+topic+" data->"+data);
			topic=MD5Util.getPwd(topic).substring(0, 12);//加密topic
			//发布消息
			try {
				topicManager.publish(topic.getBytes(),data.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
}

class UrlUtil {

	public static class UrlEntity {
		/**
		 * 基础url
		 */
		public String baseUrl;
		/**
		 * url参数
		 */
		public Map<String, String> params;
	}

	/**
	 * 解析url
	 *
	 * @param url
	 * @return
	 */
	public static UrlEntity parse(String url) {
		UrlEntity entity = new UrlEntity();
		if (url == null) {
			return entity;
		}
		url = url.trim();
		if (url.equals("")) {
			return entity;
		}
		String[] urlParts = url.split("\\?");
		entity.baseUrl = urlParts[0];
		//没有参数
		if (urlParts.length == 1) {
			return entity;
		}
		//有参数
		String[] params = urlParts[1].split("&");
		entity.params = new HashMap<>();
		for (String param : params) {
			String[] keyValue = param.split("=");
			try {
				entity.params.put(keyValue[0],URLDecoder.decode(keyValue[1],"utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return entity;
	}
}
