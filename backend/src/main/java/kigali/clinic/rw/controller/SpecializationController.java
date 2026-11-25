package kigali.clinic.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.AssignDoctorRequest;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.SpecializationService;

@RestController
@RequestMapping({"/api/specialization", "/api/specializations"})
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    @PostMapping("/save")
    public String saveSpecialization(
            @RequestBody Specialization specialization) {

        return specializationService.saveSpecialization(
                specialization);
    }

    @GetMapping("/all")
    public List<Specialization> getAllSpecializations() {

        return specializationService.getAllSpecializations();
    }

    @GetMapping("/{id}")
    public Specialization getSpecializationById(
            @PathVariable UUID id) {

        return specializationService
                .getSpecializationById(id);
    }


    @PutMapping("/update/{id}")
    public String updateSpecialization(
            @PathVariable UUID id,
            @RequestBody Specialization specialization) {

        return specializationService.updateSpecialization(
                id,
                specialization);
    }


    @DeleteMapping("/delete/{id}")
    public String deleteSpecialization(
            @PathVariable UUID id) {

        return specializationService.deleteSpecialization(id);
    }

    
    @PostMapping("/assign-doctor")
    public String assignDoctorToSpecialization(
            @RequestBody AssignDoctorRequest request) {

        return specializationService.assignDoctorToSpecialization(
                request.getDoctorId(),
                request.getSpecializationId());
    }

    
    @GetMapping("/unused")
    public List<Specialization> getUnusedSpecializations() {

        return specializationService.getUnusedSpecializations();
    }
}