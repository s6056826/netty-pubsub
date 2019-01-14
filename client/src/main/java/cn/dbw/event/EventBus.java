package cn.dbw.event;

import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import cn.dbw.api.PubAndSubClient.AutuListener;
import cn.dbw.api.PubAndSubClient.SubscribListener;
import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;
import cn.dbw.po.LastLoginRecord;
import cn.dbw.util.MD5Util;
import cn.dbw.util.StringTools;
import jodd.util.StringUtil;

public class EventBus {
	
	
	private static Map<String,SubscribListener> subscribListenerTable=new HashMap<>(16);
	
	private static AutuListener autuListener;
	
	
	public static Handler handler=new Handler() {

		@Override
		public void onMsg(Message obj, FuncodeEnum funcodeEnum) {
			switch (funcodeEnum) {
			case NOTICE_AUTH_OK:
				//认证成功保存登陆凭证
				//LastLoginRecord.INSTANCE().setToken(obj);
				if(autuListener!=null)
					autuListener.authOk(obj);
				break;
		    case NOTICE_AUTH_FAIL:
		    	autuListener.authFail(obj);
		    	break;  	
		    case NOTICE_SUBSCRIBE_OK:
		    	selectSubscribListener(obj).subOk(obj);
		    	break;
		    case NOTICE_UNSUBSCRIBE_OK:
		    	selectSubscribListener(obj).unSubOk(obj);
		    	break;
		    case MESSAGE_SEND:
		    	selectSubscribListener(obj).msgActive(obj);
		    	break;
			default:
				break;
			}
		}

		

	};

	
	private static SubscribListener selectSubscribListener(Message obj){
		 String topic = new String(obj.getTopic());
		 SubscribListener subscribListener = subscribListenerTable.get(topic);
		 return subscribListener;
	}
	
	
	public static void setAutuListener(AutuListener autuListener) {
		EventBus.autuListener = autuListener;
	}
	
	public static void setSubscribListener(String topic,SubscribListener subscribListener) {
		if(StringUtil.isEmpty(topic)&&subscribListener==null){
			throw new RuntimeException("topic and subscribListener not null");
		}
		EventBus.subscribListenerTable.put(topic, subscribListener);
	}
	
	
	
	
}
