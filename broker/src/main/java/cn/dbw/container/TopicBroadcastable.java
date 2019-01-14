package cn.dbw.container;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.util.MD5Util;

public interface TopicBroadcastable {
	
	final byte [] BROAD_TOPIC=MD5Util.getPwd(FuncodeEnum.MESSAGE_BROAD.name().concat("$")).substring(0, 12).getBytes();
     
	void brodcast(byte[] data);
}
