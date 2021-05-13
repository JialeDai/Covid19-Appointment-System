package edu.nyu.covid19vaccinationsignupsystem.model.entity.embeddedid;

import java.io.Serializable;

public class PatientScheduleId implements Serializable {
    private Integer patientId;
    private Integer startTime;
    private Integer dayOfTheWeek;

    public PatientScheduleId() {}

    public PatientScheduleId(Integer patientId, Integer startTime, Integer dayOfTheWeek) {
        this.patientId = patientId;
        this.startTime = startTime;
        this.dayOfTheWeek = dayOfTheWeek;
    }
}
