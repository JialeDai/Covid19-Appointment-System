package edu.nyu.covid19vaccinationsignupsystem.model.entity;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String ssn;
    @Temporal(TemporalType.DATE)
    private Date birth;
    private String address;
    private String phone;
    private String email;
    @Column(name = "additional_info")
    private String additionalInfo;
    @Column(name = "max_distance")
    private Integer maxDistance;
    private String password;
    private Integer priority;
    private String status = "waiting";

    public Patient() {}

    public Patient(String name, String ssn, Date birth, String address, String phone, String email, String additionalInfo, Integer maxDistance, String password, Integer priority, String status) {
        this.name = name;
        this.ssn = ssn;
        this.birth = birth;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.additionalInfo = additionalInfo;
        this.maxDistance = maxDistance;
        this.password = password;
        this.priority = priority;
        this.status = status;
    }
}
