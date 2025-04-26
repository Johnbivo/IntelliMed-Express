package com.inteliMedExpress.classes.Medications;

import com.inteliMedExpress.classes.UIHelper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MedicationDialog {

    public static Medication showAddMedicationDialog(Stage parentStage) {
        return showMedicationDialog(parentStage, null, "Add New Medication");
    }

    public static Medication showUpdateMedicationDialog(Stage parentStage, Medication existingMedication) {
        return showMedicationDialog(parentStage, existingMedication, "Update Medication");
    }

    public static boolean showDeleteConfirmationDialog(Stage parentStage, Medication medication) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(parentStage);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Medication Record");

        alert.setContentText("Are you sure you want to delete medication: " +
                medication.getMedicationName() + " (ID: " + medication.getMedicationId() + ")?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                MedicationDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());
        dialogPane.getStyleClass().addAll("alert", "confirmation");

        ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.setOnShown(event -> {
            Button yesButton = (Button) alert.getDialogPane().lookupButton(buttonYes);
            if (yesButton != null) yesButton.getStyleClass().add("yes-button");
        });

        return alert.showAndWait().orElse(buttonNo) == buttonYes;
    }

    private static Medication showMedicationDialog(Stage parentStage, Medication medication, String title) {
        Dialog<Medication> dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(parentStage);
        dialog.setTitle(title);
        dialog.setHeaderText(medication == null ? "Enter Medication Details" : "Update Medication Details");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                MedicationDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setOnShown(event -> {
            Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
            if (saveButton != null) saveButton.getStyleClass().add("default-button");
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.getStyleClass().add("grid-pane");

        // Form fields
        TextField nameField = new TextField();
        nameField.setPromptText("Medication Name");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Short description");

        TextField priceField = new TextField();
        priceField.setPromptText("e.g. 10.50");

        TextField stockField = new TextField();
        stockField.setPromptText("e.g. 100");

        DatePicker expirationDatePicker = new DatePicker();

        nameField.setPrefWidth(250);
        descriptionField.setPrefWidth(250);
        priceField.setPrefWidth(250);
        stockField.setPrefWidth(250);
        expirationDatePicker.setPrefWidth(250);

        // Populate if updating
        if (medication != null) {
            nameField.setText(medication.getName());
            descriptionField.setText(medication.getDescription());
            priceField.setText(String.valueOf(medication.getPrice()));
            stockField.setText(String.valueOf(medication.getStockQuantity()));
            expirationDatePicker.setValue(medication.getExpirationDate());
        }

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);

        grid.add(new Label("Price (€):"), 0, 2);
        grid.add(priceField, 1, 2);

        grid.add(new Label("Stock Quantity:"), 0, 3);
        grid.add(stockField, 1, 3);

        grid.add(new Label("Expiration Date:"), 0, 4);
        grid.add(expirationDatePicker, 1, 4);

        dialogPane.setContent(grid);
        dialogPane.setPrefWidth(450);
        dialogPane.setPrefHeight(400);
        nameField.requestFocus();

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = nameField.getText().trim();
                    String description = descriptionField.getText().trim();
                    double price = Double.parseDouble(priceField.getText().trim());
                    int stock = Integer.parseInt(stockField.getText().trim());
                    LocalDate expiration = expirationDatePicker.getValue();

                    if (name.isEmpty() || description.isEmpty() || expiration == null) {
                        UIHelper.showAlert("Validation Error", "All fields are required.");
                        return null;
                    }

                    if (medication != null) {
                        medication.setName(name);
                        medication.setDescription(description);
                        medication.setPrice(price);
                        medication.setStockQuantity(stock);
                        medication.setExpirationDate(expiration);
                        return medication;
                    } else {
                        return new Medication(name, description, price, stock, expiration);
                    }
                } catch (Exception e) {
                    UIHelper.showAlert("Input Error", "Please check your inputs: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }
}
