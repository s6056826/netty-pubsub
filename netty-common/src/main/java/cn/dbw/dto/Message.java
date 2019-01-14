package cn.dbw.dto;

import java.io.Serializable;
import java.util.Arrays;

import cn.dbw.config.FuncodeEnum;

public class Message implements Serializable ,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private String id;
	
	private final byte BODY_HEAD=(byte) 0xA8; //固定头部字段
	
	private FuncodeEnum funCode; //功能码
	
	private byte isHaveTopic; //是否包含12字节定长主题字段  0 不包含  1包含
	
	private byte[] topic;  //固定12字节长度主题
	
	private int bodyLength; //body数据长度
	
	private byte[] data;  //包体数据



	public Message(FuncodeEnum funCode, byte isHaveTopic, byte[] topic,int length, byte[] data) {
		this.funCode = funCode;
		this.isHaveTopic = isHaveTopic;
		this.topic = topic;
		this.data = data;
		this.bodyLength=length;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public FuncodeEnum getFunCode() {
		return funCode;
	}


	public void setFunCode(FuncodeEnum funCode) {
		this.funCode = funCode;
	}


	public byte getIsHaveTopic() {
		return isHaveTopic;
	}


	public void setIsHaveTopic(byte isHaveTopic) {
		this.isHaveTopic = isHaveTopic;
	}


	public byte[] getTopic() {
		return topic;
	}


	public void setTopic(byte[] topic) {
		this.topic = topic;
	}


	public byte[] getData() {
		return data;
	}


	public void setData(byte[] data) {
		this.data = data;
	}


	public byte getBODY_HEAD() {
		return BODY_HEAD;
	}


	public int getBodyLength() {
		return bodyLength;
	}


	public void setBodyLength(int bodyLength) {
		this.bodyLength = bodyLength;
	}


	@Override
	public String toString() {
		return "Message [BODY_HEAD=" + BODY_HEAD + ", funCode=" + funCode + ", isHaveTopic=" + isHaveTopic + ", topic="
				+ Arrays.toString(topic) + ", bodyLength=" + bodyLength + ", data=" + Arrays.toString(data) + "]";
	}
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
    
	
	
	
	
	

}
