package edu.nyu.covid19vaccinationsignupsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nyu.covid19vaccinationsignupsystem.exceptions.PatientNotFoundException;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Appointment;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.AppointmentLog;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Patient;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Provider;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.AppointmentLogRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.AppointmentRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.PatientRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.ProviderRepository;
import edu.nyu.covid19vaccinationsignupsystem.utils.TimeUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.file.ProviderNotFoundException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(path = "/api")
public class ProviderController {
    @Resource
    ProviderRepository providerRepository;
    @Resource
    AppointmentRepository appointmentRepository;
    @Resource
    PatientRepository patientRepository;
    @Resource
    AppointmentLogRepository appointmentLogRepository;

    @PostMapping(path = "/providers/login")
    @ResponseBody
    public String loginCheck(@RequestBody Map<String, String> loginInfo) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>();
        try {
            Provider provider = providerRepository.findProviderByPhone(loginInfo.get("phone"));
            if (provider != null && provider.getPassword().equals(loginInfo.get("password"))) {
                res.put("loginInfo", provider);
                res.put("login_check", true);
            } else {
                res.put("login_check", false);
            }
        } catch (ProviderNotFoundException e) {
            res.put("login_check", false);
        }
        return new ObjectMapper().writeValueAsString(res);
    }

    @GetMapping(path = "/providers/{id}")
    @ResponseBody
    public Provider one(@PathVariable Integer id) {
        return providerRepository.findById(id).orElseThrow(() -> new ProviderNotFoundException());
    }

    @PutMapping(path = "/providers/{id}")
    public Provider updateProvider(@PathVariable Integer id, @RequestBody Map<String, Object> newProvider) {
        Provider oldProvider = providerRepository.findById(id).orElseThrow(() -> new ProviderNotFoundException());
        if (newProvider.get("name") != null) oldProvider.setName((String) newProvider.get("name"));
        if (newProvider.get("address") != null) oldProvider.setAddress((String) newProvider.get("address"));
        if (newProvider.get("phone") != null) oldProvider.setPhone((String) newProvider.get("phone"));
        if (newProvider.get("password") != null) oldProvider.setPassword((String) newProvider.get("password"));
        return providerRepository.save(oldProvider);

    }

    @PostMapping(path = "/providers/appointment")
    @ResponseBody
    public Appointment uploadAppointment(@RequestBody Map<String, String> appointmentInfo) throws ParseException {
        Appointment appointment = new Appointment();
        appointment.setProviderId((Integer.parseInt(appointmentInfo.get("id"))));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date startTime = dateFormat.parse((String) appointmentInfo.get("date") + " " + (String) appointmentInfo.get("startTime"));
        Date endTime = dateFormat.parse((String) appointmentInfo.get("date") + " " + (String) appointmentInfo.get("endTime"));
        appointment.setStartTime(new Timestamp(startTime.getTime()));
        appointment.setEndTime(new Timestamp(endTime.getTime()));
        return appointmentRepository.save(appointment);
    }

    @PatchMapping(path = "/providers/appointment")
    @ResponseBody
    public Appointment updateAppointment(@RequestBody Map<String, String> updateInfo) {
        Integer id = Integer.parseInt(updateInfo.get("id"));
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException());
        String status = updateInfo.get("status");
        appointment.setStatus(status);
        Integer patientId = appointment.getPatientId();
        if (status.equals("absent")) {
            Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException(patientId));
            patient.setStatus("waiting");
            patientRepository.save(patient);
            appointment.setPatientId(null);
            appointment.setStatus("released");
//            AppointmentLog appointmentLog = new AppointmentLog(id, "absent", TimeUtils.now(), patientId);
//            appointmentLogRepository.save(appointmentLog);
        }
        AppointmentLog appointmentLog = new AppointmentLog(id, status, TimeUtils.now(), patientId);
        appointmentLogRepository.save(appointmentLog);
        return appointmentRepository.save(appointment);
    }

    @GetMapping(path = "/providers/appointment/{id}")
    @ResponseBody
    public List<Map<String, Object>> findCurrentAppointmentByProviderId(@PathVariable Integer id) {
        List<Map<String, Object>> res = appointmentRepository.findAppointmentProviderPatientByProviderId(id);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Map<String, Object> each : res) {
            Map<String, Object> one = new HashMap<>(each);
            one.put("start_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("start_time")));
            one.put("end_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("end_time")));
            response.add(one);
        }
        return response;
    }

    @GetMapping(path = "/providers/appointment/assigned/{id}")
    @ResponseBody
    public List<Map<String, Object>> funCurrentAssignedAppointmentByProviderId(@PathVariable Integer id) {
        List<Map<String, Object>> res = appointmentRepository.findAssignedAppointmentProviderPatientByProviderId(id);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Map<String, Object> each : res) {
            Map<String, Object> one = new HashMap<>(each);
            one.put("start_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("start_time")));
            one.put("end_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("end_time")));
            response.add(one);
        }
        return response;
    }

    @GetMapping(path = "/providers/appointment/released/{id}")
    @ResponseBody
    public List<Map<String, Object>> funCurrentReleasedAppointmentByProviderId(@PathVariable Integer id) {
        List<Map<String, Object>> res = appointmentRepository.findReleasedAppointmentProviderPatientByProviderId(id);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Map<String, Object> each : res) {
            Map<String, Object> one = new HashMap<>(each);
            one.put("start_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("start_time")));
            one.put("end_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("end_time")));
            response.add(one);
        }
        return response;
    }

    @GetMapping(path = "/providers/appointment/expired/{id}")
    @ResponseBody
    public List<Map<String, Object>> findExpiredAppointmentByProviderId(@PathVariable Integer id) {
        List<Map<String, Object>> res = appointmentRepository.findExpiredAppointmentProviderPatientByProviderId(id);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Map<String, Object> each : res) {
            Map<String, Object> one = new HashMap<>(each);
            one.put("start_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("start_time")));
            one.put("end_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("end_time")));
            response.add(one);
        }
        return response;
    }

    @PostMapping(path = "/providers")
    public String newProvider(@RequestBody Provider newProvider) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        Boolean isPhoneExisted = false;
        if (providerRepository.findProviderByPhone(newProvider.getPhone()) == null) {
            response.put("newProvider", providerRepository.save(newProvider));
            response.put("isExisted", false);
        } else {
            response.put("isExisted", true);
        }
        return new ObjectMapper().writeValueAsString(response);
    }

    @GetMapping(path = "/providers/appointment/recent/{id}")
    @ResponseBody
    public List<Map<String, Object>> findRecentlyReleasedAppointmentByProviderId(@PathVariable Integer id) {
        List<Map<String, Object>> res = appointmentRepository.findRecentReleasedAppointmentProviderPatientByProviderId(id);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Map<String, Object> each : res) {
            Map<String, Object> one = new HashMap<>(each);
            one.put("start_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("start_time")));
            one.put("end_time", new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format((java.sql.Timestamp) one.get("end_time")));
            response.add(one);
        }
        return response;
    }
}
