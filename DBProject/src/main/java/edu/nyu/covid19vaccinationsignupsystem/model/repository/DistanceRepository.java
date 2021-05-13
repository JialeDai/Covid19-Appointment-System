package edu.nyu.covid19vaccinationsignupsystem.model.repository;

import edu.nyu.covid19vaccinationsignupsystem.model.entity.Distance;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.embeddedid.DistanceId;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@ComponentScan("edu.nyu")
public interface DistanceRepository extends JpaRepository<Distance, DistanceId> {
}
