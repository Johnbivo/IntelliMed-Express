package com.inteliMedExpress.classes;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class UIHelper {


    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title.toLowerCase().contains("error")) {
            alert = new Alert(Alert.AlertType.ERROR);
        } else if (title.toLowerCase().contains("warning")) {
            alert = new Alert(Alert.AlertType.WARNING);
        }

        alert.setTitle(title);
        alert.setHeaderText(title); // Keep the header but style it minimally
        alert.setContentText(message);

        // Get the DialogPane
        DialogPane dialogPane = alert.getDialogPane();

        // Apply minimal styling to the dialog
        dialogPane.setStyle(
                "-fx-background-color: #f0f8ff;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 6, 0, 0, 3);"
        );

        // Style the header - make it more subtle
        Label headerLabel = (Label) dialogPane.lookup(".header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle(
                    "-fx-text-fill: #1e67a8;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 16px;"
            );
        }

        // Make header panel more subtle
        Region headerPanel = (Region) dialogPane.lookup(".header-panel");
        if (headerPanel != null) {
            String headerColor = "#f0f8ff"; // Same as background for minimalism

            // Just a slight border at bottom to separate
            headerPanel.setStyle(
                    "-fx-background-color: " + headerColor + ";" +
                            "-fx-border-color: #a9d6e5;" +
                            "-fx-border-width: 0 0 1 0;" + // Only bottom border
                            "-fx-background-radius: 8 8 0 0;"
            );
        }

        // Style the content label
        Label contentLabel = (Label) dialogPane.lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setStyle(
                    "-fx-text-fill: #2c7bb6;" +
                            "-fx-font-size: 14px;" +
                            "-fx-padding: 10 10 10 10;"
            );
        }

        // Style the buttons
        for (ButtonType buttonType : alert.getDialogPane().getButtonTypes()) {
            Button button = (Button) dialogPane.lookupButton(buttonType);

            // Minimalist button style
            String baseStyle =
                    "-fx-background-color: white;" +
                            "-fx-text-fill: #1e67a8;" +
                            "-fx-border-color: #a9d6e5;" +
                            "-fx-border-width: 1px;" +
                            "-fx-border-radius: 4;" +
                            "-fx-background-radius: 4;" +
                            "-fx-padding: 6 14 6 14;";

            button.setStyle(baseStyle);

            // Hover effect
            button.setOnMouseEntered(e ->
                    button.setStyle(
                            "-fx-background-color: #f5faff;" +
                                    "-fx-text-fill: #0953a0;" +
                                    "-fx-border-color: #2986cc;" +
                                    "-fx-border-width: 1px;" +
                                    "-fx-border-radius: 4;" +
                                    "-fx-background-radius: 4;" +
                                    "-fx-padding: 6 14 6 14;" +
                                    "-fx-cursor: hand;"
                    )
            );

            // Reset on exit
            button.setOnMouseExited(e -> button.setStyle(baseStyle));
        }
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Load your application's icon - update the path to match your icon location
        Image appIcon = new Image(UIHelper.class.getResourceAsStream("/resources/com/inteliMedExpress/images/logo.png"));
        stage.getIcons().add(appIcon);

        alert.showAndWait();
    }

    public static void styleNavigationButton(Button button, boolean selected) {
        if (selected) {
            button.setStyle(
                    "-fx-background-color: #1e67a8; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 4; " +
                            "-fx-font-weight: bold;"
            );
        } else {
            button.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-text-fill: #1e67a8; " +
                            "-fx-font-weight: normal;"
            );
        }
    }
}
