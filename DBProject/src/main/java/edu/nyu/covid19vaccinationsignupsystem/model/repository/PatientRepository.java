package edu.nyu.covid19vaccinationsignupsystem.model.repository;

import edu.nyu.covid19vaccinationsignupsystem.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface PatientRepository extends JpaRepository<Patient,Integer> {
    Patient findPatientByEmail(String email);
    Patient findPatientById(Integer id);

    @Query(value = "select appointment.status as status, appointment.id as id, provider.name as provider, start_time, end_time, distance, p.name as patient from appointment join provider on appointment.provider_id = provider.id join patient p on appointment.patient_id = p.id join distance d on p.id = d.patient_id and provider.id = d.provider_id where (appointment.status = \"assigned\" or appointment.status = \"accepted\") and appointment.patient_id = ?;",nativeQuery = true)
    List<Map<String,Object>> findCurrentAppointmentByPatientId (Integer id);
}
