package kigali.clinic.rw.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping({"/api/appointment", "/api/appointments"})
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    
    @PostMapping("/save")
    public ResponseEntity<?> saveAppointment(
            @RequestBody Appointment appointment,
            @RequestParam UUID patientId,
            @RequestParam UUID doctorId) {

        String returnedMessage = appointmentService.saveAppointment(
                appointment,
                patientId,
                doctorId
        );

        if (returnedMessage.equals("Doctor is already booked on that date")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    
    @GetMapping("/all")
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    
    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable UUID id) {
        return appointmentService.getAppointmentById(id);
    }

    
    @PutMapping("/update/{id}")
    public String updateAppointment(
            @PathVariable UUID id,
            @RequestBody Appointment appointment) {

        return appointmentService.updateAppointment(id, appointment);
    }

    
    @DeleteMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable UUID id) {
        return appointmentService.deleteAppointment(id);
    }

    
    @GetMapping("/pending")
    public List<Appointment> getPendingAppointments() {
        return appointmentService.getPendingAppointments();
    }

    
    @GetMapping("/pending/doctor/{doctorId}")
    public List<Appointment> getPendingAppointmentsByDoctor(
            @PathVariable UUID doctorId) {

        return appointmentService.getPendingAppointmentsByDoctor(
                doctorId
        );
    }

   
@GetMapping("/busiest-doctor")
public Map<String, Object> getBusiestDoctor() {
    return appointmentService.getBusiestDoctor();
}

    
    @GetMapping("/by-status")
    public List<Appointment> getAppointmentsByStatus(
            @RequestParam AppointmentStatus status) {

        return appointmentService.getAppointmentsByStatus(status);
    }

    
    @GetMapping("/between")
    public List<Appointment> getAppointmentsBetween(
            @RequestParam String start,
            @RequestParam String end) {

        Date startDate = Date.valueOf(LocalDate.parse(start));
        Date endDate = Date.valueOf(LocalDate.parse(end));

        return appointmentService.getAppointmentsBetween(startDate, endDate);
    }

    
    @GetMapping("/stats/by-status")
    public List<Object[]> getAppointmentCountByStatus() {
        return appointmentService.getAppointmentCountByStatus();
    }

    
    @PatchMapping("/cancel-day")
    public String cancelDay(
            @RequestParam UUID doctorId,
            @RequestParam String date) {

        return appointmentService.cancelDoctorDay(
                doctorId,
                Date.valueOf(LocalDate.parse(date))
        );
    }

    
    @GetMapping("/page")
    public Page<Appointment> getAppointmentsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "appointmentDate,desc") String sort) {

        String[] parts = sort.split(",");
        String field = parts[0];
        Sort.Direction direction =
                (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, field));

        return appointmentService.getAppointmentsPage(pageable);
    }

    
    @DeleteMapping("/cancelled-before")
    public String deleteCancelledBefore(@RequestParam String date) {

        return appointmentService.deleteCancelledBefore(
                Date.valueOf(LocalDate.parse(date))
        );
    }
}