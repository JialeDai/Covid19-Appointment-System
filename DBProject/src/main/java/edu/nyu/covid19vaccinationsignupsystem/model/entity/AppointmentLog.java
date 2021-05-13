package edu.nyu.covid19vaccinationsignupsystem.model.entity;

import edu.nyu.covid19vaccinationsignupsystem.model.entity.embeddedid.AppointmentLogId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "appointment_log")
@IdClass(AppointmentLogId.class)
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentLog implements Serializable {
    @Id
    @Column(name = "appointment_id")
    private Integer appointmentId;
    private String action;
    @Id
    @Column(name = "log_time")
    private Timestamp logTime;
    @Column(name = "patient_id")
    private Integer patientId;
}
