package com.inteliMedExpress.controllers;

import com.inteliMedExpress.classes.UIHelper;
import com.inteliMedExpress.classes.labTests.LabTest;
import com.inteliMedExpress.classes.labTests.LabTestDialog;
import com.inteliMedExpress.classes.labTests.LabTestService;
import com.inteliMedExpress.classes.Employees.Doctor;
import com.inteliMedExpress.classes.Employees.DoctorService;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ContentDisplay;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RadiologyController {

    @FXML
    private Label doctorName;

    @FXML
    private ToolBar toolbar;

    @FXML
    private Button imaging_tests_button;

    @FXML
    private Button logout_button;

    @FXML
    private Label radiology_title;

    // Imaging Tests Table
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

    // Imaging Tests Buttons
    @FXML
    private Button assign_doctor_button;
    @FXML
    private Button add_results_button;
    @FXML
    private Button view_details_button;
    @FXML
    private Button refresh_button;

    private ObservableList<LabTest> imagingTestsList = FXCollections.observableArrayList();
    private LabTestService labTestService;
    private Doctor currentDoctor;
    private DoctorService doctorService;
    private String loggedInDoctorName;
    private String departmentName = "Radiology";

    private ImageView staticRefreshIcon;
    private ImageView animatedRefreshIcon;

    @FXML
    public void initialize() {
        try {
            System.out.println("Radiology Controller initializing...");

            // Initialize services
            labTestService = new LabTestService(departmentName);
            doctorService = new DoctorService(departmentName);

            // Set up navigation buttons
            setupNavigationButtons();

            // Set up the imaging tests table
            setupImagingTestTable();

            // Load imaging tests
            loadAllImagingTests();

            // Initialize refresh button
            initializeRefreshButton();

            // Set the default view to imaging tests
            showImagingTestsView();

        } catch (Exception e) {
            System.err.println("Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupNavigationButtons() {
        imaging_tests_button.setOnAction(event -> showImagingTestsView());
        imaging_tests_button.setStyle("-fx-background-color: #e6f2ff; -fx-text-fill: #0052cc; -fx-font-weight: bold;");
    }

    private void showImagingTestsView() {
        testTable.setVisible(true);
        assign_doctor_button.setVisible(true);
        add_results_button.setVisible(true);
        view_details_button.setVisible(true);
        refresh_button.setVisible(true);
    }

    private void setupImagingTestTable() {
        // Configure imaging test table
        testTable.setEditable(false);
        TableView.TableViewSelectionModel<LabTest> selectionModel = testTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        testTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Configure imaging test table columns
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

        // Date formatter for order date column
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

        // Date formatter for completion date column
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

        // Color code results based on status
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

        testTable.setItems(imagingTestsList);

        // Setup imaging test action buttons
        assign_doctor_button.setOnAction(event -> handleAssignImagingTest());
        add_results_button.setOnAction(event -> handleCompleteImagingTest());
        view_details_button.setOnAction(event -> handleViewImagingTestDetails());

        // Add context menu to rows
        testTable.setRowFactory(tv -> {
            TableRow<LabTest> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem viewItem = new MenuItem("View Details");
            viewItem.setOnAction(event -> handleViewImagingTestDetails());

            MenuItem assignItem = new MenuItem("Assign Doctor");
            assignItem.setOnAction(event -> handleAssignImagingTest());

            MenuItem completeItem = new MenuItem("Add Results");
            completeItem.setOnAction(event -> handleCompleteImagingTest());

            contextMenu.getItems().addAll(viewItem, assignItem, completeItem);

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

            // Double-click to view details
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    handleViewImagingTestDetails();
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
                loadAllImagingTests();
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
            doctorName.setText("Welcome, Doctor");
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

    // Load imaging tests from the server
    private void loadAllImagingTests() {
        new Thread(() -> {
            try {
                // Fetch tests assigned to this department
                List<LabTest> assignedTests = labTestService.getAllLabTests();

                // Fetch tests requested by this department
                List<LabTest> requestedTests = LabTest.getRequestedLabTests();

                // Combine lists and update UI
                Platform.runLater(() -> {
                    imagingTestsList.clear();
                    imagingTestsList.addAll(assignedTests);
                    imagingTestsList.addAll(requestedTests);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    UIHelper.showAlert("Error", "Failed to load imaging tests: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    // Handle button actions

    private void handleViewImagingTestDetails() {
        LabTest selectedTest = testTable.getSelectionModel().getSelectedItem();

        if (selectedTest == null) {
            UIHelper.showAlert("Selection Required", "Please select an imaging test to view details.");
            return;
        }

        Stage stage = (Stage) testTable.getScene().getWindow();
        LabTestDialog.showLabTestDetailsDialog(stage, selectedTest);
    }

    private void handleAssignImagingTest() {
        LabTest selectedTest = testTable.getSelectionModel().getSelectedItem();

        if (selectedTest == null) {
            UIHelper.showAlert("Selection Required", "Please select an imaging test to assign.");
            return;
        }

        if (selectedTest.getDoctorId() != null && selectedTest.getDoctorId() > 0) {
            UIHelper.showAlert("Already Assigned", "This test already has a doctor assigned.");
            return;
        }

        try {
            Stage stage = (Stage) assign_doctor_button.getScene().getWindow();

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
                    UIHelper.showAlert("Success", "You have been assigned to this imaging test!");
                    loadAllImagingTests();
                } else {
                    UIHelper.showAlert("Error", "Failed to assign doctor to test on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error assigning doctor to test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleCompleteImagingTest() {
        LabTest selectedTest = testTable.getSelectionModel().getSelectedItem();

        if (selectedTest == null) {
            UIHelper.showAlert("Selection Required", "Please select an imaging test to complete.");
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
            Stage stage = (Stage) add_results_button.getScene().getWindow();
            LabTest completedTest = LabTestDialog.showCompleteLabTestDialog(stage, selectedTest);

            if (completedTest != null) {
                boolean success = labTestService.completeLabTest(
                        completedTest.getTestId(),
                        completedTest.getResult(),
                        completedTest.getNotes()
                );

                if (success) {
                    UIHelper.showAlert("Success", "Imaging test completed successfully!");
                    loadAllImagingTests();
                } else {
                    UIHelper.showAlert("Error", "Failed to complete imaging test on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error completing imaging test: " + e.getMessage());
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