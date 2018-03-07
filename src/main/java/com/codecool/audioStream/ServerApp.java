package com.codecool.audioStream;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServerApp {

    public void start() {

        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext("com.codecool.audioStream");

    }
}
