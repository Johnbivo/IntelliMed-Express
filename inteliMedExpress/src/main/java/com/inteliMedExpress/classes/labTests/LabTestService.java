package com.inteliMedExpress.classes.labTests;

import java.io.IOException;
import java.util.List;

public class LabTestService {
    private String departmentName;

    public LabTestService(String departmentName) {
        this.departmentName = departmentName;
        // Set the department for all lab tests
        LabTest.setDepartment(departmentName);
    }

    public LabTestService() {
        // Default to General department if not specified
        this.departmentName = "General";
        // Set the department for all lab tests
        LabTest.setDepartment(this.departmentName);
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
        // Update the department for all lab tests
        LabTest.setDepartment(departmentName);
    }

    // Get all lab tests
    public List<LabTest> getAllLabTests() throws IOException {
        return LabTest.getAllLabTests();
    }

    // Request a new lab test
    public boolean requestLabTest(LabTest labTest) throws IOException {
        return labTest.requestLabTest();
    }

    // Assign a doctor to a lab test
    public boolean assignDoctorToTest(int testId, int doctorId) throws IOException {
        // First, we need to get the test details
        List<LabTest> tests = getAllLabTests();
        for (LabTest test : tests) {
            if (test.getTestId() == testId) {
                return test.assignDoctor(doctorId);
            }
        }
        throw new IOException("Test with ID " + testId + " not found");
    }

    // Complete a lab test with results
    public boolean completeLabTest(int testId, String result, String notes) throws IOException {
        // First, we need to get the test details
        List<LabTest> tests = getAllLabTests();
        for (LabTest test : tests) {
            if (test.getTestId() == testId) {
                return test.completeTest(result, notes);
            }
        }
        throw new IOException("Test with ID " + testId + " not found");
    }

    // Delete a lab test
    public boolean deleteLabTest(int testId) throws IOException {
        return LabTest.deleteLabTest(testId);
    }
}