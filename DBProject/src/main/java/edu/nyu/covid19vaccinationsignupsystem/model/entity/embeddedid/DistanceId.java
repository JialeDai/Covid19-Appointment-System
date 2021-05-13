package edu.nyu.covid19vaccinationsignupsystem.model.entity.embeddedid;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class DistanceId implements Serializable {
    private Integer patientId;
    private Integer providerId;
}
