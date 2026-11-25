package kigali.clinic.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    List<Doctor> findByFirstName(String fName);

    List<Doctor> findByLastName(String lName);

    Optional<Doctor> findByOffice(Office office);

    Optional<Doctor> findByFirstNameAndLastNameAndDateOfBirth(
            String firstName,
            String lastName,
            java.sql.Date dateOfBirth
    );

    boolean existsByOffice(Office office);

    boolean existsByFirstNameAndLastNameAndDateOfBirth(
        String firstName,
        String lastName,
        Date dateOfBirth
);

    @Query("""
        SELECT DISTINCT d
        FROM Doctor d
        JOIN d.specializations s
        WHERE LOWER(s.name) = LOWER(:name)
        """)
    List<Doctor> findDoctorsBySpecializationName(@Param("name") String name);

    @Query("""
        SELECT d
        FROM Doctor d
        WHERE d.office IS NULL
        ORDER BY d.lastName ASC
        """)
    List<Doctor> findDoctorsWithoutOffice();
}