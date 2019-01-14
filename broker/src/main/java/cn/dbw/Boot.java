package cn.dbw;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.apache.log4j.PropertyConfigurator;

import cn.dbw.server.BrokeServer;
import cn.dbw.server.HttpServer;

public class Boot {
	
	private final ExecutorService executorService=Executors.newFixedThreadPool(2);
	
	
	public void start(){
		executorService.submit(()->{
			BrokeServer.INSTANCE.start();
		});
		executorService.submit(()->{
			HttpServer.INSTANCE.start();
		});
	}

	public static void main(String[] args) {
		new Boot().start();
	}
}
