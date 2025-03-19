package com.aitpaeva.idoctor.controller;

import com.aitpaeva.idoctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public String showDoctors(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "doctors"; // Refers to doctors.html in templates folder
    }
}