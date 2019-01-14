package cn.dbw.server.handler;

import java.io.IOException;
import java.util.List;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;
import cn.dbw.exception.IllegalDataHeaderException;
import cn.dbw.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ReplayingDecoder;

public class MessageToPoDecoder extends ReplayingDecoder<Void> {
	
	private final byte BODY_HEAD=(byte) 0xA8;
    
	 //协议格式 1字节固定包头  1字节功能码  1字节（判断是否包含topic字段） 4字节固定长度字段   12字节固定topic（非必须）  剩余字节数据
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//System.out.println("服务器收到");
		byte fixHead = in.readByte();//读取固定头部 0xA8
		if(!checkHead(fixHead)){
			System.out.println("服务器收到头部："+fixHead);
			ctx.channel().close();
			throw new IllegalDataHeaderException(fixHead);
		}
		byte funCode = in.readByte();//读取功能码
		byte ishaveTopic = in.readByte(); //读取判断字段
		int bodyLength=in.readInt(); //读取固定数据长度字段 
	    byte[] topic=null;
	    byte[] body=null;
	    if(ishaveTopic==1){
	    	topic=new byte[12];
	    	in.readBytes(topic);
	    	bodyLength=bodyLength-12; //若有topic主题字段 则读取body体字段长度-12
	    }
//	    int readableBytes = in.readableBytes();//获取剩余可读数
//	    System.out.println("服务器剩余可读取："+readableBytes);
	    	body=new byte[bodyLength];
	    	in.readBytes(body);
	    //System.out.println("添加MSG功能码："+FuncodeEnum.getEumInstanceByType(funCode));
	    out.add(new Message(FuncodeEnum.getEumInstanceByType(funCode), ishaveTopic, topic,bodyLength, body));
		
//		
//		FuncodeEnum funcodeEnum = FuncodeEnum.getEumInstanceByType(funCode);
//		switch(funcodeEnum){
//		   case HEART_BEAT:
//			   break;
//		   case AUTH_USER:
//			   ChannelPipeline pipeline = ctx.pipeline();
//			   pipeline.addLast("auth",new AuthenticationHandler());
//		   case MESSAGE_SEND:
//			   int length = in.readInt();
//			   ByteBuf byteBuf = in.readBytes(length);
//			   byte[] data = byteBuf.array();
//			   //反序列化data转为message po
//			   Message message = SerializationUtil.deserialize(data, Message.class);
//			   out.add(message);
//			   break;
//				   
//		}
		
	}
	
	
	/**
	 * 包头校验
	 * @param b
	 * @return
	 */
	private boolean checkHead(byte b){
		if(b==BODY_HEAD)
			return true;
		else 
			return false;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		// TODO Auto-generated method stub
//		super.exceptionCaught(ctx, cause);
        if(cause instanceof IOException)
		    System.err.println(ctx.channel().remoteAddress()+" "+cause.getMessage());
        else
        	cause.printStackTrace();
	}
}
