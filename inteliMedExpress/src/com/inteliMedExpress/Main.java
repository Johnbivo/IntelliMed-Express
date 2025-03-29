package com.inteliMedExpress;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main application class for InteliMedExpress
 * This class initializes and launches the JavaFX application
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load the main application FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inteliMedExpress/resources/fxml/MainView.fxml"));
            Parent root = loader.load();

            // Set up the primary scene
            Scene scene = new Scene(root);


            // Configure the primary stage
            primaryStage.setTitle("InteliMedExpress - Healthcare Management System");
            primaryStage.setScene(scene);

            // Set application icon if available


            // Set minimum window size for better UX
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);

            // Show the application window
            primaryStage.show();

            System.out.println("InteliMedExpress application started successfully");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting InteliMedExpress application: " + e.getMessage());
        }
    }

    /**
     * Application initialization method called before start()
     */
    @Override
    public void init() throws Exception {
        // Initialize application resources, connections, etc.
        System.out.println("Initializing InteliMedExpress...");
        // TODO: Add initialization code, database connections, config loading, etc.
        super.init();
    }

    /**
     * Application cleanup method called on exit
     */
    @Override
    public void stop() throws Exception {
        // Clean up resources, close connections, etc.
        System.out.println("Shutting down InteliMedExpress...");
        // TODO: Add cleanup code, close database connections, save config, etc.
        super.stop();
    }

    /**
     * Main method to launch the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("Fatal error in InteliMedExpress application");
            e.printStackTrace();
        }
    }
}