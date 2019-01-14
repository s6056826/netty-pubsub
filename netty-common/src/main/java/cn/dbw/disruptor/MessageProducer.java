package cn.dbw.disruptor;

import com.lmax.disruptor.RingBuffer;

import cn.dbw.dto.Message;
import cn.dbw.po.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

public class MessageProducer {
	
	
	private String producerId;
	
	private RingBuffer<MessageWrapper> ringBuffer;

	public MessageProducer(String producerId, RingBuffer<MessageWrapper> ringBuffer) {
		this.producerId = producerId;
		this.ringBuffer = ringBuffer;
	}
	
	public void onData(ChannelHandlerContext ctx,Message message){
		//获取下一个可用的序列号
		long sequence = ringBuffer.next();
		try {
			//获取待填充数据
			MessageWrapper messageWrapper = ringBuffer.get(sequence);
			messageWrapper.setCtx(ctx);
			messageWrapper.setMessage(message);
		} finally {
			//发布数据
			ringBuffer.publish(sequence);
		}
	}

}
