package com.waygo.config.infrastructure.realtime;


import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIoConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public SocketIOServer socketIOServer(
            @Value("${waygo.socketio.host:0.0.0.0}") String host,
            @Value("${waygo.socketio.port:8081}") int port
    ) {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setOrigin("*");
        return new SocketIOServer(config);
    }
}
