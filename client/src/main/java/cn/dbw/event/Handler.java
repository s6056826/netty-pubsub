package cn.dbw.event;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;

public abstract class Handler {
	
	
   public void sendMsg(Message obj,FuncodeEnum funcodeEnum){
	   onMsg(obj, funcodeEnum);
   }
   
   
   public abstract void onMsg(Message obj,FuncodeEnum funcodeEnum);

}
