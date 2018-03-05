package com.codecool.audioStream;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StreamerApp {

    public void start(){
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext("com.codecool.audioStream");
    }
}
