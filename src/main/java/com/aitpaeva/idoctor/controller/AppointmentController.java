package com.aitpaeva.idoctor.controller;

import com.aitpaeva.idoctor.model.Appointment;
import com.aitpaeva.idoctor.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getUpcomingAppointments(@PathVariable Long doctorId) {
        return appointmentService.getUpcomingAppointments(doctorId);
    }

    @PostMapping("/book")
    public Appointment bookAppointment(@RequestBody Appointment appointment) {
        return appointmentService.bookAppointment(appointment);
    }
}