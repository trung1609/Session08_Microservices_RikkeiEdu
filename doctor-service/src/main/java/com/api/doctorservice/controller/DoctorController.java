package com.api.doctorservice.controller;

import com.api.doctorservice.dto.DoctorDTO;
import com.api.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getDoctors() {
        List<DoctorDTO> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDoctorById(@PathVariable Long id) {
        Object response = doctorService.getDoctorById(id);
        if (response instanceof DoctorDTO) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(429).body(response);
        }
    }

    @GetMapping("/async/{id}")
    public CompletableFuture<ResponseEntity<Object>> getDoctorByIdAsync(@PathVariable Long id) {
        return doctorService.getDoctorByIdAsync(id)
                .thenApply(response -> {
                    if (response instanceof DoctorDTO) {
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT.value()).body(response);
                    }
                });

    }
}
