package cn.dbw.po;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;

public class MessageWrapper {
	
	private byte fixHead=(byte) 0xA8;
	
	private FuncodeEnum funcodeEnum=null;
	
	private Object message;
	

	public MessageWrapper( FuncodeEnum funcodeEnum, Object message) {
		this.funcodeEnum = funcodeEnum;
		this.message = message;
	}

	public byte getFixHead() {
		return fixHead;
	}


	public FuncodeEnum getFuncodeEnum() {
		return funcodeEnum;
	}

	public void setFuncodeEnum(FuncodeEnum funcodeEnum) {
		this.funcodeEnum = funcodeEnum;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}
	
	
	

}
