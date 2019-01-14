package client;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import cn.dbw.util.SerializationUtil;

public class KafkaDeSerialization implements Deserializer<TestMsg> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TestMsg deserialize(String topic, byte[] data) {
		// TODO Auto-generated method stub
		if(data!=null&&data.length>0)
		return SerializationUtil.deserialize(data, TestMsg.class);
		else return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
