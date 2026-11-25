package kigali.clinic.rw.domain;

import java.util.UUID;

public class AssignDoctorRequest {

    private UUID doctorId;
    private UUID specializationId;

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public UUID getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(UUID specializationId) {
        this.specializationId = specializationId;
    }
}