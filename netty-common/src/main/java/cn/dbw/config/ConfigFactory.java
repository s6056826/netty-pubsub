package cn.dbw.config;

public interface ConfigFactory {
	
	 String filaPath="config.properties";
	
	 ServerConfig getConfig(String filePath);

}
