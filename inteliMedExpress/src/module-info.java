module com.inteliMedExpress {
    // Java platform modules
    requires java.base;
    requires java.desktop;
    requires java.sql;

    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // Web functionality
    requires java.net.http;
    requires json.simple;

    // Export packages to make them accessible
    exports com.inteliMedExpress;  // Export the root package where Main is located
    exports com.inteliMedExpress.controllers;
    exports com.inteliMedExpress.classes;
    exports com.inteliMedExpress.classes.medicalRecords;
    exports com.inteliMedExpress.classes.appointments;
    exports com.inteliMedExpress.classes.patients;
    exports com.inteliMedExpress.classes.Beds;
    exports com.inteliMedExpress.classes.Employees;
    exports com.inteliMedExpress.classes.labTests;
    // Open packages for FXML and reflection
    opens com.inteliMedExpress to javafx.fxml, javafx.graphics;
    opens com.inteliMedExpress.controllers to javafx.fxml, javafx.graphics;
    opens com.inteliMedExpress.classes to java.base, javafx.fxml, javafx.graphics;
    opens com.inteliMedExpress.classes.patients to java.base, javafx.fxml, javafx.graphics;
    opens com.inteliMedExpress.classes.appointments to java.base, javafx.fxml, javafx.graphics;
    opens com.inteliMedExpress.classes.medicalRecords to javafx.base, javafx.fxml, javafx.graphics;
    opens com.inteliMedExpress.classes.labTests to java.base, javafx.fxml, javafx.graphics;
    opens com.inteliMedExpress.classes.Beds to java.base, javafx.fxml, javafx.graphics;
    opens com.inteliMedExpress.classes.Employees to java.base, javafx.fxml, javafx.graphics;
}