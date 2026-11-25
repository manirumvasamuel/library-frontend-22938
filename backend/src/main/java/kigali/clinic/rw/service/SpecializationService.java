package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;
    private final DoctorRepository doctorRepository;

    public SpecializationService(
            SpecializationRepository specializationRepository,
            DoctorRepository doctorRepository) {

        this.specializationRepository = specializationRepository;
        this.doctorRepository = doctorRepository;
    }

    public String saveSpecialization(Specialization specialization) {

        specializationRepository.save(specialization);

        return "Specialization is saved successfully";
    }


    public List<Specialization> getAllSpecializations() {

        return specializationRepository.findAll();
    }


    public Specialization getSpecializationById(UUID id) {

        Optional<Specialization> specialization =
                specializationRepository.findById(id);

        return specialization.orElse(null);
    }


    public String updateSpecialization(
            UUID id,
            Specialization specialization) {

        Optional<Specialization> existing =
                specializationRepository.findById(id);

        if (existing.isPresent()) {

            Specialization oldSpecialization =
                    existing.get();

            oldSpecialization.setName(
                    specialization.getName());

            specializationRepository.save(oldSpecialization);

            return "Specialization is updated successfully";
        }

        return "Specialization not found";
    }


    public String deleteSpecialization(UUID id) {

        if (specializationRepository.existsById(id)) {

            specializationRepository.deleteById(id);

            return "Specialization is deleted successfully";
        }

        return "Specialization not found";
    }


    @Transactional
    public String assignDoctorToSpecialization(
            UUID doctorId,
            UUID specializationId) {

        Optional<Doctor> doctor =
                doctorRepository.findById(doctorId);

        Optional<Specialization> specialization =
                specializationRepository.findById(specializationId);

        if (doctor.isEmpty()) {
            return "Doctor not found";
        }

        if (specialization.isEmpty()) {
            return "Specialization not found";
        }

        Specialization specializationObject =
                specialization.get();

        Doctor doctorObject =
                doctor.get();

        if (!specializationObject.getDoctors()
                .contains(doctorObject)) {

            specializationObject.getDoctors()
                    .add(doctorObject);

            specializationRepository.save(
                    specializationObject);
        }

        return "Doctor assigned to specialization successfully";
    }

    public List<Specialization> getUnusedSpecializations() {
        return specializationRepository.findUnusedSpecializations();
    }
}