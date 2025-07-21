package com.inteliMedExpress.controllers;

import com.inteliMedExpress.classes.*;
import com.inteliMedExpress.classes.Beds.Bed;
import com.inteliMedExpress.classes.Beds.BedDialog;
import com.inteliMedExpress.classes.Beds.BedService;
import com.inteliMedExpress.classes.Employees.Doctor;
import com.inteliMedExpress.classes.Employees.DoctorService;
import com.inteliMedExpress.classes.Employees.NurseService;
import com.inteliMedExpress.classes.Prescriptions.Prescription;
import com.inteliMedExpress.classes.Prescriptions.PrescriptionDialog;
import com.inteliMedExpress.classes.Prescriptions.PrescriptionService;
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



import com.inteliMedExpress.classes.labTests.LabTest;
import com.inteliMedExpress.classes.labTests.LabTestDialog;
import com.inteliMedExpress.classes.labTests.LabTestService;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


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



    //Prescriptions!!
    // Prescriptions-related fields
    @FXML
    private TableView<Prescription> PrescriptionsTable;
    @FXML
    private TableColumn<Prescription, Integer> prescription_column;
    @FXML
    private TableColumn<Prescription, String> patient_name_column_prescription;
    @FXML
    private TableColumn<Prescription, String> patient_surname_prescription;
    @FXML
    private TableColumn<Prescription, String> doctor_surname_prescription;
    @FXML
    private TableColumn<Prescription, String> dosage_prescription;
    @FXML
    private TableColumn<Prescription, String> duration_prescription;
    @FXML
    private TableColumn<Prescription, LocalDateTime> date_prescription;
    @FXML
    private TableColumn<Prescription, String> medication_prescription_text;

    @FXML
    private Button add_prescription_button;
    @FXML
    private Button modify_prescription_button;
    @FXML
    private Button delete_prescription_button;
    @FXML
    private Button refresh_prescription_button;
    @FXML
    private Button prescriptions_button;

    private ObservableList<Prescription> prescriptionsList = FXCollections.observableArrayList();
    private PrescriptionService prescriptionService;
    private ImageView staticRefreshIconPrescription;
    private ImageView animatedRefreshIconPrescription;

    @FXML
    private void showAppointmentDetails(ActionEvent event ) {
        Appointment selectedAppointment = appointmentsViewTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            UIHelper.showAlert("Selection Required", "Please select an appointment to view details.");
            return;
        }
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



    // Lab Tests!!!!

    @FXML
    private TableView<LabTest> testTable;
    @FXML
    private TableColumn<LabTest, Integer> testIDcolumn;
    @FXML
    private TableColumn<LabTest, String> testNameColumn;
    @FXML
    private TableColumn<LabTest, String> testSurnameColumn;
    @FXML
    private TableColumn<LabTest, String> testDoctorSurnameColumn;
    @FXML
    private TableColumn<LabTest, String> testTypeColumn;
    @FXML
    private TableColumn<LabTest, LocalDateTime> OrderDateColumn;
    @FXML
    private TableColumn<LabTest, String> testResultColumn;
    @FXML
    private TableColumn<LabTest, LocalDateTime> testCompletionDateColumn;
    @FXML
    private TableColumn<LabTest, String> testRequestingDoctor;
    @FXML
    private TableColumn<LabTest, String> testRequestingDepartmentColumn;
    @FXML
    private TableColumn<LabTest, String> testNotesColumn;

    @FXML
    private Button requestLabTestButton;

    private ObservableList<LabTest> labTestsList = FXCollections.observableArrayList();
    private LabTestService labTestService;
    @FXML
    private Button refresh_labTests_button;
    private ImageView staticRefreshIconLabTests;
    private ImageView animatedRefreshIconLabTests;





    // AI Diagnostics UI Components
    @FXML
    private ScrollPane aiDiagnosticsScrollPane;
    @FXML
    private ImageView xrayImageView;
    @FXML
    private Label noImageLabel;
    @FXML
    private Label diagnosisResultLabel;
    @FXML
    private Button selectXrayButton;
    @FXML
    private Button analyzeXrayButton;
    @FXML
    private BarChart<String, Number> confidenceChart;
    @FXML
    private Label serverStatusLabel;

    @FXML
    private VBox aiDiagnosticsPane;

    private File selectedXrayFile;
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private final String aiServerUrl = "https://intellimed-ai.onrender.com";


    private String departmentName;

    private Doctor currentDoctor;
    private DoctorService doctorService;


    @FXML
    private Label general_medicine_title;


    @FXML
    public void initialize() {
        try {
            System.out.println("Controller initializing...");

            // Set default department if not set
            if (departmentName == null || departmentName.isEmpty()) {
                departmentName = "General";
                System.out.println("Setting default department: General");
            }

            setDepartment(departmentName);

            // First set up all your UI components and event handlers
            setupNavigationButtons();

            // THEN initialize tables
            setupPatientTable();
            setupAppointmentTable();
            setupMedicalRecordTable();
            setupBeds();
            setupLabTestTable();
            setupAIDiagnosticsHandlers();
            setupPrescriptionTable();
            initializeRefreshButtons();

            // FINALLY activate the default view (appointments)
            System.out.println("Setting up default view...");
            Platform.runLater(() -> {
                if (appointments_button != null) {
                    System.out.println("Showing appointments view");
                    handleNavigation(appointments_button);
                } else {
                    System.err.println("appointments_button is null!");
                }
            });

        } catch (Exception e) {
            System.err.println("Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void initializeServices() {
        patientService = new PatientService();
        medicalRecordsService = new MedicalRecordsService();
        prescriptionService = new PrescriptionService(departmentName);
        labTestService = new LabTestService(departmentName);
        bedService = new BedService();
    }

    public void setDepartment(String department) {
        this.departmentName = department;
        System.out.println("Setting department to: " + department);
        initializeServices();
        updateDoctorGreeting();

        if (general_medicine_title != null) {
            if (departmentName.equals("General")){
                general_medicine_title.setText("General Medicine");
            } else {
                general_medicine_title.setText(department);
            }
        }

        try {
            Appointment.setDepartment(department);
            MedicalRecord.setDepartment(department);
            Patient.setDepartment(department);
            NurseService.setDepartment(department);
            Prescription.setDepartment(department);
            labTestService = new LabTestService(department);
            bedService = new BedService();
            Bed.setDepartment(department);

            // Load data specific to this department
            loadAllAppointments();
            loadPatientsFromServer();
            loadAllMedicalRecords();
            loadAllLabTests();
            mapBedComponents();
            loadAndDisplayBeds();
            loadAllPrescriptions();

            setupNavigationButtons();

            customizeDepartmentUI(department);
        } catch (Exception e) {
            System.err.println("Error setting department: " + e.getMessage());
        }
    }

    private void customizeDepartmentUI(String department) {
        switch (department.toLowerCase()) {
            case "cardiology":
                break;
            case "radiology":
                AI_diagnostics_button.setStyle("-fx-font-weight: bold;");
                break;
        }
    }




    @FXML
    public void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/com/inteliMedExpress/fxml/login.fxml"));
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




    //Setup the navigation buttons

    private void setupNavigationButtons() {
        System.out.println("Setting up navigation buttons...");

        // Set up all navigation buttons with proper logging
        appointments_button.setOnAction(event -> {
            System.out.println("Appointments button clicked");
            handleNavigation(appointments_button);
        });

        patients_button.setOnAction(event -> {
            System.out.println("Patients button clicked");
            handleNavigation(patients_button);
        });

        medical_records_button.setOnAction(event -> {
            System.out.println("Medical Records button clicked");
            handleNavigation(medical_records_button);
        });

        AI_diagnostics_button.setOnAction(event -> {
            System.out.println("AI Diagnostics button clicked");
            handleNavigation(AI_diagnostics_button);
        });

        lab_tests_button.setOnAction(event -> {
            System.out.println("Lab Tests button clicked");
            handleNavigation(lab_tests_button);
        });

        hospital_beds_button.setOnAction(event -> {
            System.out.println("Hospital Beds button clicked");
            handleNavigation(hospital_beds_button);
        });

        prescriptions_button.setOnAction(event -> {
            System.out.println("Prescriptions button clicked");
            handleNavigation(prescriptions_button);
        });

        List<Button> navButtons = Arrays.asList(
                appointments_button, patients_button, medical_records_button,
                AI_diagnostics_button, lab_tests_button, hospital_beds_button,
                prescriptions_button
        );

        try {
            navigationManager = new NavigationManager(navButtons);
            System.out.println("Navigation manager initialized");
        } catch (Exception e) {
            System.err.println("Warning: Could not initialize navigation manager: " + e.getMessage());
        }
    }




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
        // Parse the full name to get first and last name
        String[] nameParts = doctorName.split("\\s+", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        this.loggedInDoctorName = doctorName;
        updateDoctorGreeting();

        setCurrentDoctor(firstName, lastName);
    }



    private void updateDoctorGreeting() {
        if (loggedInDoctorName != null && !loggedInDoctorName.isEmpty()) {
            doctorName.setText("Welcome, Dr. " + loggedInDoctorName);
        } else {
            doctorName.setText("Welcome, Doctor");
        }
    }






    private void handleNavigation(Button selectedButton) {
        try {

            if (navigationManager != null) {
                navigationManager.selectButton(selectedButton);
            }
            else {
                resetAllButtons();
                selectedButton.setStyle("-fx-background-color: #e6f2ff; -fx-text-fill: #0052cc; -fx-font-weight: bold;");
            }

            // Hide all components first
            disablePatientComponents();
            disableAppointmentComponents();
            disableMedicalRecordComponents();
            disableBedComponents();
            disableLabTestComponents();
            disableAIDiagnosticsComponents();
            disablePrescriptionComponents();

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
            } else if (selectedButton == hospital_beds_button) {
                enableBedComponents();
                loadAndDisplayBeds();
            } else if (selectedButton == lab_tests_button) {
                enableLabTestComponents();
            } else if (selectedButton == AI_diagnostics_button) {
                enableAIDiagnosticsComponents();
            }else if (selectedButton == prescriptions_button) {
                enablePrescriptionComponents();

            }
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
        patientsTableView.setPrefHeight(581.0);
        patientsTableView.setFixedCellSize(25);
        patientsTableView.setMinHeight(581.0);
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
        TableView.TableViewSelectionModel<Appointment> selectionModel = appointmentsViewTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        appointmentsViewTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        appointmentsViewTable.setPrefHeight(581.0);
        appointmentsViewTable.setFixedCellSize(25);
        appointmentsViewTable.setMinHeight(581.0);
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

        TableView.TableViewSelectionModel<MedicalRecord> selectionModel = MedicalRecordsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        MedicalRecordsTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        MedicalRecordsTable.setPrefHeight(581.0);
        MedicalRecordsTable.setFixedCellSize(25);
        MedicalRecordsTable.setMinHeight(581.0);
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

    private void setupBeds() {
        bedService = new BedService();
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
        assign_patient_button.setDisable(true);

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
        discharge_patient_button.setDisable(true);

        if (!((AnchorPane)bedPane.getParent()).getChildren().contains(assign_patient_button)) {
            ((AnchorPane)bedPane.getParent()).getChildren().addAll(assign_patient_button, discharge_patient_button);
        }
    }



    // Lab tests table setup


    private void setupLabTestTable() {
        labTestService = new LabTestService(departmentName);

        testTable.setEditable(false);
        TableView.TableViewSelectionModel<LabTest> selectionModel = testTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        testTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Configure lab test table columns
        testIDcolumn.setCellValueFactory(new PropertyValueFactory<>("testId"));
        testNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        testSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("patientSurname"));
        testDoctorSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("doctorSurname"));
        testTypeColumn.setCellValueFactory(new PropertyValueFactory<>("testType"));
        OrderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        testResultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        testCompletionDateColumn.setCellValueFactory(new PropertyValueFactory<>("completionDate"));
        testRequestingDoctor.setCellValueFactory(new PropertyValueFactory<>("requestingDoctorSurname"));
        testRequestingDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("requestingDepartmentName"));
        testNotesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        //Date formatter
        OrderDateColumn.setCellFactory(column -> {
            return new TableCell<LabTest, LocalDateTime>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                @Override
                protected void updateItem(LocalDateTime dateTime, boolean empty) {
                    super.updateItem(dateTime, empty);
                    setText(empty || dateTime == null ? null : formatter.format(dateTime));
                }
            };
        });

        testCompletionDateColumn.setCellFactory(column -> {
            return new TableCell<LabTest, LocalDateTime>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                @Override
                protected void updateItem(LocalDateTime dateTime, boolean empty) {
                    super.updateItem(dateTime, empty);
                    setText(empty || dateTime == null ? null : formatter.format(dateTime));
                }
            };
        });

        // Add a status color
        testResultColumn.setCellFactory(column -> {
            return new TableCell<LabTest, String>() {
                @Override
                protected void updateItem(String result, boolean empty) {
                    super.updateItem(result, empty);

                    if (empty || result == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(result.length() > 30 ? result.substring(0, 27) + "..." : result);
                    }
                }
            };
        });

        testTable.setItems(labTestsList);
        requestLabTestButton.setOnAction(event -> handleRequestLabTest());
        testTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && testTable.getSelectionModel().getSelectedItem() != null) {
                handleViewLabTestDetails();
            }
        });

        testTable.setRowFactory(tv -> {
            TableRow<LabTest> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem viewItem = new MenuItem("View Details");
            viewItem.setOnAction(event -> handleViewLabTestDetails());
            MenuItem assignItem = new MenuItem("Assign Doctor");
            assignItem.setOnAction(event -> handleAssignLabTest());
            MenuItem completeItem = new MenuItem("Complete Test");
            completeItem.setOnAction(event -> handleCompleteLabTest());
            MenuItem deleteItem = new MenuItem("Delete Test");
            deleteItem.setOnAction(event -> handleDeleteLabTest());
            contextMenu.getItems().addAll(viewItem, assignItem, completeItem, deleteItem);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            row.setOnContextMenuRequested(event -> {
                LabTest test = row.getItem();
                if (test != null) {
                    // Only allow assignment if test has no doctor assigned
                    assignItem.setDisable(test.getDoctorId() != null && test.getDoctorId() > 0);

                    // Only allow completion if test has a doctor assigned and is not already completed
                    completeItem.setDisable(test.getDoctorId() == null || test.getDoctorId() <= 0 ||
                            "Completed".equalsIgnoreCase(test.getStatus()));
                }
            });

            return row;
        });
    }
    // Setup prescription table
    private void setupPrescriptionTable() {
        // Configure prescription table
        TableView.TableViewSelectionModel<Prescription> selectionModel = PrescriptionsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        PrescriptionsTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        PrescriptionsTable.setPrefHeight(581.0);
        PrescriptionsTable.setFixedCellSize(25);
        PrescriptionsTable.setMinHeight(581.0);

        VirtualFlow<?> flowPrescriptions = (VirtualFlow<?>) PrescriptionsTable.lookup(".virtual-flow");
        if (flowPrescriptions != null) {
            flowPrescriptions.setVertical(true);
        }

        // Configure prescription table columns using the fields from your Prescription class
        prescription_column.setCellValueFactory(new PropertyValueFactory<>("prescriptionId"));
        patient_name_column_prescription.setCellValueFactory(new PropertyValueFactory<>("patientFirstName"));
        patient_surname_prescription.setCellValueFactory(new PropertyValueFactory<>("patientLastName"));
        doctor_surname_prescription.setCellValueFactory(new PropertyValueFactory<>("doctorSurname"));
        dosage_prescription.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        duration_prescription.setCellValueFactory(new PropertyValueFactory<>("duration"));
        date_prescription.setCellValueFactory(new PropertyValueFactory<>("datePrescribed"));
        medication_prescription_text.setCellValueFactory(new PropertyValueFactory<>("medicationName"));

        // Date formatter for prescription date
        date_prescription.setCellFactory(column -> {
            return new TableCell<Prescription, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime dateTime, boolean empty) {
                    super.updateItem(dateTime, empty);

                    if (empty || dateTime == null) {
                        setText(null);
                        System.out.println("Found empty or null date cell");
                    } else {
                        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        setText(formattedDate);
                        System.out.println("Set date cell to: " + formattedDate);
                    }
                }
            };
        });

        PrescriptionsTable.setItems(prescriptionsList);

        // Setup prescription action buttons
        add_prescription_button.setOnAction(event -> handleAddPrescription());
        modify_prescription_button.setOnAction(event -> handleModifyPrescription());
        delete_prescription_button.setOnAction(event -> handleDeletePrescription());

    }

    private void setCurrentDoctor(String doctorName, String doctorSurname) {
        try {
            // Initialize doctor service if needed
            if (doctorService == null) {
                doctorService = new DoctorService(departmentName);
            }

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



    //refresh buttons

    private void initializePatientRefreshButton() {
        try {
            // Load static icon for normal state
            staticRefreshIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/com/inteliMedExpress/images/refresh-static.png")));
            staticRefreshIcon.setFitHeight(20);
            staticRefreshIcon.setFitWidth(20);

            // Load animated GIF for active state
            animatedRefreshIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/com/inteliMedExpress/images/refresh-animated.gif")));
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
        initializePatientRefreshButton();
        initializePrescriptionRefreshButton();


        try {

            staticRefreshIconAppointment = new ImageView(new Image(getClass().getResourceAsStream("/resources/com/inteliMedExpress/images/refresh-static.png")));
            staticRefreshIconAppointment.setFitHeight(20);
            staticRefreshIconAppointment.setFitWidth(20);

            animatedRefreshIconAppointment = new ImageView(new Image(getClass().getResourceAsStream("/resources/com/inteliMedExpress/images/refresh-animated.gif")));
            animatedRefreshIconAppointment.setFitHeight(20);
            animatedRefreshIconAppointment.setFitWidth(20);

            refresh_appointment_button.setGraphic(staticRefreshIconAppointment);
            refresh_appointment_button.setContentDisplay(ContentDisplay.LEFT);
            refresh_appointment_button.setGraphicTextGap(8);
        } catch (Exception e) {
            System.err.println("Could not load appointment refresh icons: " + e.getMessage());

        }

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

        try {
            staticRefreshIconRecords = new ImageView(new Image(getClass().getResourceAsStream("/resources/com/inteliMedExpress/images/refresh-static.png")));
            staticRefreshIconRecords.setFitHeight(20);
            staticRefreshIconRecords.setFitWidth(20);

            animatedRefreshIconRecords = new ImageView(new Image(getClass().getResourceAsStream("/resources/com/inteliMedExpress/images/refresh-animated.gif")));
            animatedRefreshIconRecords.setFitHeight(20);
            animatedRefreshIconRecords.setFitWidth(20);

            refresh_records_button.setGraphic(staticRefreshIconRecords);
            refresh_records_button.setContentDisplay(ContentDisplay.LEFT);
            refresh_records_button.setGraphicTextGap(8);
        } catch (Exception e) {
            System.err.println("Could not load medical records refresh icons: " + e.getMessage());

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
        try {
            // Reuse the same icon resources for lab tests
            staticRefreshIconLabTests = new ImageView(new Image(getClass().getResourceAsStream("/resources/com/inteliMedExpress/images/refresh-static.png")));
            staticRefreshIconLabTests.setFitHeight(20);
            staticRefreshIconLabTests.setFitWidth(20);

            animatedRefreshIconLabTests = new ImageView(new Image(getClass().getResourceAsStream("/resources/com/inteliMedExpress/images/refresh-animated.gif")));
            animatedRefreshIconLabTests.setFitHeight(20);
            animatedRefreshIconLabTests.setFitWidth(20);

            refresh_labTests_button.setGraphic(staticRefreshIconLabTests);
            refresh_labTests_button.setContentDisplay(ContentDisplay.LEFT);
            refresh_labTests_button.setGraphicTextGap(8);
        } catch (Exception e) {
            System.err.println("Could not load lab tests refresh icons: " + e.getMessage());
            // Continue without icons
        }

// Set action for lab tests refresh button
        refresh_labTests_button.setOnAction(event -> {
            refresh_labTests_button.setGraphic(animatedRefreshIconLabTests != null ? animatedRefreshIconLabTests : null);
            refresh_labTests_button.setDisable(true);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                loadAllLabTests();
                refresh_labTests_button.setGraphic(staticRefreshIconLabTests != null ? staticRefreshIconLabTests : null);
                refresh_labTests_button.setDisable(false);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }
    private void initializePrescriptionRefreshButton() {
        try {
            // Reuse the same icon resources for prescriptions
            staticRefreshIconPrescription = new ImageView(new Image(getClass().getResourceAsStream("/resources/com/inteliMedExpress/images/refresh-static.png")));
            staticRefreshIconPrescription.setFitHeight(20);
            staticRefreshIconPrescription.setFitWidth(20);

            animatedRefreshIconPrescription = new ImageView(new Image(getClass().getResourceAsStream("/resources/com/inteliMedExpress/images/refresh-animated.gif")));
            animatedRefreshIconPrescription.setFitHeight(20);
            animatedRefreshIconPrescription.setFitWidth(20);

            refresh_prescription_button.setGraphic(staticRefreshIconPrescription);
            refresh_prescription_button.setContentDisplay(ContentDisplay.LEFT);
            refresh_prescription_button.setGraphicTextGap(8);
        } catch (Exception e) {
            System.err.println("Could not load prescription refresh icons: " + e.getMessage());
        }

        // Set action for prescription refresh button
        refresh_prescription_button.setOnAction(event -> {
            refresh_prescription_button.setGraphic(animatedRefreshIconPrescription != null ? animatedRefreshIconPrescription : null);
            refresh_prescription_button.setDisable(true);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                loadAllPrescriptions();
                refresh_prescription_button.setGraphic(staticRefreshIconPrescription != null ? staticRefreshIconPrescription : null);
                refresh_prescription_button.setDisable(false);
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

        initializePatientRefreshButton();
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

    //prescriptions
    // Prescriptions dashboard enable/disable functions
    private void disablePrescriptionComponents() {
        PrescriptionsTable.setVisible(false);
        add_prescription_button.setVisible(false);
        modify_prescription_button.setVisible(false);
        delete_prescription_button.setVisible(false);
        refresh_prescription_button.setVisible(false);
    }

    private void enablePrescriptionComponents() {
        PrescriptionsTable.setVisible(true);
        add_prescription_button.setVisible(true);
        modify_prescription_button.setVisible(true);
        delete_prescription_button.setVisible(true);
        refresh_prescription_button.setVisible(true);

    }


    //Beds dashboard
    private void disableBedComponents() {
        // Hide all bed panes
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


        //Due to null pointer errors, added these.
        if (assign_patient_button != null) {
            assign_patient_button.setVisible(false);
        }

        if (discharge_patient_button != null) {
            discharge_patient_button.setVisible(false);
        }
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
        mapBedComponents();
        loadAndDisplayBeds();
    }


    //Enable-Disable lab tests components
    private void disableLabTestComponents() {
        testTable.setVisible(false);
        requestLabTestButton.setVisible(false);
        refresh_labTests_button.setVisible(false);
    }

    private void enableLabTestComponents() {
        testTable.setVisible(true);
        requestLabTestButton.setVisible(true);
        refresh_labTests_button.setVisible(true);
        loadAllLabTests();
    }

    private void disableAIDiagnosticsComponents() {
        aiDiagnosticsScrollPane.setVisible(false);
    }

    private void enableAIDiagnosticsComponents() {
        aiDiagnosticsScrollPane.setVisible(true);
        aiDiagnosticsPane.setVisible(true);
        checkAIServerStatus();
    }


    //AI diagnostics handler
    private void setupAIDiagnosticsHandlers() {
        selectXrayButton.setOnAction(e -> handleSelectXrayImage());
        analyzeXrayButton.setOnAction(e -> handleAnalyzeXray());
        checkAIServerStatus();
    }




    // Patient Handlers

    private void loadPatientsFromServer() {
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
        new Thread(() -> {
            try {
                List<Appointment> serverAppointments = Appointment.getAllAppointments();
                Platform.runLater(() -> {
                    appointmentList.clear();
                    appointmentList.addAll(serverAppointments);
                });
            } catch (IOException e) {
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

    private void loadAllMedicalRecords() {
        new Thread(() -> {
            try {
                List<MedicalRecord> serverMedicalRecords = medicalRecordsService.getAllMedicalRecords();
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

            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(
                    MedicalRecordsDialog.class.getResource("/resources/com/inteliMedExpress/css/patient_dialogs.css").toExternalForm());
            dialogPane.setPrefWidth(900);
            dialogPane.setPrefHeight(600);

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(500);

            VBox timelineContainer = new VBox(20);
            timelineContainer.setPadding(new Insets(20));
            timelineContainer.setAlignment(Pos.CENTER);

            Label titleLabel = new Label("Medical Timeline");
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            timelineContainer.getChildren().add(titleLabel);

            HBox timelineTrack = new HBox();
            timelineTrack.setAlignment(Pos.CENTER);
            timelineTrack.setSpacing(0);
            timelineTrack.setPadding(new Insets(10, 0, 30, 0));

            Map<YearMonth, List<MedicalRecord>> recordsByMonth = new HashMap<>();
            for (MedicalRecord record : patientHistory) {
                YearMonth yearMonth = YearMonth.from(record.getRecordDate());
                recordsByMonth.computeIfAbsent(yearMonth, k -> new ArrayList<>()).add(record);
            }

            LocalDateTime minDate = patientHistory.get(0).getRecordDate();
            LocalDateTime maxDate = patientHistory.get(patientHistory.size() - 1).getRecordDate();

            List<YearMonth> timelineMonths = new ArrayList<>();
            YearMonth current = YearMonth.from(minDate);
            YearMonth end = YearMonth.from(maxDate);

            while (!current.isAfter(end)) {
                timelineMonths.add(current);
                current = current.plusMonths(1);
            }

            for (YearMonth month : timelineMonths) {
                boolean hasRecords = recordsByMonth.containsKey(month);

                VBox timelineNode = new VBox(5);
                timelineNode.setAlignment(Pos.TOP_CENTER);
                timelineNode.setPrefWidth(120);

                Label monthLabel = new Label(month.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " " + month.getYear());
                monthLabel.setStyle("-fx-font-size: 10px;");

                Region node;
                if (hasRecords) {
                    Circle circle = new Circle(10);
                    circle.setFill(Color.DODGERBLUE);
                    circle.setStroke(Color.BLACK);
                    circle.setStrokeWidth(1);

                    List<MedicalRecord> monthRecords = recordsByMonth.get(month);
                    if (monthRecords.size() > 1) {
                        StackPane stackPane = new StackPane();
                        stackPane.getChildren().add(circle);

                        Label countLabel = new Label(String.valueOf(monthRecords.size()));
                        countLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: white; -fx-font-weight: bold;");
                        stackPane.getChildren().add(countLabel);

                        node = stackPane;
                    } else {

                        StackPane circleWrapper = new StackPane();
                        circleWrapper.getChildren().add(circle);
                        node = circleWrapper;
                    }
                } else {

                    Circle emptyCircle = new Circle(5);
                    emptyCircle.setFill(Color.LIGHTGRAY);
                    StackPane emptyWrapper = new StackPane();
                    emptyWrapper.getChildren().add(emptyCircle);
                    node = emptyWrapper;
                }

                Line line = new Line();
                line.setStartX(0);
                line.setEndX(120);
                line.setStroke(Color.LIGHTGRAY);

                timelineNode.getChildren().addAll(node, line, monthLabel);

                if (hasRecords) {
                    List<MedicalRecord> monthRecords = recordsByMonth.get(month);
                    final YearMonth finalMonth = month;
                    timelineNode.setOnMouseClicked(event -> showMonthRecordsDetails(finalMonth, monthRecords));
                    timelineNode.setCursor(Cursor.HAND);
                    timelineNode.setStyle("-fx-background-color: rgba(135, 206, 250, 0.1); -fx-background-radius: 5;");
                }

                timelineTrack.getChildren().add(timelineNode);
            }

            timelineContainer.getChildren().add(timelineTrack);
            MedicalRecord mostRecentRecord = patientHistory.get(patientHistory.size() - 1);
            VBox summaryBox = createRecordSummaryPanel(mostRecentRecord, "Most Recent Record");
            timelineContainer.getChildren().add(summaryBox);

            Label instructionsLabel = new Label("Click on any blue node to view details for that month");
            instructionsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #555555;");
            timelineContainer.getChildren().add(instructionsLabel);

            scrollPane.setContent(timelineContainer);
            dialogPane.setContent(scrollPane);

            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            dialog.showAndWait();
        } catch (Exception e) {
            UIHelper.showAlert("Error", "Error viewing medical history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showMonthRecordsDetails(YearMonth month, List<MedicalRecord> records) {
        Dialog<Void> detailsDialog = new Dialog<>();
        detailsDialog.setTitle("Medical Records: " +
                month.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + month.getYear());
        detailsDialog.setHeaderText("Patient: " + records.get(0).getPatientName() + " " + records.get(0).getPatientSurname());

        DialogPane dialogPane = detailsDialog.getDialogPane();
        dialogPane.getStylesheets().add(
                MedicalRecordsDialog.class.getResource("/resources/com/inteliMedExpress/css/patient_dialogs.css").toExternalForm());

        VBox content = new VBox(15);
        content.setPadding(new Insets(10));

        records.sort(Comparator.comparing(MedicalRecord::getRecordDate).reversed());
        for (MedicalRecord record : records) {
            VBox recordSummary = createRecordSummaryPanel(record,
                    "Record Date: " + record.getRecordDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
            content.getChildren().add(recordSummary);
        }
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setPrefWidth(600);

        dialogPane.setContent(scrollPane);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);

        detailsDialog.showAndWait();
    }

    private VBox createRecordSummaryPanel(MedicalRecord record, String title) {
        VBox recordBox = new VBox(8);
        recordBox.setPadding(new Insets(15));
        recordBox.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(8);
        int row = 0;

        addDetailRow(detailsGrid, row++, "Doctor:", "Dr. " + record.getDoctorSurname());
        addDetailRow(detailsGrid, row++, "Diagnosis:", record.getDiagnosis());
        addDetailRow(detailsGrid, row++, "Treatment:", record.getTreatment());
        addDetailRow(detailsGrid, row++, "Prescription:", record.getPrescription());

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

        recordBox.getChildren().addAll(titleLabel, detailsGrid);

        return recordBox;
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold;");

        Label valueNode = new Label(value != null && !value.isEmpty() ? value : "N/A");

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private void mapBedComponents() {
        System.out.println("Mapping bed components for department: " + departmentName);
        bedPaneMap.clear();
        String dept = (departmentName != null) ? departmentName : "General";
        if (departmentName.equals("Cardiology")) {
            System.out.println("Setting up Cardiology bed mapping");
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
        }
        else if (departmentName.equals("Pediatrics")) {
            System.out.println("Setting up Pediatrics bed mapping");
            bedPaneMap.put(13, bedPane);
            bedPaneMap.put(14, bedPane1);
            bedPaneMap.put(15, bedPane2);
            bedPaneMap.put(16, bedPane3);
            bedPaneMap.put(17, bedPane11);
            bedPaneMap.put(18, bedPane12);
            bedPaneMap.put(19, bedPane21);
            bedPaneMap.put(20, bedPane111);
            bedPaneMap.put(21, bedPane13);
            bedPaneMap.put(22, bedPane112);
            bedPaneMap.put(23, bedPane121);
            bedPaneMap.put(24, bedPane1111);
        }
        else { // Default to General
            System.out.println("Setting up General Medicine bed mapping");
            bedPaneMap.put(25, bedPane);
            bedPaneMap.put(26, bedPane1);
            bedPaneMap.put(27, bedPane2);
            bedPaneMap.put(28, bedPane3);
            bedPaneMap.put(29, bedPane11);
            bedPaneMap.put(30, bedPane12);
            bedPaneMap.put(31, bedPane21);
            bedPaneMap.put(32, bedPane111);
            bedPaneMap.put(33, bedPane13);
            bedPaneMap.put(34, bedPane112);
            bedPaneMap.put(35, bedPane121);
            bedPaneMap.put(36, bedPane1111);
        }

        System.out.println("Mapped " + bedPaneMap.size() + " bed components for " + dept + " department");

        for (Map.Entry<Integer, StackPane> entry : bedPaneMap.entrySet()) {
            final Integer bedIndex = entry.getKey();
            StackPane bedPane = entry.getValue();
            bedPane.setCursor(Cursor.HAND);
            bedPane.setPickOnBounds(true);

            bedPane.setOnMouseClicked(e -> {
                Label bedIdLabel = (Label) bedPane.lookup(".bed-id-label");
                String bedIdText = bedIdLabel.getText();
                int bedId = -1;
                try {
                    if (bedIdText.startsWith("Bed ")) {
                        bedId = Integer.parseInt(bedIdText.substring(4).trim());
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("Could not parse bed ID from label: " + bedIdText);
                }

                System.out.println("Clicked on bed with ID: " + bedId);
                if (bedId > 0 && bedsMap.containsKey(bedId)) {
                    if (selectedBed != null) {
                        int prevBedId = selectedBed.getBedId();
                        if (bedPaneMap.containsKey(prevBedId)) {
                            bedPaneMap.get(prevBedId).setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.3)));
                            bedPaneMap.get(prevBedId).setStyle("");
                        }
                    }

                    selectedBed = bedsMap.get(bedId);
                    if (selectedBed != null) {
                        System.out.println("Selected bed: " + selectedBed.getBedId() + ", Status: " + selectedBed.getStatus());
                        bedPane.setEffect(new DropShadow(15, Color.DODGERBLUE));
                        bedPane.setStyle("-fx-border-color: #4080c0; -fx-border-width: 3;");

                        // Force button visibility again to be sure
                        if (assign_patient_button != null) {
                            assign_patient_button.setVisible(true);
                        }

                        if (discharge_patient_button != null) {
                            discharge_patient_button.setVisible(true);
                        }

                        updateBedActionButtons();
                    }
                } else {
                    System.out.println("No bed data found for ID " + bedId);
                }
            });
        }
    }

    private void updateBedActionButtons() {
        System.out.println("Updating bed action buttons");

        if (assign_patient_button == null || discharge_patient_button == null) {
            System.out.println("Bed action buttons not initialized yet");
            return;
        }

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

    private void loadAndDisplayBeds() {
        System.out.println("Starting to load bed data...");
        new Thread(() -> {
            try {

                Bed.setDepartment(departmentName);
                System.out.println("Fetching beds from server...");
                List<Bed> beds = bedService.getAllBeds();
                System.out.println("Received " + beds.size() + " beds from server");

                for (Bed bed : beds) {
                    System.out.println("Server bed: " + bed.getBedId() + ", Status: " + bed.getStatus());
                }
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


    private void updateBedDisplay(List<Bed> beds) {
        System.out.println("Updating bed display with " + beds.size() + " beds");


        bedsMap.clear();


        for (Map.Entry<Integer, StackPane> entry : bedPaneMap.entrySet()) {
            StackPane pane = entry.getValue();


            pane.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.3))); // Reset to default shadow
            pane.setStyle("");

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

        for (Bed bed : beds) {

            bedsMap.put(bed.getBedId(), bed);
            System.out.println("Stored bed " + bed.getBedId() + " in bedsMap");
            if (bedPaneMap.containsKey(bed.getBedId())) {
                StackPane bedPane = bedPaneMap.get(bed.getBedId());

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
                    mapBedComponents();
                    loadAndDisplayBeds();
                } else {
                    UIHelper.showAlert("Error", "Failed to assign patient to bed on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error assigning patient to bed: " + e.getMessage());
            e.printStackTrace();
        }
    }


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



    //Lab tests functions





    // Load lab tests from the server
    private void loadAllLabTests() {
        new Thread(() -> {
            try {
                // Fetch tests assigned to this department
                List<LabTest> assignedTests = labTestService.getAllLabTests();

                // Fetch tests requested by this department
                List<LabTest> requestedTests = LabTest.getRequestedLabTests();

                // Merge lists, avoiding duplicates
                Set<Integer> seenIds = new HashSet<>();
                List<LabTest> combinedTests = new ArrayList<>();

                for (LabTest test : assignedTests) {
                    if (test.getTestId() != null && seenIds.add(test.getTestId())) {
                        combinedTests.add(test);
                    }
                }

                for (LabTest test : requestedTests) {
                    if (test.getTestId() != null && seenIds.add(test.getTestId())) {
                        combinedTests.add(test);
                    }
                }

                // Update UI on JavaFX thread
                Platform.runLater(() -> {
                    labTestsList.clear();
                    labTestsList.addAll(combinedTests);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    UIHelper.showAlert("Error", "Failed to load lab tests: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }


    private void handleRequestLabTest() {
        try {
            Stage stage = (Stage) requestLabTestButton.getScene().getWindow();

            if (currentDoctor == null) {
                UIHelper.showAlert("Error", "Doctor information not available. Please log in again.");
                return;
            }

            String doctorName = currentDoctor.getDoctorName();
            String doctorSurname = currentDoctor.getDoctorSurname();
            int doctorId = currentDoctor.getDoctorId();
            String doctorSpecialty = currentDoctor.getDoctorSpecialty();

            int departmentId = currentDoctor.getDepartmentId();
            String departmentName = this.departmentName;

            LabTest newLabTest = LabTestDialog.showRequestLabTestDialog(
                    stage,
                    doctorName, doctorSurname,
                    doctorId, doctorSpecialty,
                    departmentId, departmentName
            );

            if (newLabTest != null) {
                boolean success = labTestService.requestLabTest(newLabTest);
                if (success) {
                    UIHelper.showAlert("Success", "Lab test requested successfully!");
                    loadAllLabTests();
                } else {
                    UIHelper.showAlert("Error", "Failed to request lab test from the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error requesting lab test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleViewLabTestDetails() {
        LabTest selectedTest = testTable.getSelectionModel().getSelectedItem();

        if (selectedTest == null) {
            UIHelper.showAlert("Selection Required", "Please select a lab test to view details.");
            return;
        }
        Stage stage = (Stage) testTable.getScene().getWindow();
        LabTestDialog.showLabTestDetailsDialog(stage, selectedTest);
    }



    private void handleAssignLabTest() {
        LabTest selectedTest = testTable.getSelectionModel().getSelectedItem();

        if (selectedTest == null) {
            UIHelper.showAlert("Selection Required", "Please select a lab test to assign.");
            return;
        }

        if (selectedTest.getDoctorId() != null && selectedTest.getDoctorId() > 0) {
            UIHelper.showAlert("Already Assigned", "This test already has a doctor assigned.");
            return;
        }

        try {
            Stage stage = (Stage) testTable.getScene().getWindow();

            if (currentDoctor == null) {
                UIHelper.showAlert("Error", "Doctor information not available. Please log in again.");
                return;
            }
            String doctorName = currentDoctor.getDoctorName();
            String doctorSurname = currentDoctor.getDoctorSurname();
            int doctorId = currentDoctor.getDoctorId();

            boolean confirmed = LabTestDialog.showAssignDoctorDialog(
                    stage, selectedTest, doctorId, doctorName, doctorSurname
            );

            if (confirmed) {
                boolean success = labTestService.assignDoctorToTest(selectedTest.getTestId(), doctorId);

                if (success) {
                    UIHelper.showAlert("Success", "You have been assigned to this lab test!");
                    loadAllLabTests();
                } else {
                    UIHelper.showAlert("Error", "Failed to assign doctor to test on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error assigning doctor to test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handle completing a lab test with results
    private void handleCompleteLabTest() {
        LabTest selectedTest = testTable.getSelectionModel().getSelectedItem();

        if (selectedTest == null) {
            UIHelper.showAlert("Selection Required", "Please select a lab test to complete.");
            return;
        }
        if ("Completed".equalsIgnoreCase(selectedTest.getStatus())) {
            UIHelper.showAlert("Already Completed", "This test has already been completed.");
            return;
        }

        if (selectedTest.getDoctorId() == null || selectedTest.getDoctorId() <= 0) {
            UIHelper.showAlert("Not Assigned", "This test needs to be assigned to a doctor before it can be completed.");
            return;
        }

        try {
            Stage stage = (Stage) testTable.getScene().getWindow();
            LabTest completedTest = LabTestDialog.showCompleteLabTestDialog(stage, selectedTest);

            if (completedTest != null) {
                boolean success = labTestService.completeLabTest(
                        completedTest.getTestId(),
                        completedTest.getResult(),
                        completedTest.getNotes()
                );

                if (success) {
                    UIHelper.showAlert("Success", "Lab test completed successfully!");
                    loadAllLabTests(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to complete lab test on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error completing lab test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDeleteLabTest() {
        LabTest selectedTest = testTable.getSelectionModel().getSelectedItem();

        if (selectedTest == null) {
            UIHelper.showAlert("Selection Required", "Please select a lab test to delete.");
            return;
        }

        try {
            Stage stage = (Stage) testTable.getScene().getWindow();
            boolean confirmed = LabTestDialog.showDeleteConfirmationDialog(stage, selectedTest);

            if (confirmed) {
                boolean success = labTestService.deleteLabTest(selectedTest.getTestId());

                if (success) {
                    UIHelper.showAlert("Success", "Lab test deleted successfully!");
                    loadAllLabTests(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to delete lab test from the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error deleting lab test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //AI DIAGNOSTICS

    private void checkAIServerStatus() {
        new Thread(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(aiServerUrl + "/health"))
                        .timeout(java.time.Duration.ofSeconds(5))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(
                        request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(response.body());
                    boolean modelLoaded = (boolean) json.get("model_loaded");

                    Platform.runLater(() -> {
                        if (modelLoaded) {
                            serverStatusLabel.setText("AI Server Status: Connected");
                            serverStatusLabel.setStyle("-fx-text-fill: green;");
                        } else {
                            serverStatusLabel.setText("AI Server Status: Error - Model not loaded");
                            serverStatusLabel.setStyle("-fx-text-fill: orange;");
                        }
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    serverStatusLabel.setText("AI Server Status: Disconnected");
                    serverStatusLabel.setStyle("-fx-text-fill: red;");

                    // Schedule another check in 5 seconds
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            checkAIServerStatus();
                        }
                    }, 5000);
                });
            }
        }).start();
    }

    // Handle selecting an X-ray image
    private void handleSelectXrayImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select X-ray Image");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(selectXrayButton.getScene().getWindow());
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                xrayImageView.setImage(image);

                noImageLabel.setVisible(false);

                analyzeXrayButton.setDisable(false);

                selectedXrayFile = selectedFile;
                diagnosisResultLabel.setText("Analysis Results: None");
                confidenceChart.getData().clear();
            } catch (Exception e) {
                UIHelper.showAlert("Error", "Could not load the selected image: " + e.getMessage());
            }
        }
    }

    private void handleAnalyzeXray() {
        if (selectedXrayFile == null) {
            return;
        }

        analyzeXrayButton.setDisable(true);
        analyzeXrayButton.setText("Analyzing...");

        new Thread(() -> {
            try {
                byte[] fileBytes = Files.readAllBytes(selectedXrayFile.toPath());

                String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write((twoHyphens + boundary + lineEnd).getBytes());
                baos.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" +
                        selectedXrayFile.getName() + "\"" + lineEnd).getBytes());
                baos.write(("Content-Type: image/jpeg" + lineEnd).getBytes());
                baos.write(lineEnd.getBytes());
                baos.write(fileBytes);
                baos.write((lineEnd + twoHyphens + boundary + twoHyphens + lineEnd).getBytes());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(aiServerUrl + "/predict"))
                        .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                        .POST(HttpRequest.BodyPublishers.ofByteArray(baos.toByteArray()))
                        .build();

                HttpResponse<String> response = httpClient.send(
                        request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {

                    JSONParser parser = new JSONParser();
                    JSONObject result = (JSONObject) parser.parse(response.body());
                    String prediction = (String) result.get("prediction");
                    double confidence = (double) result.get("confidence");

                    String formattedPrediction = prediction.replace("_", " ");
                    formattedPrediction = formattedPrediction.substring(0, 1).toUpperCase() +
                            formattedPrediction.substring(1);


                    JSONObject probabilities = (JSONObject) result.get("probabilities");
                    System.out.println("All classification categories: " + probabilities.keySet());
                    String finalFormattedPrediction = formattedPrediction;
                    Platform.runLater(() -> {
                        try {
                            diagnosisResultLabel.setText(String.format(
                                    "Analysis Results: %s (Confidence: %.1f%%)",
                                    finalFormattedPrediction, confidence * 100));

                            confidenceChart.getData().clear();
                            XYChart.Series<String, Number> series = new XYChart.Series<>();
                            series.setName("Confidence");
                            for (Object key : probabilities.keySet()) {
                                String className = (String) key;
                                double prob = (double) probabilities.get(className);
                                String formattedClass = formatClassName(className);
                                series.getData().add(new XYChart.Data<>(formattedClass, prob));
                            }

                            confidenceChart.setTitle("X-ray Analysis Confidence Levels");

                            confidenceChart.setVerticalGridLinesVisible(true);
                            confidenceChart.setHorizontalGridLinesVisible(true);
                            confidenceChart.setAnimated(false);


                            confidenceChart.getData().add(series);
                            for (XYChart.Data<String, Number> item : series.getData()) {
                                Node bar = item.getNode();
                                if (bar != null) {
                                    if (item.getXValue().equalsIgnoreCase(finalFormattedPrediction)) {
                                        bar.setStyle("-fx-bar-fill: #4080c0; -fx-background-color: #4080c0;");
                                        bar.getStyleClass().add("selected-bar");
                                    } else {
                                        bar.setStyle("-fx-bar-fill: #a0a0a0; -fx-background-color: #a0a0a0;");
                                    }
                                    Tooltip tooltip = new Tooltip(String.format("%s: %.1f%%",
                                            item.getXValue(), item.getYValue().doubleValue() * 100));
                                    Tooltip.install(bar, tooltip);
                                } else {
                                    System.out.println("Warning: Bar node is null for " + item.getXValue());
                                }
                            }
                            confidenceChart.setVisible(true);
                            confidenceChart.setLegendVisible(false);
                            confidenceChart.layout();
                        } catch (Exception e) {
                            System.err.println("Error updating chart: " + e.getMessage());
                            e.printStackTrace();
                        } finally {
                            analyzeXrayButton.setDisable(false);
                            analyzeXrayButton.setText("Analyze X-ray");
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        UIHelper.showAlert("Error", "Failed to analyze X-ray: " + response.body());
                        analyzeXrayButton.setDisable(false);
                        analyzeXrayButton.setText("Analyze X-ray");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    UIHelper.showAlert("Error", "Error analyzing X-ray: " + e.getMessage());
                    analyzeXrayButton.setDisable(false);
                    analyzeXrayButton.setText("Analyze X-ray");
                });
            }
        }).start();
    }

    // Helper method to format class names
    private String formatClassName(String className) {
        if (className == null || className.isEmpty()) {
            return "";
        }
        String formatted = className.replace("_", " ");
        String[] words = formatted.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return result.toString().trim();
    }


    private void loadAllPrescriptions() {
        System.out.println("Loading prescriptions from server...");
        new Thread(() -> {
            try {

                System.out.println("Using department: " + departmentName + " for prescriptions");
                List<Prescription> serverPrescriptions = prescriptionService.getAllPrescriptions();
                System.out.println("Received " + (serverPrescriptions != null ? serverPrescriptions.size() : 0) + " prescriptions from server");

                if (serverPrescriptions != null && !serverPrescriptions.isEmpty()) {
                    for (Prescription p : serverPrescriptions) {
                        System.out.println("Prescription: ID=" + p.getPrescriptionId() +
                                ", Patient=" + p.getPatientFirstName() + " " + p.getPatientLastName() +
                                ", Medication=" + p.getMedicationName());
                    }
                } else {
                    System.out.println("No prescriptions found or list is null");
                }

                Platform.runLater(() -> {
                    prescriptionsList.clear();
                    if (serverPrescriptions != null) {
                        prescriptionsList.addAll(serverPrescriptions);
                    }
                    System.out.println("Prescription table updated with " + prescriptionsList.size() + " items");
                });
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    UIHelper.showAlert("Connection Error", "Failed to fetch prescriptions from server: " + e.getMessage());
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    UIHelper.showAlert("Error", "Unexpected error loading prescriptions: " + e.getMessage());
                });
            }
        }).start();
    }


    private void handleAddPrescription() {
        try {
            // Get a doctor from the current department if needed
            if (currentDoctor == null) {
                try {
                    if (doctorService == null) {
                        doctorService = new DoctorService(departmentName);
                    }
                    List<Doctor> doctors = doctorService.getAllDoctors();
                    if (!doctors.isEmpty()) {
                        currentDoctor = doctors.get(0);
                        System.out.println("Using doctor: " +
                                currentDoctor.getDoctorName() + " " + currentDoctor.getDoctorSurname());
                    } else {
                        UIHelper.showAlert("Error", "No doctors found in department: " + departmentName);
                        return;
                    }
                } catch (Exception e) {
                    System.err.println("Error finding a doctor: " + e.getMessage());
                    UIHelper.showAlert("Error", "Could not find a doctor for this department.");
                    return;
                }
            }

            Stage stage = (Stage) add_prescription_button.getScene().getWindow();
            Prescription newPrescription = PrescriptionDialog.showAddPrescriptionDialog(stage);

            if (newPrescription != null) {
                newPrescription.setDoctorId(currentDoctor.getDoctorId());
                newPrescription.setDoctorName(currentDoctor.getDoctorName());
                newPrescription.setDoctorSurname(currentDoctor.getDoctorSurname());

                newPrescription.setDepartmentId(currentDoctor.getDepartmentId());
                newPrescription.setDepartmentName(departmentName);

                if (newPrescription.getDatePrescribed() == null) {
                    newPrescription.setDatePrescribed(LocalDateTime.now());
                }

                System.out.println("Adding prescription with: " +
                        "Doctor=" + newPrescription.getDoctorName() + " " + newPrescription.getDoctorSurname() +
                        ", Department=" + newPrescription.getDepartmentName());

                boolean success = prescriptionService.addPrescription(newPrescription);
                if (success) {
                    UIHelper.showAlert("Success", "Prescription added successfully!");
                    loadAllPrescriptions(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to add prescription to the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error adding prescription: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleModifyPrescription() {
        Prescription selectedPrescription = PrescriptionsTable.getSelectionModel().getSelectedItem();

        if (selectedPrescription == null) {
            UIHelper.showAlert("Selection Required", "Please select a prescription to modify.");
            return;
        }

        try {
            Stage stage = (Stage) modify_prescription_button.getScene().getWindow();
            Prescription updatedPrescription = PrescriptionDialog.showUpdatePrescriptionDialog(stage, selectedPrescription);

            if (updatedPrescription != null) {
                // Keep the existing doctor information when modifying
                if (selectedPrescription.getDoctorId() != null) {
                    updatedPrescription.setDoctorId(selectedPrescription.getDoctorId());
                    updatedPrescription.setDoctorName(selectedPrescription.getDoctorName());
                    updatedPrescription.setDoctorSurname(selectedPrescription.getDoctorSurname());
                } else if (currentDoctor != null) {
                    updatedPrescription.setDoctorId(currentDoctor.getDoctorId());
                    updatedPrescription.setDoctorName(currentDoctor.getDoctorName());
                    updatedPrescription.setDoctorSurname(currentDoctor.getDoctorSurname());
                } else {
                    // Try to find a doctor in the current department
                    try {
                        if (doctorService == null) {
                            doctorService = new DoctorService(departmentName);
                        }
                        List<Doctor> doctors = doctorService.getAllDoctors();
                        if (!doctors.isEmpty()) {
                            Doctor doctor = doctors.get(0);
                            updatedPrescription.setDoctorId(doctor.getDoctorId());
                            updatedPrescription.setDoctorName(doctor.getDoctorName());
                            updatedPrescription.setDoctorSurname(doctor.getDoctorSurname());
                        } else {
                            UIHelper.showAlert("Error", "No doctors found in department: " + departmentName);
                            return;
                        }
                    } catch (Exception e) {
                        UIHelper.showAlert("Error", "Could not find a doctor for this department.");
                        return;
                    }
                }

                if (selectedPrescription.getDepartmentId() != null) {
                    updatedPrescription.setDepartmentId(selectedPrescription.getDepartmentId());
                    updatedPrescription.setDepartmentName(selectedPrescription.getDepartmentName());
                } else if (currentDoctor != null && currentDoctor.getDepartmentId() != null) {
                    updatedPrescription.setDepartmentId(currentDoctor.getDepartmentId());
                    updatedPrescription.setDepartmentName(departmentName);
                } else {
                    updatedPrescription.setDepartmentId(updatedPrescription.getDoctorId());
                    updatedPrescription.setDepartmentName(departmentName);
                }

                // Ensure date is set
                if (updatedPrescription.getDatePrescribed() == null) {
                    updatedPrescription.setDatePrescribed(LocalDateTime.now());
                }

                System.out.println("Updating prescription with: " +
                        "Doctor=" + updatedPrescription.getDoctorName() + " " + updatedPrescription.getDoctorSurname() +
                        ", Department=" + updatedPrescription.getDepartmentName());

                boolean success = prescriptionService.updatePrescription(updatedPrescription);
                if (success) {
                    UIHelper.showAlert("Success", "Prescription updated successfully!");
                    loadAllPrescriptions();
                } else {
                    UIHelper.showAlert("Error", "Failed to update prescription on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error updating prescription: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDeletePrescription() {
        Prescription selectedPrescription = PrescriptionsTable.getSelectionModel().getSelectedItem();

        if (selectedPrescription == null) {
            UIHelper.showAlert("Selection Required", "Please select a prescription to delete.");
            return;
        }

        try {
            Stage stage = (Stage) delete_prescription_button.getScene().getWindow();
            boolean confirmed = PrescriptionDialog.showDeleteConfirmationDialog(stage, selectedPrescription);

            if (confirmed) {
                boolean success = prescriptionService.deletePrescription(selectedPrescription.getPrescriptionId());
                if (success) {
                    UIHelper.showAlert("Success", "Prescription deleted successfully!");
                    loadAllPrescriptions();
                } else {
                    UIHelper.showAlert("Error", "Failed to delete prescription from the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error deleting prescription: " + e.getMessage());
            e.printStackTrace();
        }


    }


}




