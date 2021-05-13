package edu.nyu.covid19vaccinationsignupsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nyu.covid19vaccinationsignupsystem.exceptions.AppointmentNotFoundException;
import edu.nyu.covid19vaccinationsignupsystem.exceptions.PatientNotFoundException;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Appointment;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.AppointmentLog;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Patient;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.PatientSchedule;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.AppointmentLogRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.AppointmentRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.PatientRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.PatientScheduleRepository;
import edu.nyu.covid19vaccinationsignupsystem.utils.FilePath;
import edu.nyu.covid19vaccinationsignupsystem.utils.TimeUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(path = "/api")
public class PatientController {
    @Resource
    private PatientRepository patientRepository;
    @Resource
    private PatientScheduleRepository patientScheduleRepository;
    @Resource
    private AppointmentRepository appointmentRepository;
    @Resource
    private AppointmentLogRepository appointmentLogRepository;

    @PostMapping(path = "/patients")
    public String newPatient(@RequestBody Patient newPatient) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
//        Boolean isEmailExisted = false;
        if (patientRepository.findPatientByEmail(newPatient.getEmail()) == null) {
            response.put("newPatient", patientRepository.save(newPatient));
            response.put("isExisted", false);
        } else {
            response.put("isExisted", true);
        }
        return new ObjectMapper().writeValueAsString(response);
    }

    @GetMapping(path = "/patients/{id}")
    public Patient one(@PathVariable Integer id) {
        return patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
    }

    @GetMapping(path = "/patients")
    public @ResponseBody
    List<Patient> all() {
        return patientRepository.findAll();
    }

    @PostMapping(path = "/patients/login")
    @ResponseBody
    public String loginCheck(@RequestBody Map<String, String> loginInfo) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Patient patient = patientRepository.findPatientByEmail(loginInfo.get("email"));
            if (patient != null && patient.getPassword().equals(loginInfo.get("password"))) {
                res.put("loginInfo", patient);
                res.put("login_check", true);
                FilePath.getInstance().setUrl(null);
            } else {
                res.put("login_check", false);
            }
        } catch (PatientNotFoundException e) {
            res.put("login_check", false);
        }
        return mapper.writeValueAsString(res);
    }

    @PutMapping(path = "/patients/{id}")
    public Patient updatePatient(@PathVariable Integer id, @RequestBody Map<String, Object> newPatient) throws ParseException {
        Patient oldPatient = patientRepository.findPatientById(id);
        if (newPatient.get("name") != null) oldPatient.setName((String) newPatient.get("name"));
        if (newPatient.get("email") != null) oldPatient.setEmail((String) newPatient.get("email"));
        if (newPatient.get("ssn") != null) oldPatient.setSsn((String) newPatient.get("ssn"));
        if (newPatient.get("birth") != null) {
            Date birth = TimeUtils.parseDate((String) newPatient.get("birth"));
            oldPatient.setBirth(birth);
        }
        if (newPatient.get("address") != null) oldPatient.setAddress((String) newPatient.get("address"));
        if (newPatient.get("phone") != null) oldPatient.setPhone((String) newPatient.get("phone"));
        if (FilePath.getInstance().getUrl() != null) oldPatient.setAdditionalInfo(FilePath.getInstance().getUrl());
        if (newPatient.get("password") != null) oldPatient.setPassword((String) newPatient.get("password"));
        return patientRepository.save(oldPatient);
    }

    @GetMapping(path = "/patients/schedule/{id}")
    @ResponseBody
    public List<PatientSchedule> findAllTimeSlotById(@PathVariable Integer id) {
        return patientScheduleRepository.findPatientScheduleByPatientId(id);
    }

    @PostMapping(path = "/patients/schedule/add")
    @ResponseBody
    public PatientSchedule addSchedule(@RequestBody PatientSchedule schedule) {
        return patientScheduleRepository.save(schedule);
    }

    @PostMapping(path = "/patients/schedule/delete")
    @ResponseBody
    public void deleteSchedule(@RequestBody PatientSchedule schedule) throws JsonProcessingException {
        patientScheduleRepository.delete(schedule);
    }

    @GetMapping(path = "/patients/appointment/current/{id}")
    @ResponseBody
    public List<Map<String, Object>> findCurrentAppointmentByPatientId(@PathVariable Integer id) {
        List<Map<String, Object>> res = patientRepository.findCurrentAppointmentByPatientId(id);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Map<String, Object> each : res) {
            Map<String, Object> one = new HashMap<>(each);
            one.put("start_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("start_time")));
            one.put("end_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("end_time")));
            response.add(one);
        }
        return response;
    }

    @PatchMapping(path = "/patients/appointment/current/cancel")
    public void patientCancelAppointment(@RequestBody Map<String, String> updateContent) {
        Integer id = Integer.parseInt(updateContent.get("id"));
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
        appointment.setStatus("released");
        appointment.setPatientId(null);
        appointmentRepository.save(appointment);
    }

    @PatchMapping(path = "/patients/appointment/current/accept")
    public void patientAcceptAppointment(@RequestBody Map<String, String> updateContent) {
        Integer id = Integer.parseInt(updateContent.get("id"));
        Integer patientId = Integer.parseInt(updateContent.get("patient_id"));
        appointmentRepository.releaseAppointment(patientId);
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
        appointment.setStatus("accepted");
        appointment.setPatientId(patientId);
        appointmentRepository.save(appointment);
    }

    @PatchMapping(path = "/patients/priority")
    @ResponseBody
    public Patient changePriority(@RequestBody Map<String, String> updateInfo) {
        Integer id = Integer.parseInt(updateInfo.get("id"));
        Patient patient = patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException(id));
        Integer priority = Integer.parseInt(updateInfo.get("priority"));
        patient.setPriority(priority);
        return patientRepository.save(patient);
    }

    @PatchMapping(path = "/patients/appointment/current/decline")
    public void patientDeclineAppointment(@RequestBody Map<String, String> updateContent) {
        Integer id = Integer.parseInt(updateContent.get("id"));
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
        appointment.setStatus("released");
        appointment.setPatientId(null);
        appointmentRepository.save(appointment);
    }

    @ResponseBody
    @GetMapping(path = "/patients/appointment/log/{id}")
    public List<Map<String, Object>> findAllAppointmentLogByPatientId(@PathVariable Integer id) {
        List<Map<String, Object>> res = appointmentLogRepository.findAppointmentLogByPatientId(id);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Map<String, Object> each : res) {
            Map<String, Object> one = new HashMap<>(each);
            one.put("start_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("start_time")));
            one.put("end_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("end_time")));
            one.put("log_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("log_time")));
            response.add(one);
        }
        return response;
    }

    @ResponseBody
    @PostMapping(path = "/patients/appointment/log")
    public AppointmentLog addOne(@RequestBody Map<String, String> appointment) {
//        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeZone(timeZone);
//        Timestamp logTime = new Timestamp(calendar.getTimeInMillis());
        AppointmentLog appointmentToAdd = new AppointmentLog(Integer.parseInt(appointment.get("id")), appointment.get("action"), TimeUtils.now(), Integer.parseInt(appointment.get("patient_id")));
        return appointmentLogRepository.save(appointmentToAdd);
    }
}
