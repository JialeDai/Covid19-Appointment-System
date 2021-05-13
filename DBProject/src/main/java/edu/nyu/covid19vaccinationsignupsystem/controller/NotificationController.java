package edu.nyu.covid19vaccinationsignupsystem.controller;

import com.google.common.collect.Lists;
import edu.nyu.covid19vaccinationsignupsystem.exceptions.PatientNotFoundException;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Appointment;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Patient;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Provider;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.AppointmentRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.PatientRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.ProviderRepository;
import edu.nyu.covid19vaccinationsignupsystem.utils.PropertiesUtil;
import edu.nyu.covid19vaccinationsignupsystem.utils.TimeUtils;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.nio.file.ProviderNotFoundException;
import java.util.Map;

@RestController
@RequestMapping(path = "/notification")
public class NotificationController {
    @Resource
    public EmailService emailService;
    @Resource
    public PatientRepository patientRepository;
    @Resource
    public AppointmentRepository appointmentRepository;
    @Resource
    public ProviderRepository providerRepository;

    @PostMapping(path = "/patient/confirmation")
    public void sendConfirmation(@RequestBody Map<String, String> patientInfo) throws IOException {
        String fromEmail = PropertiesUtil.getProperty("spring.mail.username");
        String fromName = "Reservation Confirmed";
        Patient patient = patientRepository.findById(Integer.parseInt(patientInfo.get("patient_id"))).orElseThrow(()->new PatientNotFoundException(patientInfo.get("patient_id")));
        String toEmail = patient.getEmail();
        String toName = patient.getName();
        Appointment appointment = appointmentRepository.findById(Integer.parseInt(patientInfo.get("id"))).orElseThrow(()->new RuntimeException());
        Provider provider = providerRepository.findById(appointment.getProviderId()).orElseThrow(()->new ProviderNotFoundException());
        String emailContent = "Dear "+patient.getName()+":\n\tYour Reservation is Confirmed!\n========================Reservation Info==========================\nAddress: "+
                provider.getAddress()+ "\nstart time:\t"+ TimeUtils.parseTimeStamp(appointment.getStartTime()).toString() +"\nend time:\t"+TimeUtils.parseTimeStamp(appointment.getEndTime()).toString();
        final Email email = DefaultEmail.builder()
                .from(new InternetAddress(fromEmail, fromName))
                .to(Lists.newArrayList(new InternetAddress(toEmail, toName)))
                .subject("Covid19 Vaccination Reversion")
                .body(emailContent)
                .encoding("UTF-8").build();
        emailService.send(email);
    }
}
