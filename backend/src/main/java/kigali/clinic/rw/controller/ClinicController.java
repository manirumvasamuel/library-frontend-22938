package kigali.clinic.rw.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;

@RestController
@RequestMapping("/api/clinic")
public class ClinicController {

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/all")
    public Map<String, Object> getAllClinicData() {

        Map<String, Object> data = new HashMap<>();

        data.put("offices", officeRepository.findAll());
        data.put("doctors", doctorRepository.findAll());
        data.put("patients", patientRepository.findAll());

        return data;
    }
}