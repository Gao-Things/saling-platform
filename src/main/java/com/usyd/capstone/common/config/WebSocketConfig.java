package com.usyd.capstone.common.config;

import com.usyd.capstone.common.compents.MyWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
//@EnableWebSocketMessageBroker
public class WebSocketConfig /*extends AbstractSecurityWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer*/ {

//    @Autowired
//    private MyWebSocketHandler myWebSocketHandler;
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(myWebSocketHandler, "/myHandler")
//                .addInterceptors(new HttpSessionHandshakeInterceptor());
//    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .simpDestMatchers("/chat").permitAll()
//                .anyMessage().authenticated();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/secured/history");
//        config.setApplicationDestinationPrefixes("/spring-security-mvc-socket");
//    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/chat")
//                .withSockJS();
//    }

}
