package com.codecool.audioStream;


import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sound.sampled.LineUnavailableException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@ComponentScan("com.codecool.audioStream")
public class ServerApp extends Application {

    private static ServerController controller;

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.codecool.audioStream");
        context.scan("com.codecool.audioStream");

        controller = context.getBean(ServerController.class);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        controller.setClientsMap();
        controller.setOutput();
        controller.setLine();
        controller.start();
    }
}
