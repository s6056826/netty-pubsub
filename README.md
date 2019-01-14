# netty-pubsub

#### 介绍
百万级高性能分布式发布订阅系统，基于nio,netty开发，无锁并发，充分发挥cpu极限,
该项目已自带心跳检测功能，断线自动重连，非常稳定。
注意该项目软件编码为GBK，导入的时候一定要看清项目编码格式。

#### 软件架构
软件架构说明

netty+zookeeper分布式发布订阅系统


#### 安装教程

1. xxxx
2. xxxx
3. xxxx

#### 使用说明

1. broker启动  运行boot类
2. client 有订阅者和发布者，具体代码可以在test中有测试用例
3. 现阶段分布式还没发布，需要测试的可以有单机版,切换单机版，需要在resouces下的config.properties的enableCluster=false


#### 代码用例

1.broker启动src/main/java 下的 Boot.java

2.client测试
```
  //创建客户端
  NettyPubAndSubClient client = NettyPubAndSubClient.getInstance();
  //客户端连接
	client.connect("127.0.0.1",9999);
  //传入订阅主题，和监听器
  client.subscribe("mm", new SubscribListener() {
			@Override
			public void subOk(Message message) {
				System.out.println("订阅成功");
			}
			@Override
			public void msgActive(Message message) {
				try {
					System.out.println("收到消息mm："+new String(message.getData(),"utf-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
   //接收广播
   client.acceptBraodCast(new SubscribListener() {
			@Override
			public void msgActive(Message message) {
                   try {
					System.out.println("接收广播消息："+new String(message.getData(),"utf-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    //发布消息
    client.publish("mm", "哈哈");
    //发送广播
    client.broadcast("hello 广播");
    //优雅关闭
    client.shutdown();
```		
    

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request
