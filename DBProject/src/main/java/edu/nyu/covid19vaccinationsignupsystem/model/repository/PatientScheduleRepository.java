package edu.nyu.covid19vaccinationsignupsystem.model.repository;

import edu.nyu.covid19vaccinationsignupsystem.model.entity.PatientSchedule;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.embeddedid.PatientScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientScheduleRepository extends JpaRepository<PatientSchedule,PatientScheduleId> {
    @Query(value = "select * from patient_schedule where patient_id = ?;", nativeQuery = true)
    List<PatientSchedule> findPatientScheduleByPatientId (Integer id) ;
}
