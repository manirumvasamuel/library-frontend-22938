package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patRepo;

    @Autowired
    private DoctorRepository docRepo;

    
    public String savePatient(Patient patient) {
        patRepo.save(patient);
        return "Patient is saved successfully";
    }

    
    public List<Patient> getAllPatients() {
        return patRepo.findAll();
    }

   
    public Optional<Patient> getPatientById(UUID id) {
        return patRepo.findById(id);
    }

    
    public String updatePatient(UUID id, Patient patient) {

        Optional<Patient> existingPatient = patRepo.findById(id);

        if (existingPatient.isPresent()) {

            Patient oldPatient = existingPatient.get();

            oldPatient.setFirstName(patient.getFirstName());
            oldPatient.setLastName(patient.getLastName());
            oldPatient.setDateOfBirth(patient.getDateOfBirth());

            patRepo.save(oldPatient);

            return "Patient is updated successfully";
        }

        return "Patient not found";
    }

   
    public String deletePatient(UUID id) {

        if (patRepo.existsById(id)) {
            patRepo.deleteById(id);
            return "Patient is deleted successfully";
        }

        return "Patient not found";
    }

    
    public List<Patient> getPatientsByLastName(String lastName) {
        return patRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    
    public Optional<List<Patient>> getPatientsOfDoctor(UUID doctorId) {

        if (!docRepo.existsById(doctorId)) {
            return Optional.empty();
        }

        return Optional.of(patRepo.findPatientsOfDoctor(doctorId));
    }

    
    public List<Patient> getFrequentPatients(long min) {
        return patRepo.findFrequentPatients(min);
    }
}