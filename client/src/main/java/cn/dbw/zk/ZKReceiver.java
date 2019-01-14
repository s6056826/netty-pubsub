package cn.dbw.zk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.log4j.Logger;

import cn.dbw.config.ZkConstants;
import jodd.util.StringUtil;

/**
 * zk节点数据接收者
 * @author dbw
 *
 */
public class ZKReceiver {
	
	/** zookeeper单节点地址 */
	static final String CONNECT_ADDR_SINGLE="127.0.0.1:2181";
	/** session超时时间 */
	static final int SESSION_OUTTIME = 10000;//ms
	
	private final Logger LOGGER=Logger.getLogger(ZKReceiver.class);
	
	private static ZkClient zkc=null;
	
	
	private void initZkConnection(){
		if(zkc==null)
		zkc=new ZkClient(new ZkConnection(CONNECT_ADDR_SINGLE),SESSION_OUTTIME);
	}
	
	public ZkClient getZkSession(){
		if(zkc==null)
			initZkConnection();
		return zkc;
	}
	
	
	/**
	 * 获取存活的broker
	 * @return
	 */
	public Set<String> getActiveBroker(){
		List<String> childrens = zkc.getChildren(ZkConstants.ZK_ROOT_PATH);
		HashSet<String> hashSet = new HashSet<>();
		childrens.forEach((c)->{
			String data=zkc.readData(ZkConstants.ZK_ROOT_PATH+"/"+c);
			if(!StringUtil.isEmpty(data)){
				hashSet.add(data);
			}
		});
		return hashSet;
	}
	
	
	
	
	
	

}
