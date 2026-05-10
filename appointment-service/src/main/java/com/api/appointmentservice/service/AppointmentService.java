package com.api.appointmentservice.service;

import com.api.appointmentservice.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    Object createAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
}
