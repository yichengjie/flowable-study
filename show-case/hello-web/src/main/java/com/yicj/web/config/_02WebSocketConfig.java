package com.yicj.web.config;

import com.yicj.web.service._02WebSocketServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class _02WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new _02WebSocketServer(), "/test/ws") ;
        // 注册服务和对应话题，这里为_02WebSocketServer注册了话题"/test/ws"
        // 如果你的application.yml中配置了context-path，本项目配置的是book，那么真正注册话题路径为book/test/ws
                //.setAllowedOrigins("http://localhost:63342");   // 配置跨域
    }
}
