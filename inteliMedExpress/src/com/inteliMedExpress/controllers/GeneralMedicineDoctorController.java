package com.inteliMedExpress.controllers;

import com.inteliMedExpress.classes.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GeneralMedicineDoctorController {

    @FXML
    private Label doctorName;

    @FXML
    private ToolBar toolbar;

    @FXML
    private TableView<Patient> patientsTableView;

    // Add fields for each column
    @FXML
    private TableColumn<Patient, Integer> patientIdColumn;
    @FXML
    private TableColumn<Patient, String> nameColumn;
    @FXML
    private TableColumn<Patient, String> surnameColumn;
    @FXML
    private TableColumn<Patient, String> emailColumn;
    @FXML
    private TableColumn<Patient, String> phoneColumn;
    @FXML
    private TableColumn<Patient, String> addressColumn;
    @FXML
    private TableColumn<Patient, Integer> ageColumn;
    @FXML
    private TableColumn<Patient, String> genderColumn;
    @FXML
    private TableColumn<Patient, LocalDate> birthDateColumn;
    @FXML
    private TableColumn<Patient, String> statusColumn;

    // Create an ObservableList to hold the patients
    private ObservableList<Patient> patientsList = FXCollections.observableArrayList();

    @FXML
    private Button logout_button;

    @FXML
    private Button appointments_button;

    @FXML
    private Button patients_button;

    @FXML
    private Button medical_records_button;

    @FXML
    private Button AI_diagnostics_button;

    @FXML
    private Button lab_tests_button;

    @FXML
    private Button hospital_beds_button;


    @FXML
    private Button add_patient_button;

    @FXML
    private Button update_patient_button;

    @FXML
    private Button delete_patient_button;

    private String loggedInDoctorName;


    public void initialize() {
        updateDoctorGreeting();
        disablePatientComponents();
        patientsTableView.setEditable(false);
        patientsTableView.setSelectionModel(null);

        //Navigation buttons
        appointments_button.setOnAction(event -> handleNavigation(appointments_button));
        patients_button.setOnAction(event -> handleNavigation(patients_button));
        medical_records_button.setOnAction(event -> handleNavigation(medical_records_button));
        AI_diagnostics_button.setOnAction(event -> handleNavigation(AI_diagnostics_button));
        lab_tests_button.setOnAction(event -> handleNavigation(lab_tests_button));
        hospital_beds_button.setOnAction(event -> handleNavigation(hospital_beds_button));


        //Patient Table init
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        // Date Formatter
        birthDateColumn.setCellFactory(column -> {
            return new TableCell<Patient, LocalDate>() {
                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        setText(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                    }
                }
            };
        });

        patientsTableView.setItems(patientsList);

        // Add some dummy data for testing
        patientsList.add(new Patient(1, "John", "Doe", "john@example.com",
                "123-456-7890", "123 Main St", 35, "Male",
                LocalDate.of(1988, 5, 15), "Active"));
        patientsList.add(new Patient(2, "Jane", "Smith", "jane@example.com",
                "987-654-3210", "456 Elm St", 28, "Female",
                LocalDate.of(1995, 8, 22), "Active"));


        // Set patients as initially selected
        handleNavigation(patients_button);



    }
    private void handleNavigation(Button selectedButton) {
        // First reset all buttons to default state
        resetAllButtons();

        // Then set the selected button style
        selectedButton.setStyle(
                "-fx-background-color: #1e67a8; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 4; " +
                        "-fx-font-weight: bold;"
        );

        // Show the appropriate view based on button
        if (selectedButton == patients_button) {
            enablePatientComponents();
            // Hide other views if they exist
        } else{
            disablePatientComponents();
        }


        // Add other conditions for other buttons
    }

    private void resetAllButtons() {
        // Reset all buttons to default style
        String defaultStyle =
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #1e67a8; " +
                        "-fx-font-weight: normal;";

        appointments_button.setStyle(defaultStyle);
        patients_button.setStyle(defaultStyle);
        medical_records_button.setStyle(defaultStyle);
        AI_diagnostics_button.setStyle(defaultStyle);
        lab_tests_button.setStyle(defaultStyle);
        hospital_beds_button.setStyle(defaultStyle);
    }

    public void setDoctorName(String doctorName) {
        this.loggedInDoctorName = doctorName;
        updateDoctorGreeting();

    }

    private void updateDoctorGreeting() {
        if (loggedInDoctorName != null && !loggedInDoctorName.isEmpty()) {
            doctorName.setText("Welcome, Dr. " + loggedInDoctorName);
        } else {
            doctorName.setText("Welcome, Doctor");
        }
    }

    private void disablePatientComponents(){
        patientsTableView.setVisible(false);
        add_patient_button.setVisible(false);
        update_patient_button.setVisible(false);
        delete_patient_button.setVisible(false);

    }
    private void enablePatientComponents(){
        patientsTableView.setVisible(true);
        add_patient_button.setVisible(true);
        update_patient_button.setVisible(true);
        delete_patient_button.setVisible(true);
    }

    private void showAlert(String title, String message) {
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
        Image appIcon = new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/logo.png"));
        stage.getIcons().add(appIcon);

        alert.showAndWait();
    }




    public void logout(ActionEvent event) {


        try {
            // Load the register.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inteliMedExpress/resources/fxml/login.fxml"));
            Parent loginRoot = loader.load();

            // Create a new scene with the register form
            Scene loginScene = new Scene(loginRoot);

            // Get the current stage from the event source
            Stage currentStage = (Stage) logout_button.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("InteliMedExpress - Login");
            currentStage.centerOnScreen();

            System.out.println("Navigation to registration form successful");
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not load registration form: " + e.getMessage());
            System.err.println("Error navigating to registration form: " + e.getMessage());
            e.printStackTrace();
        }
    }











}
