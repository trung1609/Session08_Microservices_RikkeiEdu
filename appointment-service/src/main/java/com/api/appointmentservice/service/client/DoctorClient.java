package com.api.appointmentservice.service.client;

import com.api.appointmentservice.dto.DoctorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DoctorClient {
    private final RestTemplate restTemplate;

    public DoctorDTO getDoctorById(Long doctorId) {
        String url = "http://doctor-service/api/v1/doctors/" + doctorId;
        return restTemplate.getForObject(url, DoctorDTO.class);
    }
}
