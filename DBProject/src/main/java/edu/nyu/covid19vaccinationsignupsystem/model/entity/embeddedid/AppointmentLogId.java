package edu.nyu.covid19vaccinationsignupsystem.model.entity.embeddedid;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
public class AppointmentLogId implements Serializable {
    private Integer appointmentId;
    private Timestamp logTime;
}
