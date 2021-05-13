package edu.nyu.covid19vaccinationsignupsystem.model.repository;

import edu.nyu.covid19vaccinationsignupsystem.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Transactional
    @Modifying
    @Query(value = "update appointment set patient_id = null, status = 'release' where patient_id = ?", nativeQuery = true)
    void releaseAppointment(Integer id);

    @Query(value = "select appointment.id as id, provider.name as provider,patient.name as patient, start_time,end_time,appointment.status as status from appointment join provider on appointment.provider_id = provider.id left outer join patient on appointment.patient_id = patient.id where appointment.provider_id = ? and start_time > now();", nativeQuery = true)
    List<Map<String, Object>> findAppointmentProviderPatientByProviderId(Integer id);

    @Query(value = "select appointment.id as id, provider.name as provider,patient.name as patient, start_time,end_time,appointment.status as status from appointment join provider on appointment.provider_id = provider.id left outer join patient on appointment.patient_id = patient.id where appointment.provider_id = ? and appointment.status = 'assigned' and start_time > now();", nativeQuery = true)
    List<Map<String, Object>> findAssignedAppointmentProviderPatientByProviderId(Integer id);

    @Query(value = "select appointment.id as id, provider.name as provider,patient.name as patient, start_time,end_time,appointment.status as status from appointment join provider on appointment.provider_id = provider.id left outer join patient on appointment.patient_id = patient.id where appointment.provider_id = ? and appointment.status = 'released' and start_time > now();", nativeQuery = true)
    List<Map<String, Object>> findReleasedAppointmentProviderPatientByProviderId(Integer id);

    @Query(value = "select appointment.id as id, provider.name as provider,patient.name as patient, start_time,end_time,appointment.status as status from appointment join provider on appointment.provider_id = provider.id left outer join patient on appointment.patient_id = patient.id where appointment.provider_id = ? and start_time > now() order by appointment.start_time;", nativeQuery = true)
    List<Map<String, Object>> findRecentReleasedAppointmentProviderPatientByProviderId(Integer id);

    @Query(value = "with tmp as (select appointment.provider_id as provider_id, appointment.id as appointment_id, appointment.start_time as start_time, appointment.end_time as end_time from patient_schedule,appointment\n" +
            "where appointment.patient_id is null and dayofweek(appointment.start_time)=day_of_the_week\n" +
            "  and ((hour(appointment.start_time)<=patient_schedule.start_time and hour(appointment.end_time)>=patient_schedule.start_time + 4)\n" +
            "      or (hour(appointment.start_time)>=patient_schedule.start_time and hour(appointment.start_time)<=patient_schedule.start_time+4)\n" +
            "      or (hour(appointment.end_time)>=patient_schedule.start_time and hour(appointment.end_time)<=patient_schedule.start_time+4)\n" +
            "      or (hour(appointment.start_time)>=patient_schedule.start_time and hour(appointment.start_time)<=patient_schedule.start_time+4)))\n" +
            "select tmp.*, distance.distance, priority, max_distance, patient.id as patient_id from tmp natural join distance join patient on patient_id = patient.id  where distance <= max_distance order by priority;",nativeQuery = true)
    List<Map<String, Object>> findAvailablePatientAndAppointment();

    @Query(value = "select appointment.id as id, provider.name as provider,patient.name as patient, start_time,end_time,appointment.status as status from appointment join provider on appointment.provider_id = provider.id left outer join patient on appointment.patient_id = patient.id where appointment.provider_id = ? and end_time <= now() and appointment.status != 'released';", nativeQuery = true)
    List<Map<String, Object>> findExpiredAppointmentProviderPatientByProviderId(Integer id);
}
