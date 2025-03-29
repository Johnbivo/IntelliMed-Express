module com.inteliMedExpress {
    // Java platform modules
    requires java.base;
    requires java.desktop;
    requires java.sql;

    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Web functionality
    requires java.net.http;
    requires json.simple;

    // Export packages to make them accessible
    exports com.inteliMedExpress;  // Export the root package where Main is located
    exports com.inteliMedExpress.controllers;
    exports com.inteliMedExpress.classes;


    // Open packages for FXML and reflection
    opens com.inteliMedExpress to javafx.fxml, javafx.graphics;
    opens com.inteliMedExpress.controllers to javafx.fxml;
    opens com.inteliMedExpress.classes to java.base;
}