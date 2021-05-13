package edu.nyu.covid19vaccinationsignupsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointment")
public class Appointment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "provider_id")
    private Integer providerId;
    @Column(name = "start_time")
    private Timestamp startTime;
    @Column(name = "end_time")
    private Timestamp endTime;
    @Column(name = "patient_id")
    private Integer patientId;
    private String status = "released";

    public Appointment(Integer providerId, Timestamp startTime, Timestamp endTime, Integer patientId, String status) {
        this.providerId = providerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.patientId = patientId;
        this.status = status;
    }
}
