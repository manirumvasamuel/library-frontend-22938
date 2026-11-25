package kigali.clinic.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kigali.clinic.rw.domain.Specialization;

public interface SpecializationRepository
        extends JpaRepository<Specialization, UUID> {

    // B3: specializations that no doctor holds (tests the collection, no COUNT)
    @Query("""
        SELECT s
        FROM Specialization s
        WHERE s.doctors IS EMPTY
        """)
    List<Specialization> findUnusedSpecializations();
}