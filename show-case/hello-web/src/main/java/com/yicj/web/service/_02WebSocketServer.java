package com.yicj.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 服务端
 * 此项目使用WebSocketConfigurer进行Spring WebSocket配置，
 * _02WebSocketServer继承TextWebSocketHandler类，
 * 所以没有OnOpen这些函数，但看函数名你也能对应上是那个函数，
 * 如果想要使用OnOpen这些，请参考JSR-356的WebSocket开发规则，避免产生配置冲突问题
 */
@Slf4j
@Component
public class _02WebSocketServer extends TextWebSocketHandler {
    // 在线客户端会话集合
    private static final ConcurrentHashMap<String, WebSocketSession> clients = new ConcurrentHashMap<>();
    // 话题发布对象
    private static _02TestPublisher _02Publisher;

    // 注入_02TestPublisher这个类的Bean，这里没有办法直接使用@Autowired注入
    @Autowired
    public void set_02Publisher(_02TestPublisher _02Publisher) {
        _02WebSocketServer._02Publisher = _02Publisher;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        clients.put(session.getId(), session);
        session.sendMessage(new TextMessage("欢迎连接 WebSocket 服务！"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {



        // 将消息发送到 Redis 进行广播
        if (_02Publisher != null) {
            _02Publisher.publish("_02TestChannel", message.getPayload());
        } else {
            System.out.println("Publisher bean is not available");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        clients.remove(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
         System.out.println("WebSocket 发生错误：" + exception);
    }

    // 广播消息
    public void broadcast(String message) {
        for (WebSocketSession session : clients.values()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
