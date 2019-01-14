package cn.dbw.api;

import cn.dbw.api.PubAndSubClient.AutuListener;

public interface Authable {

    //客户端认证
	void auth(String username,String password,AutuListener autuListener);
}
