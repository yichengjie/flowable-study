package com.yicj.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class _02TestSubscriber {

	// 该Subscriber调用_02WebSocketServer的广播服务
    @Autowired
    private _02WebSocketServer webSocketServer;

    public void _02TestHandleMessage(String message) {
        System.out.println("订阅者收到消息: " + message);
        webSocketServer.broadcast("来自 Redis 的广播消息：" + message);
    }
}
