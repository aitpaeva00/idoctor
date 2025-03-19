package com.aitpaeva.idoctor.service;

import com.aitpaeva.idoctor.model.Appointment;
import com.aitpaeva.idoctor.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getUpcomingAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorIdAndAppointmentTimeAfter(doctorId, LocalDateTime.now());
    }

    public Appointment bookAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
}