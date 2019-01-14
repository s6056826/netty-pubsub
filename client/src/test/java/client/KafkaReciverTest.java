package client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class KafkaReciverTest {
	
	public static void main(String[] args) {
		    Properties properties = new Properties();
	        properties.put("bootstrap.servers", "127.0.0.1:9092");
	        properties.put("group.id", "group-3");
	        properties.put("enable.auto.commit", "true");
	        properties.put("auto.commit.interval.ms", "1000");
	        properties.put("auto.offset.reset", "earliest");
	        properties.put("session.timeout.ms", "30000");
	        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	        //properties.put("value.deserializer", "client.KafkaDeSerialization");
	        KafkaConsumer<String, TestMsg> kafkaConsumer = new KafkaConsumer<>(properties);
	        kafkaConsumer.subscribe(Arrays.asList("HelloWorld"));
	        while (true) {
	            ConsumerRecords<String, TestMsg> records = kafkaConsumer.poll(100);
	            if(records.count()>0)
	            for (ConsumerRecord<String, TestMsg> record : records) {
	                System.out.printf("offset = %d, value = %s", record.offset(), record.value());
	                System.out.println();
	            }
	        }
	}

}
