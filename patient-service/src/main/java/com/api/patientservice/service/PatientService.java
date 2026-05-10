package com.api.patientservice.service;

import com.api.patientservice.dto.PatientDTO;
import com.api.patientservice.entity.Patient;

public interface PatientService {
    PatientDTO createPatient(Patient patient);
    PatientDTO getPatientById(Long id);
}
