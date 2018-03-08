package com.codecool.audioStreamClientUtils;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Configuration
@ComponentScan("com.codecool.audioStreamClientUtils")
public class ClientApp extends Application{

    static PlayerController controller;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext("com.codecool.audioStreamClientUtils");
        context.scan("com.codecool.audioStreamClientClientUtils");

        controller = context.getBean(PlayerController.class);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        controller.getView().setScene(primaryStage);
        controller.start();
    }
}


