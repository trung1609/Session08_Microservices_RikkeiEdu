package com.api.doctorservice.service.impl;

import com.api.doctorservice.dto.DoctorDTO;
import com.api.doctorservice.entity.Doctor;
import com.api.doctorservice.repository.DoctorRepository;
import com.api.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;

    @Override
    public DoctorDTO createDoctor(Doctor doctor) {
        Doctor savedDoctor = doctorRepository.save(doctor);
        return DoctorDTO.builder()
                .id(savedDoctor.getId())
                .name(savedDoctor.getName())
                .specialization(savedDoctor.getSpecialization())
                .build();
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctor -> DoctorDTO.builder()
                        .id(doctor.getId())
                        .name(doctor.getName())
                        .specialization(doctor.getSpecialization())
                        .build())
                .toList();
    }

    @Override
    public DoctorDTO getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(doctor -> DoctorDTO.builder()
                        .id(doctor.getId())
                        .name(doctor.getName())
                        .specialization(doctor.getSpecialization())
                        .build())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
}
