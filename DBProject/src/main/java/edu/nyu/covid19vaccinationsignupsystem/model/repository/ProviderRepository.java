package edu.nyu.covid19vaccinationsignupsystem.model.repository;

import edu.nyu.covid19vaccinationsignupsystem.model.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider,Integer> {
    Provider findProviderByPhone(String phone);
}
