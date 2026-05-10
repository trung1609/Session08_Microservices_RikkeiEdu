package com.api.doctorservice.service.impl;

import com.api.doctorservice.dto.ApiError;
import com.api.doctorservice.dto.DoctorDTO;
import com.api.doctorservice.entity.Doctor;
import com.api.doctorservice.repository.DoctorRepository;
import com.api.doctorservice.service.DoctorService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    @RateLimiter(name = "searchDoctorLimit", fallbackMethod = "searchDoctorLimitFallback")
    public Object getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(doctor -> DoctorDTO.builder()
                        .id(doctor.getId())
                        .name(doctor.getName())
                        .specialization(doctor.getSpecialization())
                        .build())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public Object searchDoctorLimitFallback(Long id, Throwable throwable) {
        return new ApiError(
                "Error",
                "Bạn đã gửi quá 5 request mỗi 10 giây",
                429
        );
    }

    @Override
    @TimeLimiter(name = "searchDoctorTimeLimit", fallbackMethod = "searchDoctorTimeLimitFallback")
    public CompletableFuture<Object> getDoctorByIdAsync(Long id) {
        return CompletableFuture.supplyAsync(() ->
                {
                    try {
                        Thread.sleep(5000); // Simulate delay
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return doctorRepository.findById(id)
                            .map(doctor -> DoctorDTO.builder()
                                    .id(doctor.getId())
                                    .name(doctor.getName())
                                    .specialization(doctor.getSpecialization())
                                    .build())
                            .orElseThrow(() -> new RuntimeException("Doctor not found"));
                }
        );
    }

    public CompletableFuture<Object> searchDoctorTimeLimitFallback(Long id, Throwable throwable) {
        return CompletableFuture.completedFuture(new ApiError(
                "Error",
                "Dịch vụ tìm kiếm bác sĩ đang quá tải, vui lòng thử lại sau",
                HttpStatus.REQUEST_TIMEOUT.value()
        ));
    }
}
