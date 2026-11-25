package kigali.clinic.rw.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.OfficeService;

@RestController
@RequestMapping(value = {"/api/office", "/api/offices"})
public class OfficeController {

    @Autowired
    private OfficeService offServe;

    
    @PostMapping(value = "/save")
    public ResponseEntity<?> saveOffice(@RequestBody Office office) {

        String returnedMessage = offServe.saveOffice(office);

        if (returnedMessage.equals("Office is saved successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }

    
    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllOffices() {

        return new ResponseEntity<>(
                offServe.getAllOffices(),
                HttpStatus.OK
        );
    }

    
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOfficeById(@PathVariable UUID id) {

        return new ResponseEntity<>(
                offServe.getOfficeById(id),
                HttpStatus.OK
        );
    }

    
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateOffice(
            @PathVariable UUID id,
            @RequestBody Office office) {

        String returnedMessage = offServe.updateOffice(id, office);

        if (returnedMessage.equals("Office is updated successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteOffice(@PathVariable UUID id) {

        String returnedMessage = offServe.deleteOffice(id);

        if (returnedMessage.equals("Office is deleted successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    
    @GetMapping(value = "/busiest")
    public ResponseEntity<?> getBusiestOffice() {

        return new ResponseEntity<>(
                offServe.getBusiestOffice(),
                HttpStatus.OK
        );
    }
}