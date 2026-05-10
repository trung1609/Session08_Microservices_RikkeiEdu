package com.api.appointmentservice.service.impl;

import com.api.appointmentservice.dto.ApiResponseError;
import com.api.appointmentservice.entity.Appointment;
import com.api.appointmentservice.exception.RequestSpamException;
import com.api.appointmentservice.exception.ServiceUnavailableException;
import com.api.appointmentservice.repository.AppointmentRepository;
import com.api.appointmentservice.service.AppointmentService;
import com.api.appointmentservice.service.client.DoctorClient;
import com.api.appointmentservice.service.client.PatientClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorClient doctorClient;
    private final PatientClient patientClient;

    @Override
    @RateLimiter(name = "doctorServiceRL", fallbackMethod = "spamCreateAppointmentFallback")
    @CircuitBreaker(name = "doctorServiceCB", fallbackMethod = "createAppointmentFallback")
    @Retry(name = "patientRetry")
    public Object createAppointment(Appointment appointment) {
        if (doctorClient.getDoctorById(appointment.getDoctorId()) == null) {
            throw new EntityNotFoundException("Doctor not found with id: " + appointment.getDoctorId());
        }
        if (patientClient.getPatientById(appointment.getPatientId()) == null) {
            throw new EntityNotFoundException("Patient not found with id: " + appointment.getPatientId());
        }
        return appointmentRepository.save(appointment);
    }

    public Object createAppointmentFallback(Appointment appointment, Throwable throwable) {
        log.error("Sự cố liên dịch vụ: {}", throwable.getMessage());
        if (throwable instanceof EntityNotFoundException){
            throw (EntityNotFoundException) throwable;
        }
        throw new ServiceUnavailableException("Hệ thống quản lý bác sĩ hiện không khả dụng. Vui lòng đặt lịch sau!");
    }

    public Object spamCreateAppointmentFallback(Appointment appointment, Throwable throwable) {
        if (throwable instanceof RequestNotPermitted) {
            log.error("Rate Limit kích hoạt: {}", throwable.getMessage());
            throw new RequestSpamException("Bạn thao tác quá nhanh. Vui lòng đợi!");
        }

        log.error("Lỗi từ lớp bên trong truyền ra RateLimiter: {}", throwable.getMessage());
        throw new RuntimeException(throwable);
    }
    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
