package edu.nyu.covid19vaccinationsignupsystem.model.entity;

import edu.nyu.covid19vaccinationsignupsystem.model.entity.embeddedid.PatientScheduleId;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "patient_schedule")
@IdClass(PatientScheduleId.class)
public class PatientSchedule implements Serializable {
    @Id
    @Column(name = "patient_id")
    private Integer patientId;
    @Id
    @Column(name = "start_time")
    private Integer startTime;
    @Id
    @Column(name = "day_of_the_week")
    private Integer dayOfTheWeek;

    public PatientSchedule() {}

    public PatientSchedule(Integer patientId, Integer startTime, Integer dayOfTheWeek) {
        this.patientId = patientId;
        this.startTime = startTime;
        this.dayOfTheWeek = dayOfTheWeek;
    }
}
