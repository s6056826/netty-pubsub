package cn.dbw.exception;

public class IllegalDataHeaderException extends RuntimeException {
	
	public IllegalDataHeaderException(byte msg) {
		super("非法的数据头部->"+msg);
	}

}
