package com.api.patientservice.service.impl;

import com.api.patientservice.dto.PatientDTO;
import com.api.patientservice.entity.Patient;
import com.api.patientservice.repository.PatientRepository;
import com.api.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    @Override
    public PatientDTO createPatient(Patient patient) {
        Patient savedPatient = patientRepository.save(patient);
        return PatientDTO.builder()
                .id(savedPatient.getId())
                .fullName(savedPatient.getFullName())
                .address(savedPatient.getAddress())
                .medicalHistory(savedPatient.getMedicalHistory())
                .build();
    }

    @Override
    public PatientDTO getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(patient -> PatientDTO.builder()
                        .id(patient.getId())
                        .fullName(patient.getFullName())
                        .address(patient.getAddress())
                        .medicalHistory(patient.getMedicalHistory())
                        .build())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }
}
