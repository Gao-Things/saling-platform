package com.usyd.capstone;

import com.usyd.capstone.common.compents.NotificationServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling
@SpringBootApplication
@EnableAsync
public class CapstoneApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =  SpringApplication.run(CapstoneApplication.class, args);
        NotificationServer.setApplicationContext(configurableApplicationContext);
    }

}
