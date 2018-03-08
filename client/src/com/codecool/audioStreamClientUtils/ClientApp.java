package com.codecool.audioStreamClientUtils;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ComponentScan("com.codecool.audioStreamClientUtils")
public class ClientApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext("com.codecool.audioStreamClientUtils");
        context.scan("com.codecool.audioStreamClientClientUtils");

        PlayerController playerController = context.getBean(PlayerController.class);
        try {
            playerController.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        launch(args);
    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setScene(new Scene());
//        primaryStage.show();
//    }
}
