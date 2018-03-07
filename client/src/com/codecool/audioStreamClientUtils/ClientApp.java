package com.codecool.audioStreamClientUtils;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.codecool.audioStreamClientUtils")
public class ClientApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext("com.codecool.audioStreamClientUtils");
        context.register(ClientApp.class);
        context.refresh();

        UdpClient udpClient = context.getBean(UdpClient.class);

    }
}
