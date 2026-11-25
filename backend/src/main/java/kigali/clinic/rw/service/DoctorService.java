package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.repository.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository docRepo;

    
    public String saveDoctor(Doctor doctor) {

        
        if (doctor.getFirstName() == null || doctor.getFirstName().trim().isEmpty()) {
            return "First name is required";
        }

        
        if (doctor.getLastName() == null || doctor.getLastName().trim().isEmpty()) {
            return "Last name is required";
        }

        
        if (doctor.getDateOfBirth() == null) {
            return "Date of birth is required";
        }

        
        if (docRepo.existsByFirstNameAndLastNameAndDateOfBirth(
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getDateOfBirth())) {

            return "A doctor with the same first name, last name, and date of birth already exists";
        }

        
        if (doctor.getOffice() != null &&
                docRepo.existsByOffice(doctor.getOffice())) {

            return "This office already has a doctor";
        }

        
        docRepo.save(doctor);

        return "Doctor is saved successfully";
    }

    
    public List<Doctor> getAllDoctors() {
        return docRepo.findAll();
    }

    
    public Optional<Doctor> getDoctorById(UUID id) {
        return docRepo.findById(id);
    }


    public String updateDoctor(UUID id, Doctor doctor) {

        Optional<Doctor> existingDoctor = docRepo.findById(id);

        if (existingDoctor.isPresent()) {

            Doctor oldDoctor = existingDoctor.get();

            oldDoctor.setFirstName(doctor.getFirstName());
            oldDoctor.setLastName(doctor.getLastName());
            oldDoctor.setDateOfBirth(doctor.getDateOfBirth());

            docRepo.save(oldDoctor);

            return "Doctor is updated successfully";
        }

        return "Doctor not found";
    }

    
    public String deleteDoctor(UUID id) {

        if (docRepo.existsById(id)) {
            docRepo.deleteById(id);
            return "Doctor is deleted successfully";
        }

        return "Doctor not found";
    }

   
    public List<Doctor> getDoctorsBySpecialization(String name) {
        return docRepo.findDoctorsBySpecializationName(name);
    }

    
    public List<Doctor> getDoctorsWithoutOffice() {
        return docRepo.findDoctorsWithoutOffice();
    }
}