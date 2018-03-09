package com.codecool.audioStreamClientUtils;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.codecool.audioStreamClientUtils")
public class ClientApp extends Application{

    static PlayerController controller;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext("com.codecool.audioStreamClientUtils");
        context.scan("com.codecool.audioStreamClientUtils");

        controller = context.getBean(PlayerController.class);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        controller.getView().setScene(primaryStage);
        controller.setInput();
        controller.setLine();
        controller.setControls();
        controller.start();
    }
}


