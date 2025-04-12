package com.inteliMedExpress.controllers;

import com.inteliMedExpress.classes.*;
import com.inteliMedExpress.classes.Beds.Bed;
import com.inteliMedExpress.classes.Beds.BedDialog;
import com.inteliMedExpress.classes.Beds.BedService;
import com.inteliMedExpress.classes.appointments.Appointment;
import com.inteliMedExpress.classes.appointments.AppointmentDialog;
import com.inteliMedExpress.classes.medicalRecords.MedicalRecord;
import com.inteliMedExpress.classes.medicalRecords.MedicalRecordsDialog;
import com.inteliMedExpress.classes.medicalRecords.MedicalRecordsService;
import com.inteliMedExpress.classes.patients.Patient;
import com.inteliMedExpress.classes.patients.PatientDialog;
import com.inteliMedExpress.classes.patients.PatientService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.control.ContentDisplay;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;


public class GeneralMedicineDoctorController {

    @FXML
    private Label doctorName;


    // Patient Table
    @FXML
    private TableView<Patient> patientsTableView;
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

    private ObservableList<Patient> patientsList = FXCollections.observableArrayList();

    // Toolbar
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

    private String loggedInDoctorName;

    // Patient Buttons
    @FXML
    private Button add_patient_button;
    @FXML
    private Button update_patient_button;
    @FXML
    private Button delete_patient_button;
    @FXML
    private Button refresh_button;

    private ImageView staticRefreshIcon;
    private ImageView animatedRefreshIcon;

    private PatientService patientService;
    private NavigationManager navigationManager;

    // Appointments Table
    @FXML
    private TableView<Appointment> appointmentsViewTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentID_column;
    @FXML
    private TableColumn<Appointment, String> patient_name_column;
    @FXML
    private TableColumn<Appointment, String> patient_surname_column;
    @FXML
    private TableColumn<Appointment, String> doctor_surname_column;
    @FXML
    private TableColumn<Appointment, String> nurse_surname_column;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointment_date_column;
    @FXML
    private TableColumn<Appointment, String> appointment_status_column;
    @FXML
    private TableColumn<Appointment, String> appointment_notes_column;

    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    // Appointment buttons
    @FXML
    private Button create_appointment_button;
    @FXML
    private Button modify_appointment_button;
    @FXML
    private Button delete_appointment_button;
    @FXML
    private Button refresh_appointment_button;

    private ImageView staticRefreshIconAppointment;
    private ImageView animatedRefreshIconAppointment;





    //Medical Records Table
    @FXML
    private TableView<MedicalRecord> MedicalRecordsTable;
    @FXML
    private TableColumn<MedicalRecord, Integer> MedicalRecordID;
    @FXML
    private TableColumn<MedicalRecord, String> medical_record_name;
    @FXML
    private TableColumn<MedicalRecord, String> medical_record_surname;
    @FXML
    private TableColumn<MedicalRecord, String> medical_record_doc_surname;
    @FXML
    private TableColumn<MedicalRecord, String> record_diagnosis;
    @FXML
    private TableColumn<MedicalRecord, String> record_treatment;
    @FXML
    private TableColumn<MedicalRecord, String> record_prescription;
    @FXML
    private TableColumn<MedicalRecord, String> record_status;
    @FXML
    private TableColumn<MedicalRecord, LocalDateTime> record_date;

    private ObservableList<MedicalRecord> medicalRecordsList = FXCollections.observableArrayList();


    private MedicalRecordsService medicalRecordsService;
    //Medical Records Buttons



    private ImageView staticRefreshIconRecords;
    private ImageView animatedRefreshIconRecords;




    @FXML
    private Button refresh_records_button;


    @FXML
    private Button add_record_button;
    @FXML
    private Button modify_record_button;
    @FXML
    private Button delete_record_button;
    @FXML
    private Button view_medical_history_button;



    @FXML
    private Button view_appointment_details_button;



    // Method to show details of the selected appointment
    @FXML
    private void showAppointmentDetails(ActionEvent event ) {
        Appointment selectedAppointment = appointmentsViewTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            UIHelper.showAlert("Selection Required", "Please select an appointment to view details.");
            return;
        }

        // Show dialog with formatted details
        AppointmentDialog.showAppointmentDetailsDialog(selectedAppointment);
    }




    // Beds



    // Hospital Bed components
    @FXML
    private StackPane bedPane, bedPane1, bedPane2, bedPane3, bedPane11, bedPane12, bedPane21,
            bedPane111, bedPane13, bedPane112, bedPane121, bedPane1111;
    @FXML
    private Label bedIdLabel, bedIdLabel1, bedIdLabel2, bedIdLabel3, bedIdLabel11, bedIdLabel12,
            bedIdLabel21, bedIdLabel111, bedIdLabel13, bedIdLabel112, bedIdLabel121, bedIdLabel1111;
    @FXML
    private Label patientNameLabel, patientNameLabel1, patientNameLabel2, patientNameLabel3,
            patientNameLabel11, patientNameLabel12, patientNameLabel21, patientNameLabel111,
            patientNameLabel13, patientNameLabel112, patientNameLabel121, patientNameLabel1111;
    @FXML
    private Label statusLabel, statusLabel1, statusLabel2, statusLabel3, statusLabel11, statusLabel12,
            statusLabel21, statusLabel111, statusLabel13, statusLabel112, statusLabel121, statusLabel1111;

    @FXML
    private Button assign_patient_button;
    @FXML
    private Button discharge_patient_button;




    private BedService bedService;
    private Map<Integer, StackPane> bedPaneMap = new HashMap<>();
    private Map<Integer, Bed> bedsMap = new HashMap<>();
    private Bed selectedBed = null;






    @FXML
    public void initialize() {
        try {
            // Initial setup - doctor greeting
            updateDoctorGreeting();

            // Create required services
            patientService = new PatientService();
            medicalRecordsService = new MedicalRecordsService();

            // Setup patient table
            setupPatientTable();

            // Initialize refresh buttons (if files exist)
            try {
                initializeRefreshButtons();
            } catch (Exception e) {
                System.err.println("Warning: Could not initialize refresh buttons: " + e.getMessage());
                // Continue initialization even if refresh buttons fail
            }

            // Setup appointment table
            setupAppointmentTable();

            // Setup navigation buttons
            setupNavigationButtons();


            //Setup medical records table
            setupMedicalRecordTable();

            setupBeds();

            // Initially show appointments view
            handleNavigation(appointments_button);

        } catch (Exception e) {
            System.err.println("Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @FXML
    public void logout(ActionEvent event) {
        try {
            // Load the login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inteliMedExpress/resources/fxml/login.fxml"));
            Parent loginRoot = loader.load();

            // Create a new scene with the login form
            Scene loginScene = new Scene(loginRoot);

            // Get the current stage from the event source
            Stage currentStage = (Stage) logout_button.getScene().getWindow();

            // Set the new scene on the current stage
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




    //Setup the navigation buttons

    private void setupNavigationButtons() {
        // Setup navigation button actions
        appointments_button.setOnAction(event -> handleNavigation(appointments_button));
        patients_button.setOnAction(event -> handleNavigation(patients_button));
        medical_records_button.setOnAction(event -> handleNavigation(medical_records_button));
        AI_diagnostics_button.setOnAction(event -> handleNavigation(AI_diagnostics_button));
        lab_tests_button.setOnAction(event -> handleNavigation(lab_tests_button));
        hospital_beds_button.setOnAction(event -> handleNavigation(hospital_beds_button));

        // Create navigation manager
        List<Button> navButtons = Arrays.asList(
                appointments_button,
                patients_button,
                medical_records_button,
                AI_diagnostics_button,
                lab_tests_button,
                hospital_beds_button
        );

        try {
            navigationManager = new NavigationManager(navButtons);
        } catch (Exception e) {
            System.err.println("Warning: Could not initialize navigation manager: " + e.getMessage());
        }
    }



    // Refreshing navigation buttons
    private void resetAllButtons() {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: #1e67a8; -fx-font-weight: normal;";
        appointments_button.setStyle(defaultStyle);
        patients_button.setStyle(defaultStyle);
        medical_records_button.setStyle(defaultStyle);
        AI_diagnostics_button.setStyle(defaultStyle);
        lab_tests_button.setStyle(defaultStyle);
        hospital_beds_button.setStyle(defaultStyle);
    }

    //Getting the doctor name for a greeting

    public void setDoctorName(String doctorName) {
        this.loggedInDoctorName = doctorName;
        updateDoctorGreeting();
    }


    // Setting the text for greeting
    private void updateDoctorGreeting() {
        if (loggedInDoctorName != null && !loggedInDoctorName.isEmpty()) {
            doctorName.setText("Welcome, Dr. " + loggedInDoctorName);
        } else {
            doctorName.setText("Welcome, Doctor");
        }
    }






    private void handleNavigation(Button selectedButton) {
        try {
            // Use NavigationManager to handle button styling if available
            if (navigationManager != null) {
                navigationManager.selectButton(selectedButton);
            } else {
                // Fallback styling if NavigationManager is unavailable
                resetAllButtons();
                selectedButton.setStyle("-fx-background-color: #e6f2ff; -fx-text-fill: #0052cc; -fx-font-weight: bold;");
            }

            // Hide all components first
            disablePatientComponents();
            disableAppointmentComponents();
            disableMedicalRecordComponents();
            disableBedComponents();// Make sure to add this line

            // Show the appropriate view based on button
            if (selectedButton == patients_button) {
                enablePatientComponents();
                loadPatientsFromServer();
            } else if (selectedButton == appointments_button) {
                enableAppointmentComponents();
                loadAllAppointments();
            } else if (selectedButton == medical_records_button) {
                enableMedicalRecordComponents();
                loadAllMedicalRecords();
            }else if (selectedButton == hospital_beds_button) {
                enableBedComponents();
                loadAndDisplayBeds();
            }
            // Other navigation options would be handled here
        } catch (Exception e) {
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }




    //Setup of tables


    private void setupPatientTable() {
        // Configure patient table
        patientsTableView.setEditable(false);
        TableView.TableViewSelectionModel<Patient> selectionModel = patientsTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        patientsTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        patientsTableView.setPrefHeight(581.0); // Match the height in your FXML
        patientsTableView.setFixedCellSize(25); // Set fixed cell height for better scrolling
        patientsTableView.setMinHeight(581.0); // Ensure minimum height
        VirtualFlow<?> flow = (VirtualFlow<?>) patientsTableView.lookup(".virtual-flow");
        if (flow != null) {
            flow.setVertical(true);
        }

        // Configure patient table columns
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

        // Setup patient action buttons
        add_patient_button.setOnAction(event -> handleAddPatient());
        update_patient_button.setOnAction(event -> handleUpdatePatient());
        delete_patient_button.setOnAction(event -> handleDeletePatient());
    }







    private void setupAppointmentTable() {
        // Configure appointment table
        TableView.TableViewSelectionModel<Appointment> selectionModel = appointmentsViewTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        // In setupAppointmentTable()
        appointmentsViewTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        appointmentsViewTable.setPrefHeight(581.0); // Match the height in your FXML
        appointmentsViewTable.setFixedCellSize(25); // Set fixed cell height for better scrolling
        appointmentsViewTable.setMinHeight(581.0); // Ensure minimum height
        VirtualFlow<?> flowAppointments = (VirtualFlow<?>) appointmentsViewTable.lookup(".virtual-flow");
        if (flowAppointments != null) {
            flowAppointments.setVertical(true);
        }

        // Configure appointment table columns
        appointmentID_column.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        patient_name_column.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patient_surname_column.setCellValueFactory(new PropertyValueFactory<>("patientSurname"));
        doctor_surname_column.setCellValueFactory(new PropertyValueFactory<>("doctorSurname"));
        nurse_surname_column.setCellValueFactory(new PropertyValueFactory<>("nurseSurname"));
        appointment_date_column.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        // Add this new cell factory to format the date-time properly
        appointment_date_column.setCellFactory(column -> {
            return new TableCell<Appointment, LocalDateTime>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                @Override
                protected void updateItem(LocalDateTime dateTime, boolean empty) {
                    super.updateItem(dateTime, empty);

                    if (empty || dateTime == null) {
                        setText(null);
                    } else {
                        setText(formatter.format(dateTime));
                    }
                }
            };
        });

        appointment_status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
        appointment_notes_column.setCellValueFactory(new PropertyValueFactory<>("notes"));

        appointmentsViewTable.setItems(appointmentList);

        // Setup appointment action buttons - add event handlers
        create_appointment_button.setOnAction(event -> handleCreateAppointment());
        modify_appointment_button.setOnAction(event -> handleModifyAppointment());
        delete_appointment_button.setOnAction(event -> handleDeleteAppointment());
    }



    private void setupMedicalRecordTable() {

        // Configure MedicalRecord table
        TableView.TableViewSelectionModel<MedicalRecord> selectionModel = MedicalRecordsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        MedicalRecordsTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        MedicalRecordsTable.setPrefHeight(581.0); // Match the height in your FXML
        MedicalRecordsTable.setFixedCellSize(25); // Set fixed cell height for better scrolling
        MedicalRecordsTable.setMinHeight(581.0); // Ensure minimum height
        VirtualFlow<?> flowRecords = (VirtualFlow<?>) MedicalRecordsTable.lookup(".virtual-flow");
        if (flowRecords != null) {
            flowRecords.setVertical(true);
        }

        //Configure medicalRecords table columns
        MedicalRecordID.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        medical_record_name.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        medical_record_surname.setCellValueFactory(new PropertyValueFactory<>("patientSurname"));
        medical_record_doc_surname.setCellValueFactory(new PropertyValueFactory<>("doctorSurname"));
        record_diagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        record_treatment.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        record_prescription.setCellValueFactory(new PropertyValueFactory<>("prescription"));
        record_status.setCellValueFactory(new PropertyValueFactory<>("recordStatus"));
        record_date.setCellValueFactory(new PropertyValueFactory<>("recordDate"));

        record_date.setCellFactory(column -> new TableCell<MedicalRecord, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);
                setText(empty || dateTime == null ? null : formatter.format(dateTime));
            }
        });




        MedicalRecordsTable.setItems(medicalRecordsList);



        add_record_button.setOnAction(event -> handleAddRecord());
        modify_record_button.setOnAction(actionEvent -> handleUpdateRecord());
        delete_record_button.setOnAction(actionEvent -> handleDeleteRecord());
        view_medical_history_button.setOnAction(actionEvent -> handleViewMedicalHistory());



    }



    // Method to initialize the bed service and set up bed interaction
    private void setupBeds() {
        // Initialize bed service
        bedService = new BedService();

        // Map all bed components for easier access
        mapBedComponents();

        // Add buttons for assigning and discharging patients
        if (assign_patient_button == null) {
            assign_patient_button = new Button("Assign Patient");
            assign_patient_button.getStyleClass().add("action-button");
            assign_patient_button.setPrefWidth(154.0);
            assign_patient_button.setPrefHeight(50.0);
            assign_patient_button.setLayoutX(1075.0);
            assign_patient_button.setLayoutY(154.0);
            assign_patient_button.setOnAction(event -> handleAssignPatient());
        }
        assign_patient_button.setVisible(false);
        assign_patient_button.setDisable(true); // Initially disabled

        if (discharge_patient_button == null) {
            discharge_patient_button = new Button("Discharge Patient");
            discharge_patient_button.getStyleClass().add("action-button");
            discharge_patient_button.setPrefWidth(154.0);
            discharge_patient_button.setPrefHeight(50.0);
            discharge_patient_button.setLayoutX(1075.0);
            discharge_patient_button.setLayoutY(219.0);
            discharge_patient_button.setOnAction(event -> handleDischargePatient());
        }
        discharge_patient_button.setVisible(false);
        discharge_patient_button.setDisable(true); // Initially disabled

        // Add buttons to the parent container if they don't exist yet
        if (!((AnchorPane)bedPane.getParent()).getChildren().contains(assign_patient_button)) {
            ((AnchorPane)bedPane.getParent()).getChildren().addAll(assign_patient_button, discharge_patient_button);
        }
    }



    //refresh buttons

    private void initializePatientRefreshButton() {
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
                loadPatientsFromServer();
                refresh_button.setGraphic(staticRefreshIcon != null ? staticRefreshIcon : null);
                refresh_button.setDisable(false);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    private void initializeRefreshButtons() {
        // Initialize patient refresh button
        initializePatientRefreshButton();

        // Initialize appointment refresh button
        try {
            // Reuse the same icon resources for appointments
            staticRefreshIconAppointment = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-static.png")));
            staticRefreshIconAppointment.setFitHeight(20);
            staticRefreshIconAppointment.setFitWidth(20);

            animatedRefreshIconAppointment = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-animated.gif")));
            animatedRefreshIconAppointment.setFitHeight(20);
            animatedRefreshIconAppointment.setFitWidth(20);

            refresh_appointment_button.setGraphic(staticRefreshIconAppointment);
            refresh_appointment_button.setContentDisplay(ContentDisplay.LEFT);
            refresh_appointment_button.setGraphicTextGap(8);
        } catch (Exception e) {
            System.err.println("Could not load appointment refresh icons: " + e.getMessage());
            // Continue without icons
        }

        // Set action for appointment refresh button
        refresh_appointment_button.setOnAction(event -> {
            refresh_appointment_button.setGraphic(animatedRefreshIconAppointment != null ? animatedRefreshIconAppointment : null);
            refresh_appointment_button.setDisable(true);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                loadAllAppointments();
                refresh_appointment_button.setGraphic(staticRefreshIconAppointment != null ? staticRefreshIconAppointment : null);
                refresh_appointment_button.setDisable(false);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });

        // Initialize medical records refresh button
        try {
            // Reuse the same icon resources for medical records
            staticRefreshIconRecords = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-static.png")));
            staticRefreshIconRecords.setFitHeight(20);
            staticRefreshIconRecords.setFitWidth(20);

            animatedRefreshIconRecords = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-animated.gif")));
            animatedRefreshIconRecords.setFitHeight(20);
            animatedRefreshIconRecords.setFitWidth(20);

            refresh_records_button.setGraphic(staticRefreshIconRecords);
            refresh_records_button.setContentDisplay(ContentDisplay.LEFT);
            refresh_records_button.setGraphicTextGap(8);
        } catch (Exception e) {
            System.err.println("Could not load medical records refresh icons: " + e.getMessage());
            // Continue without icons
        }

        // Set action for medical records refresh button
        refresh_records_button.setOnAction(event -> {
            refresh_records_button.setGraphic(animatedRefreshIconRecords != null ? animatedRefreshIconRecords : null);
            refresh_records_button.setDisable(true);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                loadAllMedicalRecords();
                refresh_records_button.setGraphic(staticRefreshIconRecords != null ? staticRefreshIconRecords : null);
                refresh_records_button.setDisable(false);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }



    //Enable-Disable Components


    // Patients dashboard
    private void disablePatientComponents(){
        patientsTableView.setVisible(false);
        add_patient_button.setVisible(false);
        update_patient_button.setVisible(false);
        delete_patient_button.setVisible(false);
        refresh_button.setVisible(false);
    }

    private void enablePatientComponents(){
        patientsTableView.setVisible(true);
        add_patient_button.setVisible(true);
        update_patient_button.setVisible(true);
        delete_patient_button.setVisible(true);
        refresh_button.setVisible(true);
    }




    // Appointments dashboard
    private void disableAppointmentComponents(){
        appointmentsViewTable.setVisible(false);
        create_appointment_button.setVisible(false);
        modify_appointment_button.setVisible(false);
        delete_appointment_button.setVisible(false);
        refresh_appointment_button.setVisible(false);
        view_appointment_details_button.setVisible(false);

    }

    private void enableAppointmentComponents(){
        appointmentsViewTable.setVisible(true);
        create_appointment_button.setVisible(true);
        modify_appointment_button.setVisible(true);
        delete_appointment_button.setVisible(true);
        refresh_appointment_button.setVisible(true);
        view_appointment_details_button.setVisible(true);
    }



    //Medical Records dashboard
    private void disableMedicalRecordComponents() {
        MedicalRecordsTable.setVisible(false);
        add_record_button.setVisible(false);
        modify_record_button.setVisible(false);
        delete_record_button.setVisible(false);
        view_medical_history_button.setVisible(false);
        refresh_records_button.setVisible(false);
    }

    private void enableMedicalRecordComponents() {
        MedicalRecordsTable.setVisible(true);
        add_record_button.setVisible(true);
        modify_record_button.setVisible(true);
        delete_record_button.setVisible(true);
        view_medical_history_button.setVisible(true);
        refresh_records_button.setVisible(true);



    }




    //Beds hide/unhide

    // Hospital Beds dashboard
    private void disableBedComponents() {
        bedPane.setVisible(false);
        bedPane1.setVisible(false);
        bedPane2.setVisible(false);
        bedPane3.setVisible(false);
        bedPane11.setVisible(false);
        bedPane12.setVisible(false);
        bedPane21.setVisible(false);
        bedPane111.setVisible(false);
        bedPane13.setVisible(false);
        bedPane112.setVisible(false);
        bedPane121.setVisible(false);
        bedPane1111.setVisible(false);
    }

    private void enableBedComponents() {
        System.out.println("Enabling bed components");
        bedPane.setVisible(true);
        bedPane1.setVisible(true);
        bedPane2.setVisible(true);
        bedPane3.setVisible(true);
        bedPane11.setVisible(true);
        bedPane12.setVisible(true);
        bedPane21.setVisible(true);
        bedPane111.setVisible(true);
        bedPane13.setVisible(true);
        bedPane112.setVisible(true);
        bedPane121.setVisible(true);
        bedPane1111.setVisible(true);

        // Make sure the buttons are visible
        if (assign_patient_button != null) {
            assign_patient_button.setVisible(true);
            System.out.println("Assign patient button made visible");
        } else {
            System.out.println("Warning: Assign patient button is null");
        }

        if (discharge_patient_button != null) {
            discharge_patient_button.setVisible(true);
            System.out.println("Discharge patient button made visible");
        } else {
            System.out.println("Warning: Discharge patient button is null");
        }

        // Load bed data
        loadAndDisplayBeds();
    }





    // Patient Handlers

    private void loadPatientsFromServer() {
        // Create a background thread to fetch data
        new Thread(() -> {
            try {
                List<Patient> serverPatients = patientService.getAllPatients();
                Platform.runLater(() -> {
                    patientsList.clear();
                    patientsList.addAll(serverPatients);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    UIHelper.showAlert("Connection Error", "Failed to fetch patients from server: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleAddPatient() {
        try {
            Stage stage = (Stage) add_patient_button.getScene().getWindow();
            Patient newPatient = PatientDialog.showAddPatientDialog(stage);

            if (newPatient != null) {
                boolean success = patientService.addPatient(newPatient);
                if (success) {
                    UIHelper.showAlert("Success", "Patient added successfully!");
                    loadPatientsFromServer(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to add patient to the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error adding patient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdatePatient() {
        Patient selectedPatient = patientsTableView.getSelectionModel().getSelectedItem();

        if (selectedPatient == null) {
            UIHelper.showAlert("Selection Required", "Please select a patient to update.");
            return;
        }

        try {
            Stage stage = (Stage) update_patient_button.getScene().getWindow();
            Patient updatedPatient = PatientDialog.showUpdatePatientDialog(stage, selectedPatient);

            if (updatedPatient != null) {
                boolean success = patientService.updatePatient(updatedPatient);
                if (success) {
                    UIHelper.showAlert("Success", "Patient updated successfully!");
                    loadPatientsFromServer(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to update patient on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error updating patient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletePatient() {
        Patient selectedPatient = patientsTableView.getSelectionModel().getSelectedItem();

        if (selectedPatient == null) {
            UIHelper.showAlert("Selection Required", "Please select a patient to delete.");
            return;
        }

        try {
            Stage stage = (Stage) delete_patient_button.getScene().getWindow();
            boolean confirmed = PatientDialog.showDeleteConfirmationDialog(stage, selectedPatient);

            if (confirmed) {
                boolean success = patientService.deletePatient(selectedPatient.getPatientId());
                if (success) {
                    UIHelper.showAlert("Success", "Patient deleted successfully!");
                    loadPatientsFromServer(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to delete patient from the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error deleting patient: " + e.getMessage());
            e.printStackTrace();
        }
    }






    //Appointment Handlers

    private void loadAllAppointments() {
        // Create a background thread to fetch data
        new Thread(() -> {
            try {
                // Use the static method from Appointment class
                List<Appointment> serverAppointments = Appointment.getAllAppointments();

                // Update UI on the JavaFX application thread
                Platform.runLater(() -> {
                    appointmentList.clear();
                    appointmentList.addAll(serverAppointments);
                });
            } catch (IOException e) {
                // Handle error on JavaFX thread
                Platform.runLater(() -> {
                    UIHelper.showAlert("Connection Error", "Failed to fetch appointments from server: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }




    //Appointment Handlers



    private void handleCreateAppointment() {
        try {
            Stage stage = (Stage) create_appointment_button.getScene().getWindow();
            Appointment newAppointment = AppointmentDialog.showAddAppointmentDialog(stage);

            if (newAppointment != null) {
                boolean success = newAppointment.addToServer();
                if (success) {
                    UIHelper.showAlert("Success", "Appointment added successfully!");
                    loadAllAppointments(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to add appointment to the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error adding appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }




    private void handleModifyAppointment() {
        Appointment selectedAppointment = appointmentsViewTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            UIHelper.showAlert("Selection Required", "Please select an appointment to modify.");
            return;
        }

        try {
            Stage stage = (Stage) modify_appointment_button.getScene().getWindow();
            Appointment updatedAppointment = AppointmentDialog.showUpdateAppointmentDialog(stage, selectedAppointment);

            if (updatedAppointment != null) {
                boolean success = updatedAppointment.updateOnServer();
                if (success) {
                    UIHelper.showAlert("Success", "Appointment updated successfully!");
                    loadAllAppointments(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to update appointment on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error updating appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDeleteAppointment() {
        Appointment selectedAppointment = appointmentsViewTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            UIHelper.showAlert("Selection Required", "Please select an appointment to delete.");
            return;
        }

        try {
            Stage stage = (Stage) delete_appointment_button.getScene().getWindow();
            boolean confirmed = AppointmentDialog.showDeleteConfirmationDialog(stage, selectedAppointment);

            if (confirmed) {
                boolean success = Appointment.deleteAppointment(selectedAppointment.getAppointmentId());
                if (success) {
                    UIHelper.showAlert("Success", "Appointment deleted successfully!");
                    loadAllAppointments(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to delete appointment from the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error deleting appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }





    //Medical Records Handlers

    // Just one loadAllMedicalRecords method
    private void loadAllMedicalRecords() {
        new Thread(() -> {
            try {
                // Use MedicalRecordsService to fetch records
                List<MedicalRecord> serverMedicalRecords = medicalRecordsService.getAllMedicalRecords();

                // Update UI on the JavaFX application thread
                Platform.runLater(() -> {
                    medicalRecordsList.clear();
                    medicalRecordsList.addAll(serverMedicalRecords);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    UIHelper.showAlert("Connection Error", "Failed to fetch medical records from server: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    // Just one handleAddRecord method
    private void handleAddRecord() {
        try {
            Stage stage = (Stage) add_record_button.getScene().getWindow();
            MedicalRecord newRecord = MedicalRecordsDialog.showAddMedicalRecordDialog(stage);

            if (newRecord != null) {
                boolean success = medicalRecordsService.addMedicalRecord(newRecord);
                if (success) {
                    UIHelper.showAlert("Success", "Medical record added successfully!");
                    loadAllMedicalRecords(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to add medical record to the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error adding medical record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Just one handleUpdateRecord method
    private void handleUpdateRecord() {
        MedicalRecord selectedRecord = MedicalRecordsTable.getSelectionModel().getSelectedItem();

        if (selectedRecord == null) {
            UIHelper.showAlert("Selection Required", "Please select a medical record to update.");
            return;
        }

        try {
            Stage stage = (Stage) modify_record_button.getScene().getWindow();
            MedicalRecord updatedRecord = MedicalRecordsDialog.showUpdateMedicalRecordDialog(stage, selectedRecord);

            if (updatedRecord != null) {
                boolean success = medicalRecordsService.updateMedicalRecord(updatedRecord);
                if (success) {
                    UIHelper.showAlert("Success", "Medical record updated successfully!");
                    loadAllMedicalRecords(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to update medical record on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error updating medical record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Just one handleDeleteRecord method
    private void handleDeleteRecord() {
        MedicalRecord selectedRecord = MedicalRecordsTable.getSelectionModel().getSelectedItem();

        if (selectedRecord == null) {
            UIHelper.showAlert("Selection Required", "Please select a medical record to delete.");
            return;
        }

        try {
            Stage stage = (Stage) delete_record_button.getScene().getWindow();
            boolean confirmed = MedicalRecordsDialog.showDeleteConfirmationDialog(stage, selectedRecord);

            if (confirmed) {
                boolean success = medicalRecordsService.deleteMedicalRecord(selectedRecord.getRecordId());
                if (success) {
                    UIHelper.showAlert("Success", "Medical record deleted successfully!");
                    loadAllMedicalRecords(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to delete medical record from the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error deleting medical record: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void handleViewMedicalHistory() {
        MedicalRecord selectedRecord = MedicalRecordsTable.getSelectionModel().getSelectedItem();

        if (selectedRecord == null) {
            UIHelper.showAlert("Selection Required", "Please select a patient's medical record to view history.");
            return;
        }

        try {
            // Filter medical records by patient name and surname
            List<MedicalRecord> patientHistory = medicalRecordsList.stream()
                    .filter(record -> record.getPatientName().equals(selectedRecord.getPatientName()) &&
                            record.getPatientSurname().equals(selectedRecord.getPatientSurname()))
                    .sorted(Comparator.comparing(MedicalRecord::getRecordDate))
                    .collect(Collectors.toList());

            if (patientHistory.isEmpty()) {
                UIHelper.showAlert("No Records", "No medical history found for this patient.");
                return;
            }

            // Create a custom dialog
            Stage stage = (Stage) view_medical_history_button.getScene().getWindow();
            Dialog<Void> dialog = new Dialog<>();
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Patient Medical History Timeline");
            dialog.setHeaderText("Medical History for " + selectedRecord.getPatientName() + " " + selectedRecord.getPatientSurname());

            // Apply custom styling to the dialog
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(
                    MedicalRecordsDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());
            dialogPane.setPrefWidth(900);
            dialogPane.setPrefHeight(600);

            // Create a ScrollPane to contain the timeline
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(500);

            // Timeline container
            VBox timelineContainer = new VBox(20);
            timelineContainer.setPadding(new Insets(20));
            timelineContainer.setAlignment(Pos.CENTER);

            // Add a title for the timeline
            Label titleLabel = new Label("Medical Timeline");
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            timelineContainer.getChildren().add(titleLabel);

            // Create timeline visualization
            HBox timelineTrack = new HBox();
            timelineTrack.setAlignment(Pos.CENTER);
            timelineTrack.setSpacing(0);
            timelineTrack.setPadding(new Insets(10, 0, 30, 0));

            // Create a map to group records by month/year for simplified timeline
            Map<YearMonth, List<MedicalRecord>> recordsByMonth = new HashMap<>();
            for (MedicalRecord record : patientHistory) {
                YearMonth yearMonth = YearMonth.from(record.getRecordDate());
                recordsByMonth.computeIfAbsent(yearMonth, k -> new ArrayList<>()).add(record);
            }

            // Get the min and max dates for timeline bounds
            LocalDateTime minDate = patientHistory.get(0).getRecordDate();
            LocalDateTime maxDate = patientHistory.get(patientHistory.size() - 1).getRecordDate();

            // Create a sorted list of year-months for our timeline
            List<YearMonth> timelineMonths = new ArrayList<>();
            YearMonth current = YearMonth.from(minDate);
            YearMonth end = YearMonth.from(maxDate);

            while (!current.isAfter(end)) {
                timelineMonths.add(current);
                current = current.plusMonths(1);
            }

            // Create the visual timeline
            for (YearMonth month : timelineMonths) {
                boolean hasRecords = recordsByMonth.containsKey(month);

                // Create the timeline node
                VBox timelineNode = new VBox(5);
                timelineNode.setAlignment(Pos.TOP_CENTER);
                timelineNode.setPrefWidth(120);

                // Month label
                Label monthLabel = new Label(month.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " " + month.getYear());
                monthLabel.setStyle("-fx-font-size: 10px;");

                // Timeline node (circle or square)
                Region node;
                if (hasRecords) {
                    Circle circle = new Circle(10);
                    circle.setFill(Color.DODGERBLUE);
                    circle.setStroke(Color.BLACK);
                    circle.setStrokeWidth(1);

                    // Add record count badge if multiple records in same month
                    List<MedicalRecord> monthRecords = recordsByMonth.get(month);
                    if (monthRecords.size() > 1) {
                        StackPane stackPane = new StackPane();
                        stackPane.getChildren().add(circle);

                        Label countLabel = new Label(String.valueOf(monthRecords.size()));
                        countLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: white; -fx-font-weight: bold;");
                        stackPane.getChildren().add(countLabel);

                        node = stackPane;
                    } else {
                        // Wrap the circle in a StackPane so it can be a Region
                        StackPane circleWrapper = new StackPane();
                        circleWrapper.getChildren().add(circle);
                        node = circleWrapper;
                    }
                } else {
                    // Empty timeline node needs to be wrapped too
                    Circle emptyCircle = new Circle(5);
                    emptyCircle.setFill(Color.LIGHTGRAY);
                    StackPane emptyWrapper = new StackPane();
                    emptyWrapper.getChildren().add(emptyCircle);
                    node = emptyWrapper;
                }

                // Connecting line
                Line line = new Line();
                line.setStartX(0);
                line.setEndX(120);
                line.setStroke(Color.LIGHTGRAY);

                // Add components to node
                timelineNode.getChildren().addAll(node, line, monthLabel);

                // Make nodes with records clickable to show details
                if (hasRecords) {
                    List<MedicalRecord> monthRecords = recordsByMonth.get(month);
                    final YearMonth finalMonth = month;
                    timelineNode.setOnMouseClicked(event -> showMonthRecordsDetails(finalMonth, monthRecords));
                    timelineNode.setCursor(Cursor.HAND);
                    timelineNode.setStyle("-fx-background-color: rgba(135, 206, 250, 0.1); -fx-background-radius: 5;");
                }

                timelineTrack.getChildren().add(timelineNode);
            }

            // Add timeline to container
            timelineContainer.getChildren().add(timelineTrack);

            // Add summary panel with most recent record details
            MedicalRecord mostRecentRecord = patientHistory.get(patientHistory.size() - 1);
            VBox summaryBox = createRecordSummaryPanel(mostRecentRecord, "Most Recent Record");
            timelineContainer.getChildren().add(summaryBox);

            // Add instructions
            Label instructionsLabel = new Label("Click on any blue node to view details for that month");
            instructionsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #555555;");
            timelineContainer.getChildren().add(instructionsLabel);

            // Set the content
            scrollPane.setContent(timelineContainer);
            dialogPane.setContent(scrollPane);

            // Add close button
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            dialog.showAndWait();
        } catch (Exception e) {
            UIHelper.showAlert("Error", "Error viewing medical history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to show details for a specific month
    private void showMonthRecordsDetails(YearMonth month, List<MedicalRecord> records) {
        Dialog<Void> detailsDialog = new Dialog<>();
        detailsDialog.setTitle("Medical Records: " +
                month.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + month.getYear());
        detailsDialog.setHeaderText("Patient: " + records.get(0).getPatientName() + " " + records.get(0).getPatientSurname());

        // Apply styling
        DialogPane dialogPane = detailsDialog.getDialogPane();
        dialogPane.getStylesheets().add(
                MedicalRecordsDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());

        // Content area
        VBox content = new VBox(15);
        content.setPadding(new Insets(10));

        // Sort records by date (most recent first)
        records.sort(Comparator.comparing(MedicalRecord::getRecordDate).reversed());

        // Add each record
        for (MedicalRecord record : records) {
            VBox recordSummary = createRecordSummaryPanel(record,
                    "Record Date: " + record.getRecordDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
            content.getChildren().add(recordSummary);
        }

        // Add to a scroll pane for larger history
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setPrefWidth(600);

        dialogPane.setContent(scrollPane);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);

        detailsDialog.showAndWait();
    }

    // Helper method to create a summary panel for a medical record
    private VBox createRecordSummaryPanel(MedicalRecord record, String title) {
        VBox recordBox = new VBox(8);
        recordBox.setPadding(new Insets(15));
        recordBox.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Create a grid for record details
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(8);

        // Add record details
        int row = 0;

        addDetailRow(detailsGrid, row++, "Doctor:", "Dr. " + record.getDoctorSurname());
        addDetailRow(detailsGrid, row++, "Diagnosis:", record.getDiagnosis());
        addDetailRow(detailsGrid, row++, "Treatment:", record.getTreatment());
        addDetailRow(detailsGrid, row++, "Prescription:", record.getPrescription());

        // Status with color coding
        Label statusLabel = new Label("Status:");
        statusLabel.setStyle("-fx-font-weight: bold;");

        Label statusValue = new Label(record.getRecordStatus());
        String statusColor;
        switch (record.getRecordStatus().toLowerCase()) {
            case "active":
                statusColor = "forestgreen";
                break;
            case "pending":
                statusColor = "darkorange";
                break;
            case "completed":
                statusColor = "royalblue";
                break;
            case "canceled":
                statusColor = "crimson";
                break;
            default:
                statusColor = "black";
        }
        statusValue.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold;");

        detailsGrid.add(statusLabel, 0, row);
        detailsGrid.add(statusValue, 1, row);

        // Add components to panel
        recordBox.getChildren().addAll(titleLabel, detailsGrid);

        return recordBox;
    }

    // Helper method to add a row to the details grid
    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold;");

        Label valueNode = new Label(value != null && !value.isEmpty() ? value : "N/A");

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }




    // Map all bed components for easier access
    private void mapBedComponents() {
        bedPaneMap.put(1, bedPane);
        bedPaneMap.put(2, bedPane1);
        bedPaneMap.put(3, bedPane2);
        bedPaneMap.put(4, bedPane3);
        bedPaneMap.put(5, bedPane11);
        bedPaneMap.put(6, bedPane12);
        bedPaneMap.put(7, bedPane21);
        bedPaneMap.put(8, bedPane111);
        bedPaneMap.put(9, bedPane13);
        bedPaneMap.put(10, bedPane112);
        bedPaneMap.put(11, bedPane121);
        bedPaneMap.put(12, bedPane1111);

        // Add click handlers to all bed panes
        for (Map.Entry<Integer, StackPane> entry : bedPaneMap.entrySet()) {
            final Integer bedIndex = entry.getKey();
            StackPane bedPane = entry.getValue();

            // Explicitly set these properties
            bedPane.setCursor(Cursor.HAND);
            bedPane.setPickOnBounds(true); // Ensures clicks register within bounds

            // Debug output for clicks
            bedPane.setOnMouseClicked(e -> {
                // Get the bedId from the bedPane's bedIdLabel, not from the map index
                Label bedIdLabel = (Label) bedPane.lookup(".bed-id-label");
                String bedIdText = bedIdLabel.getText();

                // Extract just the number from "Bed X"
                int bedId = -1;
                try {
                    if (bedIdText.startsWith("Bed ")) {
                        bedId = Integer.parseInt(bedIdText.substring(4).trim());
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("Could not parse bed ID from label: " + bedIdText);
                }

                System.out.println("Clicked on bed with ID: " + bedId);

                // Now look up the bed by its actual ID
                if (bedId > 0 && bedsMap.containsKey(bedId)) {
                    // Reset previous selection styling
                    if (selectedBed != null) {
                        int prevBedId = selectedBed.getBedId();
                        if (bedPaneMap.containsKey(prevBedId)) {
                            bedPaneMap.get(prevBedId).setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.3)));
                            bedPaneMap.get(prevBedId).setStyle("");
                        }
                    }

                    // Set current selection
                    selectedBed = bedsMap.get(bedId);
                    if (selectedBed != null) {
                        System.out.println("Selected bed: " + selectedBed.getBedId() + ", Status: " + selectedBed.getStatus());
                        bedPane.setEffect(new DropShadow(15, Color.DODGERBLUE));
                        bedPane.setStyle("-fx-border-color: #4080c0; -fx-border-width: 3;");

                        // Force button visibility again to be sure
                        assign_patient_button.setVisible(true);
                        discharge_patient_button.setVisible(true);

                        // Ensure buttons are updated based on bed status
                        updateBedActionButtons();
                    }
                } else {
                    System.out.println("No bed data found for ID " + bedId);
                }
            });
        }
    }

    // Update assign/discharge buttons based on selected bed status
    private void updateBedActionButtons() {
        System.out.println("Updating bed action buttons");
        if (selectedBed != null) {
            System.out.println("Selected bed status: " + selectedBed.getStatus());

            if ("Available".equalsIgnoreCase(selectedBed.getStatus()) || "Vacant".equalsIgnoreCase(selectedBed.getStatus())) {
                System.out.println("Enabling assign button, disabling discharge button");
                assign_patient_button.setDisable(false);
                discharge_patient_button.setDisable(true);
            } else if ("Occupied".equalsIgnoreCase(selectedBed.getStatus())) {
                System.out.println("Disabling assign button, enabling discharge button");
                assign_patient_button.setDisable(true);
                discharge_patient_button.setDisable(false);
            } else {
                System.out.println("Unknown status, disabling both buttons");
                assign_patient_button.setDisable(true);
                discharge_patient_button.setDisable(true);
            }
        } else {
            System.out.println("No bed selected, disabling both buttons");
            assign_patient_button.setDisable(true);
            discharge_patient_button.setDisable(true);
        }
    }

    // Load beds from server and update UI
    private void loadAndDisplayBeds() {
        System.out.println("Starting to load bed data...");
        new Thread(() -> {
            try {
                // Set the department to "General" for general medicine
                Bed.setDepartment("General");
                System.out.println("Fetching beds from server...");

                // Get all beds
                List<Bed> beds = bedService.getAllBeds();
                System.out.println("Received " + beds.size() + " beds from server");

                // Log each bed received
                for (Bed bed : beds) {
                    System.out.println("Server bed: " + bed.getBedId() + ", Status: " + bed.getStatus());
                }

                // Update UI on JavaFX thread
                Platform.runLater(() -> {
                    updateBedDisplay(beds);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    UIHelper.showAlert("Error", "Failed to load hospital beds: " + e.getMessage());
                    e.printStackTrace();
                });
            }
        }).start();
    }

    // Update bed display with data from server
    private void updateBedDisplay(List<Bed> beds) {
        System.out.println("Updating bed display with " + beds.size() + " beds");

        // Clear previous mappings
        bedsMap.clear();

        // Reset all bed visuals to a default "no data" state first
        for (Map.Entry<Integer, StackPane> entry : bedPaneMap.entrySet()) {
            StackPane pane = entry.getValue();

            // Find the labels within the bed pane
            Label bedIdLabel = (Label) pane.lookup(".bed-id-label");
            Label patientNameLabel = (Label) pane.lookup(".patient-name-label");
            Label statusLabel = (Label) pane.lookup(".status-label-available");

            if (bedIdLabel != null) bedIdLabel.setText("Bed (No Data)");
            if (patientNameLabel != null) patientNameLabel.setText("No Data");
            if (statusLabel != null) {
                statusLabel.setText("UNKNOWN");
                statusLabel.setStyle("-fx-background-color: #f8f9fa; -fx-text-fill: #6c757d;");
            }
        }

        // Now map beds by their actual bedId, not by index in the list
        for (Bed bed : beds) {
            // Store bed in map by its actual ID
            bedsMap.put(bed.getBedId(), bed);
            System.out.println("Stored bed " + bed.getBedId() + " in bedsMap");

            // Find the corresponding UI pane for this bed ID if it exists
            if (bedPaneMap.containsKey(bed.getBedId())) {
                StackPane bedPane = bedPaneMap.get(bed.getBedId());

                // Update the visual elements
                Label bedIdLabel = (Label) bedPane.lookup(".bed-id-label");
                Label patientNameLabel = (Label) bedPane.lookup(".patient-name-label");
                Label statusLabel = (Label) bedPane.lookup(".status-label-available");

                if (bedIdLabel != null) {
                    bedIdLabel.setText("Bed " + bed.getBedId());
                }

                if (patientNameLabel != null) {
                    if (bed.getPatientFullName() != null && !bed.getPatientFullName().isEmpty()) {
                        patientNameLabel.setText(bed.getPatientFullName());
                        patientNameLabel.setStyle("-fx-background-color: white; -fx-padding: 3 8;");
                    } else {
                        patientNameLabel.setText("No Patient");
                        patientNameLabel.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 3 8;");
                    }
                }

                if (statusLabel != null) {
                    if ("Occupied".equalsIgnoreCase(bed.getStatus())) {
                        statusLabel.setText("OCCUPIED");
                        statusLabel.setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                    } else {
                        statusLabel.setText("AVAILABLE");
                        statusLabel.setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;");
                    }
                }
            } else {
                System.out.println("Warning: No UI component found for bed " + bed.getBedId());
            }
        }

        System.out.println("bedsMap now contains " + bedsMap.size() + " beds");
        for (Map.Entry<Integer, Bed> entry : bedsMap.entrySet()) {
            System.out.println("bedsMap entry: Key=" + entry.getKey() + ", Value=" + entry.getValue());
        }

        // Reset selection
        selectedBed = null;
        updateBedActionButtons();
    }


    private void handleAssignPatient() {
        if (selectedBed == null) {
            UIHelper.showAlert("Selection Required", "Please select a bed first.");
            return;
        }

        try {
            Stage stage = (Stage) assign_patient_button.getScene().getWindow();
            Bed updatedBed = BedDialog.showAssignPatientDialog(stage, selectedBed);

            if (updatedBed != null) {
                boolean success = bedService.assignPatientToBed(updatedBed);
                if (success) {
                    UIHelper.showAlert("Success", "Patient assigned to bed successfully!");
                    loadAndDisplayBeds(); // Refresh all beds
                } else {
                    UIHelper.showAlert("Error", "Failed to assign patient to bed on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error assigning patient to bed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handle discharge patient action
    private void handleDischargePatient() {
        if (selectedBed == null) {
            UIHelper.showAlert("Selection Required", "Please select a bed first.");
            return;
        }

        try {
            Stage stage = (Stage) discharge_patient_button.getScene().getWindow();
            boolean confirmed = BedDialog.showDischargeConfirmationDialog(stage, selectedBed);

            if (confirmed) {
                boolean success = bedService.dischargePatientFromBed(selectedBed);
                if (success) {
                    UIHelper.showAlert("Success", "Patient discharged from bed successfully!");
                    loadAndDisplayBeds(); // Refresh all beds
                } else {
                    UIHelper.showAlert("Error", "Failed to discharge patient from bed on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error discharging patient from bed: " + e.getMessage());
            e.printStackTrace();
        }
    }


}




