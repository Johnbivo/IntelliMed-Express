package com.inteliMedExpress.classes.appointments;


import java.io.IOException;
import java.util.List;

public class AppointmentService {


    public List<Appointment> getAllPatients() throws IOException {
        return Appointment.getAllAppointments();
    }



    public boolean addPatient(Appointment appointment) throws IOException {
        return appointment.addToServer();
    }

    public boolean updatePatient(Appointment appointment) throws IOException {
        return appointment.updateOnServer();
    }

    public boolean deletePatient(int appointmentId) throws IOException {
        return Appointment.deleteAppointment(appointmentId);
    }
}
