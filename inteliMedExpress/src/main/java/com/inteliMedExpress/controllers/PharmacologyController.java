package com.inteliMedExpress.controllers;

import com.inteliMedExpress.classes.UIHelper;
import com.inteliMedExpress.classes.Medications.Medication;
import com.inteliMedExpress.classes.Medications.MedicationDialog;
import com.inteliMedExpress.classes.Medications.MedicationService;
import com.inteliMedExpress.classes.Employees.Doctor;
import com.inteliMedExpress.classes.Employees.DoctorService;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ContentDisplay;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.util.Duration;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PharmacologyController {

    @FXML
    private Label doctorName;

    @FXML
    private ToolBar toolbar;

    @FXML
    private Button all_medications_button;

    @FXML
    private Button low_stock_button;

    @FXML
    private Button expiring_button;

    @FXML
    private Button logout_button;

    @FXML
    private Label pharmacology_title;

    // Medications Table
    @FXML
    private TableView<Medication> medicationTable;
    @FXML
    private TableColumn<Medication, Integer> medicationIDcolumn;
    @FXML
    private TableColumn<Medication, String> medicationNameColumn;
    @FXML
    private TableColumn<Medication, String> descriptionColumn;
    @FXML
    private TableColumn<Medication, Double> priceColumn;
    @FXML
    private TableColumn<Medication, Integer> stockQuantityColumn;
    @FXML
    private TableColumn<Medication, LocalDate> expirationDateColumn;
    @FXML
    private TableColumn<Medication, String> departmentNameColumn;

    // Medication Buttons
    @FXML
    private Button add_medication_button;
    @FXML
    private Button update_stock_button;
    @FXML
    private Button update_medication_button;
    @FXML
    private Button delete_medication_button;
    @FXML
    private Button refresh_button;

    private ObservableList<Medication> medicationsList = FXCollections.observableArrayList();
    private MedicationService medicationService;
    private Doctor currentDoctor;
    private DoctorService doctorService;
    private String loggedInDoctorName;
    private String departmentName = "Pharmacology";

    private static final int LOW_STOCK_THRESHOLD = 200;
    private static final int EXPIRING_SOON_DAYS = 365;

    private ImageView staticRefreshIcon;
    private ImageView animatedRefreshIcon;

    @FXML
    public void initialize() {
        try {
            System.out.println("Pharmacology Controller initializing...");

            medicationService = new MedicationService();


            Medication.setDepartment(departmentName);
            doctorService = new DoctorService(departmentName);

            setupNavigationButtons();
            setupMedicationTable();
            loadAllMedications();
            initializeRefreshButton();
            showAllMedicationsView();

        } catch (Exception e) {
            System.err.println("Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupNavigationButtons() {
        all_medications_button.setOnAction(event -> showAllMedicationsView());
        low_stock_button.setOnAction(event -> showLowStockView());
        expiring_button.setOnAction(event -> showExpiringView());
        all_medications_button.setStyle("-fx-background-color: #e6f2ff; -fx-text-fill: #0052cc; -fx-font-weight: bold;");
    }

    private void showAllMedicationsView() {
        resetButtonStyles();
        all_medications_button.setStyle("-fx-background-color: #e6f2ff; -fx-text-fill: #0052cc; -fx-font-weight: bold;");

        medicationTable.setVisible(true);
        loadAllMedications();
        enableAllButtons(true);
    }

    private void showLowStockView() {
        resetButtonStyles();
        low_stock_button.setStyle("-fx-background-color: #e6f2ff; -fx-text-fill: #0052cc; -fx-font-weight: bold;");

        medicationTable.setVisible(true);
        loadLowStockMedications();

        enableAllButtons(true);
    }

    private void showExpiringView() {
        resetButtonStyles();
        expiring_button.setStyle("-fx-background-color: #e6f2ff; -fx-text-fill: #0052cc; -fx-font-weight: bold;");

        medicationTable.setVisible(true);
        loadExpiringMedications();

        enableAllButtons(true);
    }

    private void resetButtonStyles() {
        all_medications_button.setStyle("");
        low_stock_button.setStyle("");
        expiring_button.setStyle("");
    }

    private void enableAllButtons(boolean enable) {
        add_medication_button.setVisible(enable);
        update_stock_button.setVisible(enable);
        update_medication_button.setVisible(enable);
        delete_medication_button.setVisible(enable);
        refresh_button.setVisible(enable);
    }

    private void setupMedicationTable() {

        medicationTable.setEditable(false);
        TableView.TableViewSelectionModel<Medication> selectionModel = medicationTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        medicationIDcolumn.setCellValueFactory(new PropertyValueFactory<>("medicationId"));
        medicationNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        departmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));

        priceColumn.setCellFactory(column -> {
            return new TableCell<Medication, Double>() {
                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty || price == null) {
                        setText(null);
                    } else {
                        setText(String.format("€%.2f", price));
                    }
                }
            };
        });

        // Date formatter for expiration date column
        expirationDateColumn.setCellFactory(column -> {
            return new TableCell<Medication, LocalDate>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setText(empty || date == null ? null : formatter.format(date));
                }
            };
        });

        // Color code stock quantities based on threshold
        stockQuantityColumn.setCellFactory(column -> {
            return new TableCell<Medication, Integer>() {
                @Override
                protected void updateItem(Integer quantity, boolean empty) {
                    super.updateItem(quantity, empty);

                    if (empty || quantity == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(quantity.toString());

                        // Set red background for low stock
                        if (quantity <= LOW_STOCK_THRESHOLD) {
                            setStyle("-fx-background-color: #ffcccc;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });

        medicationTable.setItems(medicationsList);

        // Setup action buttons
        add_medication_button.setOnAction(event -> handleAddMedication());
        update_stock_button.setOnAction(event -> handleUpdateStock());
        update_medication_button.setOnAction(event -> handleUpdateMedication());
        delete_medication_button.setOnAction(event -> handleDeleteMedication());

        // Add context menu to rows
        medicationTable.setRowFactory(tv -> {
            TableRow<Medication> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem viewItem = new MenuItem("View Details");
            viewItem.setOnAction(event -> handleViewMedicationDetails());

            MenuItem updateStockItem = new MenuItem("Update Stock");
            updateStockItem.setOnAction(event -> handleUpdateStock());

            MenuItem updateItem = new MenuItem("Update Medication");
            updateItem.setOnAction(event -> handleUpdateMedication());

            MenuItem deleteItem = new MenuItem("Delete Medication");
            deleteItem.setOnAction(event -> handleDeleteMedication());

            contextMenu.getItems().addAll(viewItem, updateStockItem, updateItem, deleteItem);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            // Double-click to view details
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    handleViewMedicationDetails();
                }
            });

            return row;
        });
    }

    private void initializeRefreshButton() {
        try {
            // Load static icon for normal state
            staticRefreshIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-static.png")));
            staticRefreshIcon.setFitHeight(20);
            staticRefreshIcon.setFitWidth(20);

            // Load animated GIF for active state
            animatedRefreshIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-animated.gif")));
            animatedRefreshIcon.setFitHeight(20);
            animatedRefreshIcon.setFitWidth(20);

            // Set initial icon as static
            refresh_button.setGraphic(staticRefreshIcon);
            refresh_button.setContentDisplay(ContentDisplay.LEFT);
            refresh_button.setGraphicTextGap(8);
        } catch (Exception e) {
            System.err.println("Could not load refresh icons: " + e.getMessage());
            // Continue without icons
        }

        // Set action for refresh button
        refresh_button.setOnAction(event -> {
            refresh_button.setGraphic(animatedRefreshIcon != null ? animatedRefreshIcon : null);
            refresh_button.setDisable(true);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                // Reload based on which view is currently active
                if (all_medications_button.getStyle().contains("-fx-background-color")) {
                    loadAllMedications();
                } else if (low_stock_button.getStyle().contains("-fx-background-color")) {
                    loadLowStockMedications();
                } else if (expiring_button.getStyle().contains("-fx-background-color")) {
                    loadExpiringMedications();
                }

                refresh_button.setGraphic(staticRefreshIcon != null ? staticRefreshIcon : null);
                refresh_button.setDisable(false);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    public void setDoctorName(String doctorName) {
        // Parse the full name to get first and last name
        String[] nameParts = doctorName.split("\\s+", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        this.loggedInDoctorName = doctorName;
        updateDoctorGreeting();

        // Set current doctor
        setCurrentDoctor(firstName, lastName);
    }

    private void updateDoctorGreeting() {
        if (loggedInDoctorName != null && !loggedInDoctorName.isEmpty()) {
            doctorName.setText("Welcome, Dr. " + loggedInDoctorName);
        } else {
            doctorName.setText("Welcome, Pharmacist");
        }
    }

    private void setCurrentDoctor(String doctorName, String doctorSurname) {
        try {
            // Get all doctors and find the one matching the logged-in name
            List<Doctor> allDoctors = doctorService.getAllDoctors();

            for (Doctor doctor : allDoctors) {
                if (doctor.getDoctorName().equalsIgnoreCase(doctorName) &&
                        doctor.getDoctorSurname().equalsIgnoreCase(doctorSurname)) {
                    this.currentDoctor = doctor;
                    System.out.println("Set current doctor: " + doctor.getDoctorName() + " " +
                            doctor.getDoctorSurname() + " (ID: " + doctor.getDoctorId() + ")");
                    return;
                }
            }

            System.err.println("Warning: Could not find doctor with name: " + doctorName + " " + doctorSurname);
        } catch (Exception e) {
            System.err.println("Error setting current doctor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load all medications from the server
    private void loadAllMedications() {
        new Thread(() -> {
            try {
                // Fetch medications
                List<Medication> medications = medicationService.getAllMedications();

                // Update UI
                Platform.runLater(() -> {
                    medicationsList.clear();
                    medicationsList.addAll(medications);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    UIHelper.showAlert("Error", "Failed to load medications: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    // Load low stock medications
    private void loadLowStockMedications() {
        new Thread(() -> {
            try {
                // Fetch all medications
                List<Medication> allMedications = medicationService.getAllMedications();

                // Filter low stock medications
                List<Medication> lowStockMedications = allMedications.stream()
                        .filter(med -> med.getStockQuantity() != null && med.getStockQuantity() <= LOW_STOCK_THRESHOLD)
                        .collect(Collectors.toList());

                // Update UI
                Platform.runLater(() -> {
                    medicationsList.clear();
                    medicationsList.addAll(lowStockMedications);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    UIHelper.showAlert("Error", "Failed to load low stock medications: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    // Load expiring medications
    private void loadExpiringMedications() {
        new Thread(() -> {
            try {
                // Fetch all medications
                List<Medication> allMedications = medicationService.getAllMedications();

                // Get current date and expiration threshold
                LocalDate today = LocalDate.now();
                LocalDate threshold = today.plusDays(EXPIRING_SOON_DAYS);

                // Filter expiring medications - include already expired medications too
                List<Medication> expiringMedications = allMedications.stream()
                        .filter(med -> med.getExpirationDate() != null &&
                                (med.getExpirationDate().isBefore(today) ||
                                        !med.getExpirationDate().isAfter(threshold)))
                        .collect(Collectors.toList());

                // Update UI
                Platform.runLater(() -> {
                    medicationsList.clear();
                    medicationsList.addAll(expiringMedications);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    UIHelper.showAlert("Error", "Failed to load expiring medications: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    // Handle button actions
    private void handleViewMedicationDetails() {
        Medication selectedMedication = medicationTable.getSelectionModel().getSelectedItem();

        if (selectedMedication == null) {
            UIHelper.showAlert("Selection Required", "Please select a medication to view details.");
            return;
        }

        // For now, just show an alert with the medication details
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Medication Details");
        alert.setHeaderText("Details for " + selectedMedication.getName());

        String details = "ID: " + selectedMedication.getMedicationId() + "\n" +
                "Name: " + selectedMedication.getName() + "\n" +
                "Description: " + selectedMedication.getDescription() + "\n" +
                "Price: €" + String.format("%.2f", selectedMedication.getPrice()) + "\n" +
                "Stock Quantity: " + selectedMedication.getStockQuantity() + "\n" +
                "Expiration Date: " + selectedMedication.getFormattedExpirationDate() + "\n" +
                "Department: " + selectedMedication.getDepartmentName();

        alert.setContentText(details);
        alert.showAndWait();
    }

    private void handleAddMedication() {
        try {
            Stage stage = (Stage) add_medication_button.getScene().getWindow();
            Medication newMedication = MedicationDialog.showAddMedicationDialog(stage);

            if (newMedication != null) {
                boolean success = medicationService.addMedication(newMedication);

                if (success) {
                    UIHelper.showAlert("Success", "Medication added successfully!");
                    loadAllMedications();
                } else {
                    UIHelper.showAlert("Error", "Failed to add medication on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error adding medication: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleUpdateStock() {
        Medication selectedMedication = medicationTable.getSelectionModel().getSelectedItem();

        if (selectedMedication == null) {
            UIHelper.showAlert("Selection Required", "Please select a medication to add stock.");
            return;
        }

        try {
            // Create a custom dialog
            Dialog<Integer> dialog = new Dialog<>();
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner((Stage) update_stock_button.getScene().getWindow());
            dialog.setTitle("Add Stock");
            dialog.setHeaderText("Add Stock for " + selectedMedication.getName());

            // Apply the same CSS as other dialogs
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(
                    MedicationDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("dialog-pane");

            // Set up the buttons
            ButtonType addButtonType = new ButtonType("Add Stock", ButtonBar.ButtonData.OK_DONE);
            dialogPane.getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            dialog.setOnShown(event -> {
                Button addButton = (Button) dialogPane.lookupButton(addButtonType);
                if (addButton != null) addButton.getStyleClass().add("default-button");
            });

            // Create the content
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 10, 10, 10));
            grid.getStyleClass().add("grid-pane");

            TextField quantityField = new TextField("0");
            quantityField.setPromptText("Enter quantity to add");
            quantityField.setPrefWidth(200);

            // Add labels and controls
            grid.add(new Label("Current Stock:"), 0, 0);
            grid.add(new Label(selectedMedication.getStockQuantity().toString()), 1, 0);

            grid.add(new Label("Quantity to Add:"), 0, 1);
            grid.add(quantityField, 1, 1);

            dialogPane.setContent(grid);
            quantityField.requestFocus();

            // Convert the result
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    try {
                        int quantity = Integer.parseInt(quantityField.getText().trim());
                        if (quantity < 0) {
                            UIHelper.showAlert("Invalid Input", "Quantity to add cannot be negative.");
                            return null;
                        }
                        return quantity;
                    } catch (NumberFormatException e) {
                        UIHelper.showAlert("Invalid Input", "Please enter a valid number.");
                        return null;
                    }
                }
                return null;
            });

            // Show dialog and process result
            dialog.showAndWait().ifPresent(quantityToAdd -> {
                try {
                    boolean success = medicationService.updateMedicationStock(selectedMedication, quantityToAdd);

                    if (success) {
                        UIHelper.showAlert("Success", "Stock updated successfully!");
                        // Reload based on current view
                        if (all_medications_button.getStyle().contains("-fx-background-color")) {
                            loadAllMedications();
                        } else if (low_stock_button.getStyle().contains("-fx-background-color")) {
                            loadLowStockMedications();
                        } else if (expiring_button.getStyle().contains("-fx-background-color")) {
                            loadExpiringMedications();
                        }
                    } else {
                        UIHelper.showAlert("Error", "Failed to update stock on the server.");
                    }
                } catch (IOException e) {
                    UIHelper.showAlert("Error", "Error updating stock: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            UIHelper.showAlert("Error", "Could not create dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleUpdateMedication() {
        Medication selectedMedication = medicationTable.getSelectionModel().getSelectedItem();

        if (selectedMedication == null) {
            UIHelper.showAlert("Selection Required", "Please select a medication to update.");
            return;
        }

        try {
            Stage stage = (Stage) update_medication_button.getScene().getWindow();
            Medication updatedMedication = MedicationDialog.showUpdateMedicationDialog(stage, selectedMedication);

            if (updatedMedication != null) {
                boolean success = medicationService.updateMedication(updatedMedication);

                if (success) {
                    UIHelper.showAlert("Success", "Medication updated successfully!");
                    // Reload based on current view
                    if (all_medications_button.getStyle().contains("-fx-background-color")) {
                        loadAllMedications();
                    } else if (low_stock_button.getStyle().contains("-fx-background-color")) {
                        loadLowStockMedications();
                    } else if (expiring_button.getStyle().contains("-fx-background-color")) {
                        loadExpiringMedications();
                    }
                } else {
                    UIHelper.showAlert("Error", "Failed to update medication on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error updating medication: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDeleteMedication() {
        Medication selectedMedication = medicationTable.getSelectionModel().getSelectedItem();

        if (selectedMedication == null) {
            UIHelper.showAlert("Selection Required", "Please select a medication to delete.");
            return;
        }

        try {
            Stage stage = (Stage) delete_medication_button.getScene().getWindow();
            boolean confirmed = MedicationDialog.showDeleteConfirmationDialog(stage, selectedMedication);

            if (confirmed) {
                boolean success = medicationService.deleteMedication(selectedMedication.getMedicationId());

                if (success) {
                    UIHelper.showAlert("Success", "Medication deleted successfully!");
                    // Reload based on current view
                    if (all_medications_button.getStyle().contains("-fx-background-color")) {
                        loadAllMedications();
                    } else if (low_stock_button.getStyle().contains("-fx-background-color")) {
                        loadLowStockMedications();
                    } else if (expiring_button.getStyle().contains("-fx-background-color")) {
                        loadExpiringMedications();
                    }
                } else {
                    UIHelper.showAlert("Error", "Failed to delete medication on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error deleting medication: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inteliMedExpress/resources/fxml/login.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);
            Stage currentStage = (Stage) logout_button.getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("InteliMedExpress - Login");
            currentStage.centerOnScreen();

            System.out.println("Navigation to login form successful");
        } catch (IOException e) {
            UIHelper.showAlert("Navigation Error", "Could not load login form: " + e.getMessage());
            System.err.println("Error navigating to login form: " + e.getMessage());
            e.printStackTrace();
        }
    }
}