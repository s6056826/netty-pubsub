package client;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;



public class TestHttp {

	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
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
			ChannelFuture future = bootstrap.bind(8888).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
		private static class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
			

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
						//result = "get method and paramters is "+JSON.toJSONString(UrlUtil.parse(url).params);
						
					}else if(msg.getMethod() == HttpMethod.POST) {
						result = "post method and paramters is "+ new String(bts);
					}
					FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
					response.headers().set("content-Type","text/html;charset=UTF-8");
					StringBuilder sb = new StringBuilder();
					sb.append("<html>")
								.append("<head>")
									.append("<title>netty http server</title>")
								.append("</head>")
								.append("<body>")
									.append(result)
								.append("</body>")
							.append("</html>\r\n");
					ByteBuf responseBuf = Unpooled.copiedBuffer(sb,CharsetUtil.UTF_8);
					response.content().writeBytes(responseBuf);
					responseBuf.release();
					ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
					}catch (Exception e) {
						e.printStackTrace();
					}
			}
			
		}

	}


