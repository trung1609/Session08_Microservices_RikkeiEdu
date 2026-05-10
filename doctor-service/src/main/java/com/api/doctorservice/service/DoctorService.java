package com.api.doctorservice.service;

import com.api.doctorservice.dto.DoctorDTO;
import com.api.doctorservice.entity.Doctor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DoctorService {
    DoctorDTO createDoctor(Doctor doctor);
    List<DoctorDTO> getAllDoctors();
    Object getDoctorById(Long id);
    CompletableFuture<Object> getDoctorByIdAsync(Long id);
}
