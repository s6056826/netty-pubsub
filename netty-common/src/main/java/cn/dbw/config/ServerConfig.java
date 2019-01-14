package cn.dbw.config;

import java.util.Map;

public class ServerConfig {
	
	private String host;
	
	private String port;
	
	private String serverName;
	
	private Integer retryCount;
	
	private Integer connectTimeout;
	
	private Integer pingTimeout;
	
	//是否开启集群模式
	private Boolean enableCluster;
	
	private String zkServers;
	
	private Integer zkSessionTimeout;
	
	private String zkRootPath;
	
	
	
	
	private Map<String,String> verifiedAccount;
	
	
	

	public ServerConfig(String host, String port) {
		this.host = host;
		this.port = port;
	}
    
	public ServerConfig() {
		// TODO Auto-generated constructor stub
	}
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Map<String, String> getVerifiedAccount() {
		return verifiedAccount;
	}

	public void setVerifiedAccount(Map<String, String> verifiedAccount) {
		this.verifiedAccount = verifiedAccount;
	}

	public Integer getPingTimeout() {
		return pingTimeout;
	}

	public void setPingTimeout(Integer pingTimeout) {
		this.pingTimeout = pingTimeout;
	}

	public String getZkServers() {
		return zkServers;
	}

	public void setZkServers(String zkServers) {
		this.zkServers = zkServers;
	}

	public Integer getZkSessionTimeout() {
		return zkSessionTimeout;
	}

	public void setZkSessionTimeout(Integer zkSessionTimeout) {
		this.zkSessionTimeout = zkSessionTimeout;
	}

	public String getZkRootPath() {
		return zkRootPath;
	}

	public void setZkRootPath(String zkRootPath) {
		this.zkRootPath = zkRootPath;
	}

	public Boolean getEnableCluster() {
		return enableCluster;
	}

	public void setEnableCluster(Boolean enableCluster) {
		this.enableCluster = enableCluster;
	}
	
	
	
	

}
