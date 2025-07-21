package com.inteliMedExpress.classes.Employees;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Nurse {
    private Integer nurseId;
    private String nurseName;
    private String nurseSurname;
    private String nursePhone;
    private String nurseEmail;
    private String nurseAddress;
    private Integer nurseAge;
    private String gender;
    private String nurseSpecialty;
    private Integer departmentId;
    private String departmentName;
    private LocalDateTime createdAt;

    // Default constructor
    public Nurse() {
    }

    // Parameterized constructor
    public Nurse(Integer nurseId, String nurseName, String nurseSurname, String nursePhone,
                 String nurseEmail, String nurseAddress, Integer nurseAge, String gender,
                 String nurseSpecialty, Integer departmentId, String departmentName,
                 LocalDateTime createdAt) {
        this.nurseId = nurseId;
        this.nurseName = nurseName;
        this.nurseSurname = nurseSurname;
        this.nursePhone = nursePhone;
        this.nurseEmail = nurseEmail;
        this.nurseAddress = nurseAddress;
        this.nurseAge = nurseAge;
        this.gender = gender;
        this.nurseSpecialty = nurseSpecialty;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getNurseSurname() {
        return nurseSurname;
    }

    public void setNurseSurname(String nurseSurname) {
        this.nurseSurname = nurseSurname;
    }

    public String getNursePhone() {
        return nursePhone;
    }

    public void setNursePhone(String nursePhone) {
        this.nursePhone = nursePhone;
    }

    public String getNurseEmail() {
        return nurseEmail;
    }

    public void setNurseEmail(String nurseEmail) {
        this.nurseEmail = nurseEmail;
    }

    public String getNurseAddress() {
        return nurseAddress;
    }

    public void setNurseAddress(String nurseAddress) {
        this.nurseAddress = nurseAddress;
    }

    public Integer getNurseAge() {
        return nurseAge;
    }

    public void setNurseAge(Integer nurseAge) {
        this.nurseAge = nurseAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNurseSpecialty() {
        return nurseSpecialty;
    }

    public void setNurseSpecialty(String nurseSpecialty) {
        this.nurseSpecialty = nurseSpecialty;
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
        return nurseName + " " + nurseSurname;
    }

    // Helper method to get a formatted display string
    public String getDisplayString() {
        return nurseId + ": " + nurseName + " " + nurseSurname;
    }

    // Helper method to get a formatted creation date
    public String getFormattedCreatedAt() {
        if (createdAt == null) return "N/A";
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        return "Nurse{" +
                "nurseId=" + nurseId +
                ", nurseName='" + nurseName + '\'' +
                ", nurseSurname='" + nurseSurname + '\'' +
                ", nursePhone='" + nursePhone + '\'' +
                ", nurseEmail='" + nurseEmail + '\'' +
                ", nurseAddress='" + nurseAddress + '\'' +
                ", nurseAge=" + nurseAge +
                ", gender='" + gender + '\'' +
                ", nurseSpecialty='" + nurseSpecialty + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", createdAt=" + (createdAt != null ? createdAt.format(DateTimeFormatter.ISO_DATE_TIME) : null) +
                '}';
    }
}