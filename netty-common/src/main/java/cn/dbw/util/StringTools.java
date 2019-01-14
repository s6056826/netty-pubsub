package cn.dbw.util;

import java.io.UnsupportedEncodingException;

public class StringTools {
	
	
	public static String toStrUTF(byte[] data){
		if(null!=data){
			try {
				return new String(data,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
