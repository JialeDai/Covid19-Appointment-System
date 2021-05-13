package edu.nyu.covid19vaccinationsignupsystem.utils;

import edu.nyu.covid19vaccinationsignupsystem.exceptions.PatientNotFoundException;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Appointment;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Patient;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.AppointmentRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.PatientRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.ProviderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Component
public class AssignAssignmentUtils {
    @Resource
    PatientRepository patientRepository;
    @Resource
    AppointmentRepository appointmentRepository;

    /**
     * Get List of appointment information filtered by the appointmentId
     * @param appointmentId the input appointmentId
     * @param inputMap  List of appointment needed to be filtered
     * @return
     */
    private List<Map<String, Object>> filterByAppointment(Integer appointmentId, List<Map<String, Object>> inputMap) {
        List<Map<String, Object>> tmp = new ArrayList<>();
        for (Map<String, Object> each : inputMap) {
            if ((Integer) each.get("appointment_id") == appointmentId) tmp.add(each);
        }
        return tmp;
    }

    /**
     * Return the Set of patientId which has already been assigned an appointment offer
     * @param appointmentList all appointment in the appointment table
     * @return
     */
    private Set<Integer> getAssignedPatientId(List<Appointment> appointmentList) {
        Set<Integer> tmp = new HashSet<>();
        for (Appointment each : appointmentList) {
            if ((Integer) each.getPatientId() != null) tmp.add((Integer) each.getPatientId());
        }
        return tmp;
    }

    /**
     * Scheduled to run at 1:30 am every day to assign appointment
     * to the patients starting with the highest priority.
     * <P> For every appointment in appointment list, assign the those with released status to
     * the patient who is not assigned at this or previous time</P>
     */
    @Scheduled(cron = "0 30 1 ? * *") // 1:30 am every day
    public void assignAppointment() {
        Set<Integer> patientSet = new HashSet<>();
        List<Map<String, Object>> result = appointmentRepository.findAvailablePatientAndAppointment();
        List<Appointment> appointmentList = appointmentRepository.findAll();
        Set<Integer> assignedPatientId = getAssignedPatientId(appointmentList);
        for (Appointment appointment : appointmentList) {
            if (appointment.getStatus().equals("released")) {
                Integer appointmentId = (Integer) appointment.getId();
                List<Map<String, Object>> tmp = filterByAppointment(appointmentId, result);
                for (Map<String, Object> each : tmp) {
                    Integer patientId = (Integer) each.get("patient_id");
                    if (patientSet.add(patientId) && !assignedPatientId.contains(patientId)) {
                        Integer providerId = (Integer) each.get("provider_id");
                        Timestamp startTime = (Timestamp) each.get("start_time");
                        Timestamp endTime = (Timestamp) each.get("end_time");
                        appointmentRepository.save(new Appointment(appointmentId, providerId, startTime, endTime, patientId, "assigned"));
                        Patient patient = patientRepository.findById(patientId).orElseThrow(()->new PatientNotFoundException(patientId));
                        patient.setStatus("scheduled");
                        patientRepository.save(patient);
                        break;
                    }
                }
            }
        }
        System.out.println("===============Auto Assign Appointment=================");
    }
}

