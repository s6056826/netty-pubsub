package cn.dbw.config;

/**
 * 功能码
 * @author dbw
 *
 */
public enum FuncodeEnum {
	
	HEART_BEAT("心跳",(byte)1),
	AUTH_USER("用户认证",(byte)2),
	MESSAGE_SEND("消息发送",(byte)3),
	MESSAGE_BROAD("消息广播",(byte)11),
	ERROR_INFO("错误信息",(byte)4),
	TOPIC_SUBSCRIBE("消息订阅",(byte)5),
	TOPIC_UNSUBSCRIBE("取消订阅",(byte)6),
	NOTICE_SUBSCRIBE_OK("订阅成功通知",(byte)7),
	NOTICE_UNSUBSCRIBE_OK("取消订阅通知",(byte)8),
	NOTICE_AUTH_OK("认证成功通知",(byte)9),
	NOTICE_AUTH_FAIL("认证失败通知",(byte)10),
	;
	
	private String desc;//功能描述
	private Byte code;  //功能码

	
	private FuncodeEnum(String desc, Byte code) {
		this.desc = desc;
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Byte getCode() {
		return code;
	}
	public void setCode(Byte code) {
		this.code = code;
	}
	
	 /**
     * 帮助类 跟据code字段获取eum实例
     * @param type
     * @return
     */
    public static FuncodeEnum getEumInstanceByType(byte code){
        for(FuncodeEnum funcodeEnum:FuncodeEnum.values()){
            if(funcodeEnum.getCode()==code){
                return funcodeEnum;
            }
        }
        return  null;
    }
	
	
	

}
