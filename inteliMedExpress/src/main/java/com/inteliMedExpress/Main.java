package com.inteliMedExpress;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inteliMedExpress/resources/fxml/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("InteliMedExpress - Login");
            primaryStage.setScene(scene);


            Image icon = new Image(getClass().getResource("/com/inteliMedExpress/resources/images/logo.png").toString());
            primaryStage.getIcons().add(icon);
            primaryStage.setMinWidth(1280);
            primaryStage.setMinHeight(720);


            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();


            primaryStage.show();

            System.out.println("InteliMedExpress application started successfully");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting InteliMedExpress application: " + e.getMessage());
        }
    }


    @Override
    public void init() throws Exception {
        System.out.println("Initializing InteliMedExpress...");
        super.init();
    }

    @Override
    public void stop() throws Exception {

        System.out.println("Shutting down InteliMedExpress...");
        super.stop();
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("Fatal error in InteliMedExpress application");
            e.printStackTrace();
        }
    }
}