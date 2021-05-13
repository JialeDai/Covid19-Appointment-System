package edu.nyu.covid19vaccinationsignupsystem.model.repository;

import edu.nyu.covid19vaccinationsignupsystem.model.entity.AppointmentLog;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.embeddedid.AppointmentLogId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface AppointmentLogRepository extends JpaRepository<AppointmentLog, AppointmentLogId> {
    @Query(value = "select provider.name as provider, patient.name as patient,log_time,start_time, end_time, action from appointment_log join appointment on appointment_log.appointment_id = appointment.id join patient on appointment_log.patient_id = patient.id join provider on appointment.provider_id = provider.id where appointment_log.patient_id = ? order by log_time asc;",nativeQuery = true)
    List<Map<String,Object>> findAppointmentLogByPatientId(Integer id);
}
