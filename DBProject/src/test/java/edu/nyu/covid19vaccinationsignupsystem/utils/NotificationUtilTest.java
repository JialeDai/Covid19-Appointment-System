package edu.nyu.covid19vaccinationsignupsystem.utils;

import com.google.common.collect.Lists;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Patient;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import org.apache.catalina.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotificationUtilTest {
    @Autowired
    public EmailService emailService;

    @Test
    public void testSendEmail() throws UnsupportedEncodingException, IOException {
        String fromEmail = PropertiesUtil.getProperty("spring.mail.username");
        final Email email = DefaultEmail.builder()
                .from(new InternetAddress(fromEmail, "Reservation Confirmed"))
                .to(Lists.newArrayList(new InternetAddress("jd4678@nyu.edu", "Jiale Dai")))
                .subject("Testing email")
                .body("Testing body ...")
                .encoding("UTF-8").build();
        emailService.send(email);
    }
}