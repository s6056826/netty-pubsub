package cn.dbw.po;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.dbw.dto.Message;
import cn.dbw.util.MD5Util;

/**
 * 上一次登陆记录表
 * @author dbw
 *
 */
public class LastLoginRecord{
	
	private static Map<String,Date> lastLoginTable;
	private static LastLoginRecord lastLoginRecord=new LastLoginRecord();
	private String lastToken;
	
	private LastLoginRecord(){
		lastLoginTable=new ConcurrentHashMap<>();
	}
	
	public static LastLoginRecord INSTANCE(){
		return lastLoginRecord;
	}
	
	
	public  boolean isLogin(String username,String password ){
		return lastLoginTable.get(MD5Util.getPwd(username+password).substring(0, 16))==null?false:true;
	}
	
	public boolean isLogin(String token){
		return lastLoginTable.get(token)==null?false:true;
	}

	public void register(String username,String password){
		lastLoginTable.put(MD5Util.getPwd(username+password).substring(0, 16),new Date());
	}
	
	public void setToken(Message message){
		 String token=new String(message.getData());
		 this.lastToken=token;
		 lastLoginTable.put(token, new Date());
	}
	
	
	public String getLastToken() {
		return lastToken;
	}
	
}
