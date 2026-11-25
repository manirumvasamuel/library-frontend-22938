package kigali.clinic.rw.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class AppointmentService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository) {

        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    
    public String saveAppointment(
            Appointment appointment,
            UUID patientId,
            UUID doctorId) {

        Optional<Doctor> getDoctor =
                doctorRepository.findById(doctorId);

        Optional<Patient> getPatient =
                patientRepository.findById(patientId);

        if (getPatient.isEmpty() || getDoctor.isEmpty()) {
            return "That patient or doctor is not available";
        }

        
        if (appointmentRepository.existsByDoctorAndAppointmentDateAndStatusNot(
                getDoctor.get(),
                appointment.getAppointmentDate(),
                AppointmentStatus.CANCELLED)) {

            return "Doctor is already booked on that date";
        }

        appointment.setDoctor(getDoctor.get());
        appointment.setPatient(getPatient.get());

        appointmentRepository.save(appointment);

        return "Appointment is saved successfully";
    }

    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    
    public Appointment getAppointmentById(UUID id) {

        Optional<Appointment> appointment =
                appointmentRepository.findById(id);

        return appointment.orElse(null);
    }

   
    public String updateAppointment(
            UUID id,
            Appointment appointment) {

        Optional<Appointment> existingAppointment =
                appointmentRepository.findById(id);

        if (existingAppointment.isPresent()) {

            Appointment oldAppointment =
                    existingAppointment.get();

            oldAppointment.setAppointmentDate(
                    appointment.getAppointmentDate());

            oldAppointment.setReason(
                    appointment.getReason());

            oldAppointment.setStatus(
                    appointment.getStatus());

            appointmentRepository.save(oldAppointment);

            return "Appointment is updated successfully";
        }

        return "Appointment not found";
    }

   
    public String deleteAppointment(UUID id) {

        if (appointmentRepository.existsById(id)) {

            appointmentRepository.deleteById(id);

            return "Appointment is deleted successfully";
        }

        return "Appointment not found";
    }

    
    public List<Appointment> getPendingAppointments() {

        Date today = new Date(System.currentTimeMillis());

        return appointmentRepository.findAppointmentsByStatusAndDateBefore(
                AppointmentStatus.PENDING,
                today
        );
    }

   
    public List<Appointment> getPendingAppointmentsByDoctor(
            UUID doctorId) {

        if (!doctorRepository.existsById(doctorId)) {
            return List.of();
        }

        return appointmentRepository.findPendingAppointmentsByDoctor(
                doctorId,
                AppointmentStatus.PENDING
        );
    }

    
    public Map<String, Object> getBusiestDoctor() {

        List<Object[]> results =
                appointmentRepository.findDoctorsByAppointmentCount();

        if (results.isEmpty()) {
            return null;
        }

        Object[] result = results.get(0);

        Doctor doctor = (Doctor) result[0];
        Long numberOfAppointments = (Long) result[1];

        Map<String, Object> response = new HashMap<>();

        response.put(
                "doctor",
                doctor.getFirstName() + " " + doctor.getLastName()
        );

        response.put(
                "numberOfAppointments",
                numberOfAppointments
        );

        return response;
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> getAppointmentsBetween(Date start, Date end) {
        return appointmentRepository
                .findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public List<Object[]> getAppointmentCountByStatus() {
        return appointmentRepository.countAppointmentsByStatus();
    }

    
    @Transactional
    public String cancelDoctorDay(UUID doctorId, Date date) {

        int updated = appointmentRepository.cancelDoctorAppointmentsOnDate(
                doctorId,
                date,
                AppointmentStatus.CANCELLED,
                AppointmentStatus.COMPLETED
        );

        return updated + " appointments cancelled";
    }

    
    public Page<Appointment> getAppointmentsPage(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    
    @Transactional
    public String deleteCancelledBefore(Date date) {

        int deleted = appointmentRepository.deleteByStatusAndDateBefore(
                AppointmentStatus.CANCELLED,
                date
        );

        return deleted + " appointments deleted";
    }
}