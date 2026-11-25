package kigali.clinic.rw.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("""
        SELECT a FROM Appointment a
        WHERE a.doctor.id = :doctorId
        AND a.status = :status
        ORDER BY a.appointmentDate ASC
        """)
    List<Appointment> findPendingAppointmentsByDoctor(
            @Param("doctorId") UUID doctorId,
            @Param("status") AppointmentStatus status
    );

    @Query("""
        SELECT a FROM Appointment a
        WHERE a.status = :stat
        AND a.appointmentDate < :today
        """)
    List<Appointment> findAppointmentsByStatusAndDateBefore(
            @Param("stat") AppointmentStatus stat,
            @Param("today") Date today
    );

    @Query("""
        SELECT a.doctor, COUNT(a)
        FROM Appointment a
        GROUP BY a.doctor
        ORDER BY COUNT(a) DESC
        """)
    List<Object[]> findDoctorsByAppointmentCount();


    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(Date start, Date end);

    boolean existsByDoctorAndAppointmentDateAndStatusNot(
            Doctor doctor,
            Date appointmentDate,
            AppointmentStatus status
    );

    @Query("""
        SELECT a.status, COUNT(a)
        FROM Appointment a
        GROUP BY a.status
        """)
    List<Object[]> countAppointmentsByStatus();

    @Query("""
        SELECT o.name, o.officeNumber, COUNT(a)
        FROM Appointment a
        JOIN a.doctor d
        JOIN d.office o
        GROUP BY o.id, o.name, o.officeNumber
        ORDER BY COUNT(a) DESC
        """)
    List<Object[]> findBusiestOffice(Pageable pageable);

    // C4: doctor is absent -> cancel everything that day except COMPLETED ones
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Appointment a
        SET a.status = :newStatus
        WHERE a.doctor.id = :doctorId
        AND a.appointmentDate = :date
        AND a.status <> :keepStatus
        """)
    int cancelDoctorAppointmentsOnDate(
            @Param("doctorId") UUID doctorId,
            @Param("date") Date date,
            @Param("newStatus") AppointmentStatus newStatus,
            @Param("keepStatus") AppointmentStatus keepStatus
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        DELETE FROM Appointment a
        WHERE a.status = :status
        AND a.appointmentDate < :date
        """)
    int deleteByStatusAndDateBefore(
            @Param("status") AppointmentStatus status,
            @Param("date") Date date
    );
}