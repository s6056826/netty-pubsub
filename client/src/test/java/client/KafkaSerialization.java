package client;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import cn.dbw.util.SerializationUtil;

public class KafkaSerialization implements Serializer<TestMsg> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] serialize(String topic, TestMsg data) {
		return SerializationUtil.serialize(data);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
 
}
