package com.inteliMedExpress.classes.Beds;

import java.io.IOException;
import java.util.List;

public class BedService {

    public List<Bed> getAllBeds() throws IOException {
        return Bed.getAllBeds();
    }

    public boolean assignPatientToBed(Bed bed) throws IOException {
        return bed.assignPatient();
    }

    public boolean dischargePatientFromBed(Bed bed) throws IOException {
        return bed.dischargePatient();
    }
}