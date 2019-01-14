package cn.dbw.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import jodd.props.Props;
import jodd.props.PropsEntries;

public class PropertyUtil {
   
	public static HashMap load() throws IOException{
	    	Props props = new Props();
	    	HashMap<String, String> hashMap = new HashMap<>();
	    	InputStream in=PropertyUtil.class.getClassLoader().getResourceAsStream("config.properties");
	    	props.load(in);
	    	PropsEntries entries = props.entries();
	    	entries.forEach((r)->{
	    		hashMap.put(r.getKey(), r.getValue());
	    	});
	    	return hashMap;
           
	}
}
