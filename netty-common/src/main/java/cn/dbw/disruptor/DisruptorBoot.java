package cn.dbw.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

public class DisruptorBoot {
	
	
	private static class SingletonHolder {
		static final DisruptorBoot instance = new DisruptorBoot();
	}
	
	
	private DisruptorBoot(){
		
	}
	
	
	public static DisruptorBoot getInstance(){
		return SingletonHolder.instance;
	}
	
    /**
     * 
     * @param coreSize 消费者核心数量 建议为cpu和数
     * @param clazz  消费者类型
     */
	public  void init(int coreSize,Class<? extends MessageConsumer> clazz){
		MessageConsumer[] conusmers = new MessageConsumer[coreSize];
		for(int i =0; i < conusmers.length; i++) {
			MessageConsumer messageConsumer;
			try {
				messageConsumer = clazz.newInstance();
				conusmers[i] = messageConsumer;
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
				1024*1024,
				//new YieldingWaitStrategy(),
				new BlockingWaitStrategy(),
				conusmers);
	}


}
