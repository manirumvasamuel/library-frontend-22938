package kigali.clinic.rw.controller;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping(value = {"/api/doctor", "/api/doctors"})
public class DoctorController {

    @Autowired
    private DoctorService docServe;

   
    @PostMapping(value = "/save")
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor) {

        String returnedMessage = docServe.saveDoctor(doctor);

        if (returnedMessage.equals("Doctor is saved successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }

   
    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllDoctors() {

        return new ResponseEntity<>(
                docServe.getAllDoctors(),
                HttpStatus.OK
        );
    }

  
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id) {

        return new ResponseEntity<>(
                docServe.getDoctorById(id),
                HttpStatus.OK
        );
    }

   
    @GetMapping(value = "/{id}/specializations")
    public ResponseEntity<?> getDoctorSpecializations(@PathVariable UUID id) {

        Optional<Doctor> doctor = docServe.getDoctorById(id);

        if (doctor.isEmpty()) {
            return new ResponseEntity<>(
                    "Doctor not found",
                    HttpStatus.NOT_FOUND
            );
        }

        Doctor d = doctor.get();

        Map<String, Object> response = new HashMap<>();

        response.put(
                "doctor",
                d.getFirstName() + " " + d.getLastName()
        );

        response.put(
                "specializations",
                d.getSpecializations()
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable UUID id,
            @RequestBody Doctor doctor) {

        String returnedMessage = docServe.updateDoctor(id, doctor);

        if (returnedMessage.equals("Doctor is updated successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id) {

        String returnedMessage = docServe.deleteDoctor(id);

        if (returnedMessage.equals("Doctor is deleted successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    
    @GetMapping(value = "/by-specialization")
    public ResponseEntity<?> getDoctorsBySpecialization(@RequestParam String name) {

        return new ResponseEntity<>(
                docServe.getDoctorsBySpecialization(name),
                HttpStatus.OK
        );
    }

    
    @GetMapping(value = "/without-office")
    public ResponseEntity<?> getDoctorsWithoutOffice() {

        return new ResponseEntity<>(
                docServe.getDoctorsWithoutOffice(),
                HttpStatus.OK
        );
    }
}