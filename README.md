# IntelliMed-Express

A comprehensive JavaFX-based Hospital Management System designed for educational purposes.

## ⚠️ Technical Notice

**Important**: This source code may not function properly due to technical issues encountered during the deployment process. These issues are related to development environment configurations and should not be considered a reflection of the project's quality or the developers' capabilities.

**Development Note**: Due to the high learning curve of Maven and JavaFX configuration, some setup mistakes may have been made during development. The Maven approach (Option 1) is recommended, but if it doesn't work, please try the manual setup (Option 2) in the installation section below.

**For Recruiters**: Please note that the technical challenges in this repository are deployment-related and do not represent the actual coding skills or project quality. The production version available at https://bivolaris.com demonstrates the fully functional application.

## Authors
- **Ioannis Bivolaris**
- **Mantsiou Olympia**
- **Revaz Gkelasvili**
- **Ioannis Meladinis**
- **Miltiadis Beratis**

## ⚠️ Important Notice

**This is NOT the production deployed code.** This repository contains the source code for educational and development purposes only.

For the production executable (.exe) file, please visit: **https://bivolaris.com**

## 📋 Description

IntelliMed-Express is a modern hospital management system built with JavaFX that provides comprehensive functionality for managing various aspects of hospital operations. The application features a multi-department architecture supporting different medical specialties.

## 🏥 Supported Departments

- **General Medicine** - Primary care and general medical services
- **Cardiology** - Heart and cardiovascular care
- **Pediatrics** - Children's healthcare
- **Microbiology** - Laboratory and diagnostic services
- **Pharmacology/Pharmacy** - Medication and prescription management
- **Radiology** - Medical imaging and diagnostic services

## 🚀 Features

### Core Functionality
- **User Authentication** - Secure login system with role-based access
- **Patient Management** - Complete patient records, demographics, and medical history
- **Employee Management** - Doctor and nurse profiles with department assignments
- **Appointment Scheduling** - Comprehensive appointment booking and management
- **Medical Records** - Digital patient medical records and history
- **Prescription Management** - Medication prescriptions and tracking
- **Lab Tests** - Laboratory test ordering and results management
- **Bed Management** - Hospital bed allocation and tracking
- **Medication Inventory** - Pharmaceutical stock management

### Technical Features
- **Modern UI** - JavaFX-based responsive interface
- **RESTful API Integration** - Connects to Spring Boot backend server
- **HTTPS Communication** - Secure data transmission
- **Comprehensive Logging** - Detailed application logging system
- **Multi-department Support** - Department-specific data management
- **Real-time Data Synchronization** - Live updates with server

## 🛠️ Prerequisites

Before running this application, you must install the following dependencies:

### 1. Java Development Kit (JDK)
- **Version**: Java 21 or higher
- **Download**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)

### 2. JavaFX
- **Version**: 21.0.2 (included in project dependencies)
- **Installation**: 
  - Download from [OpenJFX](https://openjfx.io/)
  - Or use the Maven dependencies included in the project

### 3. JSON Simple Library
- **Version**: 1.1.1 (included in project dependencies)
- **Purpose**: JSON parsing and manipulation

## 📦 Installation & Setup

### Option 1: Using Maven (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd IntelliMed-Express
   ```

2. **Navigate to the project directory**
   ```bash
   cd inteliMedExpress
   ```

3. **Build the project**
   ```bash
   mvn clean compile
   ```

4. **Run the application**
   ```bash
   mvn javafx:run
   ```

### Option 2: Manual Setup

1. **Install JavaFX SDK**
   - Download JavaFX SDK from [OpenJFX](https://openjfx.io/)
   - Extract to a directory (e.g., `C:\javafx-sdk-21.0.2`)

2. **Set JavaFX environment variables**
   ```bash
   set PATH_TO_FX=C:\javafx-sdk-21.0.2\lib
   ```

3. **Compile the project**
   ```bash
   javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp "lib/*" src/main/java/com/inteliMedExpress/Main.java
   ```

4. **Run the application**
   ```bash
   java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp "lib/*;src/main/java" com.inteliMedExpress.Main
   ```

## 🏗️ Project Structure

```
IntelliMed-Express/
├── inteliMedExpress/                 # Main project directory
│   ├── src/main/java/com/inteliMedExpress/
│   │   ├── classes/                  # Core business logic classes
│   │   │   ├── appointments/         # Appointment management
│   │   │   ├── Beds/                # Bed allocation system
│   │   │   ├── Employees/           # Doctor and Nurse management
│   │   │   ├── labTests/            # Laboratory test management
│   │   │   ├── medicalRecords/      # Patient medical records
│   │   │   ├── Medications/         # Pharmaceutical management
│   │   │   ├── patients/            # Patient management
│   │   │   ├── Prescriptions/       # Prescription system
│   │   │   ├── NavigationManager.java
│   │   │   └── UIHelper.java
│   │   ├── controllers/             # JavaFX controllers
│   │   │   ├── LoginController.java
│   │   │   ├── RegisterController.java
│   │   │   ├── GeneralMedicineDoctorController.java
│   │   │   ├── MicrobiologyController.java
│   │   │   ├── PharmacologyController.java
│   │   │   └── RadiologyController.java
│   │   ├── utils/                   # Utility classes
│   │   │   ├── AppLogger.java       # Logging system
│   │   │   └── HttpsUtil.java       # HTTPS utilities
│   │   └── Main.java                # Application entry point
│   ├── src/main/resources/          # Resources
│   │   ├── fxml/                    # JavaFX FXML files
│   │   ├── css/                     # Stylesheets
│   │   └── images/                  # Application images
│   ├── logs/                        # Application logs
│   └── pom.xml                      # Maven configuration
└── README.md
```

## 🔧 Configuration

### Server Configuration
The application connects to a Spring Boot backend server. The server URL is configured in the Patient class:
```java
private static final String SERVER_BASE_URL = "https://springserver-kl8q.onrender.com/api/";
```

### Logging Configuration
Logs are automatically generated in the `logs/` directory with daily rotation:
- Format: `app_YYYY-MM-DD.log`
- Location: `logs/app_2025-XX-XX.log`

## 🎯 Usage

1. **Launch the application** - The login screen will appear
2. **Login** - Use your department credentials
3. **Navigate** - Use the department-specific interface
4. **Manage Data** - Add, edit, or delete records as needed
5. **Logout** - Use the logout button to exit safely

## 🔒 Security Features

- **HTTPS Communication** - All server communication is encrypted
- **Role-based Access** - Different interfaces for different departments
- **Input Validation** - Comprehensive data validation
- **Secure Authentication** - Encrypted password transmission

## 🐛 Troubleshooting

### Common Issues

1. **JavaFX not found**
   - Ensure JavaFX is properly installed and configured
   - Check that the module path is correctly set

2. **Connection errors**
   - Verify internet connectivity
   - Check if the backend server is running

3. **Compilation errors**
   - Ensure Java 21+ is installed
   - Verify all dependencies are downloaded

### Log Files
Check the log files in the `logs/` directory for detailed error information:
```bash
tail -f logs/app_$(date +%Y-%m-%d).log
```

## 📝 License

This project is for **educational purposes only**. 

## 🤝 Contributing

This is an educational project. For production use, please contact the authors.

## 📞 Support

For technical support or questions:
- Visit: https://bivolaris.com
- Check the log files for detailed error information

## 🔄 Version History

- **v1.0-SNAPSHOT** - Initial release with core hospital management features

---

**Note**: This application is designed for educational demonstration of hospital management systems. It is not intended for production use in actual healthcare environments.
