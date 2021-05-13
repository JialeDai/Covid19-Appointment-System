package edu.nyu.covid19vaccinationsignupsystem.model.entity;

import edu.nyu.covid19vaccinationsignupsystem.model.entity.embeddedid.DistanceId;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data

@Entity
@IdClass(DistanceId.class)
public class Distance {
    @Id
    @Column(name = "patient_id")
    private int patientId;
    @Id
    @Column(name = "provider_id")
    private int providerId;
    private double distance;

    public Distance() {}

    public Distance(int patientId, int providerId, double distance) {
        this.patientId = patientId;
        this.providerId = providerId;
        this.distance = distance;
    }
}
