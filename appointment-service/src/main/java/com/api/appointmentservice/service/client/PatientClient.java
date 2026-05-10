package com.api.appointmentservice.service.client;

import com.api.appointmentservice.dto.PatientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PatientClient {
    private final RestTemplate restTemplate;

    public PatientDTO getPatientById(Long patientId) {
        String url = "http://patient-service/api/v1/patients/" + patientId;
        return restTemplate.getForObject(url, PatientDTO.class);
    }
}
