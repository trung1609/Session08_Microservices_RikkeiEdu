package com.api.doctorservice.service;

import com.api.doctorservice.dto.DoctorDTO;
import com.api.doctorservice.entity.Doctor;

import java.util.List;

public interface DoctorService {
    DoctorDTO createDoctor(Doctor doctor);
    List<DoctorDTO> getAllDoctors();
    Object getDoctorById(Long id);
}
