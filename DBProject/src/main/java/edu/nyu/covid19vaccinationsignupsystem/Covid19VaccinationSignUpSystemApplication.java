package edu.nyu.covid19vaccinationsignupsystem;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
@EnableEmailTools
@ComponentScan(basePackages = {"edu.nyu.covid19vaccinationsignupsystem.model.entity", "edu.nyu.covid19vaccinationsignupsystem.model.repository", "edu.nyu.covid19vaccinationsignupsystem.controller", "edu.nyu.covid19vaccinationsignupsystem.model.entity.embeddedid", "edu.nyu.covid19vaccinationsignupsystem.utils"})
public class Covid19VaccinationSignUpSystemApplication {

    @SpringBootApplication
    public class Application {
        @PostConstruct
        void started() {
            //TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
            TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
            //TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Covid19VaccinationSignUpSystemApplication.class, args);
    }

}
