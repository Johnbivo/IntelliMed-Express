package com.inteliMedExpress.classes.Employees;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Doctor {
    private Integer doctorId;
    private String doctorName;
    private String doctorSurname;
    private String doctorPhone;
    private String doctorEmail;
    private String doctorAddress;
    private Integer doctorAge;
    private String gender;
    private String doctorSpecialty;
    private Integer departmentId;
    private String departmentName;
    private LocalDateTime createdAt;

    // Default constructor
    public Doctor() {
    }

    // Parameterized constructor
    public Doctor(Integer doctorId, String doctorName, String doctorSurname, String doctorPhone,
                  String doctorEmail, String doctorAddress, Integer doctorAge, String gender,
                  String doctorSpecialty, Integer departmentId, String departmentName,
                  LocalDateTime createdAt) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorSurname = doctorSurname;
        this.doctorPhone = doctorPhone;
        this.doctorEmail = doctorEmail;
        this.doctorAddress = doctorAddress;
        this.doctorAge = doctorAge;
        this.gender = gender;
        this.doctorSpecialty = doctorSpecialty;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorSurname() {
        return doctorSurname;
    }

    public void setDoctorSurname(String doctorSurname) {
        this.doctorSurname = doctorSurname;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDoctorAddress() {
        return doctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        this.doctorAddress = doctorAddress;
    }

    public Integer getDoctorAge() {
        return doctorAge;
    }

    public void setDoctorAge(Integer doctorAge) {
        this.doctorAge = doctorAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDoctorSpecialty() {
        return doctorSpecialty;
    }

    public void setDoctorSpecialty(String doctorSpecialty) {
        this.doctorSpecialty = doctorSpecialty;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper method to get the full name
    public String getFullName() {
        return doctorName + " " + doctorSurname;
    }

    // Helper method to get a formatted display string
    public String getDisplayString() {
        return doctorId + ": " + doctorName + " " + doctorSurname;
    }

    // Helper method to get a formatted creation date
    public String getFormattedCreatedAt() {
        if (createdAt == null) return "N/A";
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", doctorSurname='" + doctorSurname + '\'' +
                ", doctorPhone='" + doctorPhone + '\'' +
                ", doctorEmail='" + doctorEmail + '\'' +
                ", doctorAddress='" + doctorAddress + '\'' +
                ", doctorAge=" + doctorAge +
                ", gender='" + gender + '\'' +
                ", doctorSpecialty='" + doctorSpecialty + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", createdAt=" + (createdAt != null ? createdAt.format(DateTimeFormatter.ISO_DATE_TIME) : null) +
                '}';
    }
}