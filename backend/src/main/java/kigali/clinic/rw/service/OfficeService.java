package kigali.clinic.rw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.OfficeRepository;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository offRepo;

    @Autowired
    private AppointmentRepository appointmentRepository;

    
    public String saveOffice(Office office) {
        offRepo.save(office);
        return "Office is saved successfully";
    }

    
    public List<Office> getAllOffices() {
        return offRepo.findAll();
    }

    
    public Optional<Office> getOfficeById(UUID id) {
        return offRepo.findById(id);
    }

    
    public String updateOffice(UUID id, Office office) {

        Optional<Office> existingOffice = offRepo.findById(id);

        if (existingOffice.isPresent()) {

            Office oldOffice = existingOffice.get();

            oldOffice.setName(office.getName());
            oldOffice.setOfficeNumber(office.getOfficeNumber());

            offRepo.save(oldOffice);

            return "Office is updated successfully";
        }

        return "Office not found";
    }

    
    public String deleteOffice(UUID id) {

        if (offRepo.existsById(id)) {
            offRepo.deleteById(id);
            return "Office is deleted successfully";
        }

        return "Office not found";
    }

    
    public Object getBusiestOffice() {

        List<Object[]> rows = appointmentRepository.findBusiestOffice(PageRequest.of(0, 1));

        if (rows.isEmpty()) {
            return "No appointments yet";
        }

        Object[] row = rows.get(0);

        Map<String, Object> response = new HashMap<>();
        response.put("officeName", row[0]);
        response.put("officeNumber", row[1]);
        response.put("appointmentCount", row[2]);

        return response;
    }
}