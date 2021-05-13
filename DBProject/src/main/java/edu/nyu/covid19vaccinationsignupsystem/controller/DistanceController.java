package edu.nyu.covid19vaccinationsignupsystem.controller;

import edu.nyu.covid19vaccinationsignupsystem.exceptions.PatientNotFoundException;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Distance;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Patient;
import edu.nyu.covid19vaccinationsignupsystem.model.entity.Provider;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.DistanceRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.PatientRepository;
import edu.nyu.covid19vaccinationsignupsystem.model.repository.ProviderRepository;
import edu.nyu.covid19vaccinationsignupsystem.utils.MapUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.ProviderNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class DistanceController {
    @Resource
    private DistanceRepository distanceRepository;
    @Resource
    private ProviderRepository providerRepository;
    @Resource
    private PatientRepository patientRepository;

    @GetMapping(path = "/distance")
    public List<Distance> all() {
        return distanceRepository.findAll();
    }

    /**
     * update the whole distance table, reset the distance for each patient and provider
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    @GetMapping(path = "/distance/set/all")
    public List<Distance> setAllDistance() throws URISyntaxException, IOException {
        List<Provider> providerList = providerRepository.findAll();
        List<Patient> patientList = patientRepository.findAll();
        List<Distance> distanceList = new ArrayList<>();
        for (Patient patient : patientList) {
            for (Provider provider : providerList) {
                distanceList.add(new Distance(patient.getId(), provider.getId(), MapUtils.getDistance(patient.getAddress(), provider.getAddress())));
            }
        }
        distanceRepository.deleteAll();
        return distanceRepository.saveAll(distanceList);
    }

    @PostMapping(path = "/distance/patients/signup")
    @ResponseBody
    public List<Distance> addDistanceWhenPatientSignUp(@RequestBody Map<String, String> patientInfo) throws URISyntaxException, IOException {
        List<Provider> providerList = providerRepository.findAll();
        Patient patient = patientRepository.findPatientByEmail(patientInfo.get("email"));
        List<Distance> distanceList = new ArrayList<>();
        for (Provider provider : providerList) {
            distanceList.add(new Distance(patient.getId(), provider.getId(), MapUtils.getDistance(patient.getAddress(), provider.getAddress())));
        }
        return distanceRepository.saveAll(distanceList);
    }

    @PatchMapping(path = "/distance/patients/address/update")
    @ResponseBody
    public List<Distance> updateDistanceWhenPatientUpdate(@RequestBody Map<String, String> patientInfo) throws URISyntaxException, IOException {
        Integer patientId = Integer.parseInt(patientInfo.get("id"));
        List<Provider> providerList = providerRepository.findAll();
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException(patientId));
        List<Distance> distanceList = new ArrayList<>();
        for (Provider provider : providerList) {
            distanceList.add(new Distance(patientId, provider.getId(), MapUtils.getDistance(patient.getAddress(), provider.getAddress())));
        }
        return distanceRepository.saveAll(distanceList);
    }

    @PostMapping(path = "/distance/providers/signup")
    @ResponseBody
    public List<Distance> addDistanceWhenProviderSignUp(@RequestBody Map<String, String> providerInfo) throws URISyntaxException, IOException {
        List<Patient> patientList = patientRepository.findAll();
        Provider provider = providerRepository.findProviderByPhone(providerInfo.get("phone"));
        List<Distance> distanceList = new ArrayList<>();
        for (Patient patient : patientList) {
            distanceList.add(new Distance(patient.getId(), provider.getId(), MapUtils.getDistance(patient.getAddress(), provider.getAddress())));
        }
        return distanceRepository.saveAll(distanceList);
    }

    @PatchMapping(path = "/distance/providers/address/update")
    @ResponseBody
    public List<Distance> updateDistanceWhenProviderUpdate(@RequestBody Map<String, String> providerInfo) throws URISyntaxException, IOException {
        Integer providerId = Integer.parseInt(providerInfo.get("id"));
        List<Patient> patientList = patientRepository.findAll();
        Provider provider = providerRepository.findById(providerId).orElseThrow(()->new ProviderNotFoundException());
        List<Distance> distanceList = new ArrayList<>();
        for (Patient patient : patientList) {
            distanceList.add(new Distance(patient.getId(),providerId,MapUtils.getDistance(patient.getAddress(),provider.getAddress())));
        }
        return distanceRepository.saveAll(distanceList);
    }
}
