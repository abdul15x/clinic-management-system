package com.clinic.controller;

import com.clinic.dto.DoctorRequestDto;
import com.clinic.dto.DoctorResponseDto;
import com.clinic.enums.Specialization;
import com.clinic.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorViewController {

    private final DoctorService doctorService;

    @GetMapping
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("specializations", Specialization.values());
        return "doctors/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("doctorRequest", new DoctorRequestDto());
        model.addAttribute("specializations", Specialization.values());
        return "doctors/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createDoctor(@Valid @ModelAttribute("doctorRequest") DoctorRequestDto requestDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("specializations", Specialization.values());
            return "doctors/form";
        }
        doctorService.createDoctor(requestDto);
        return "redirect:/doctors";
    }

    @GetMapping("/{id}")
    public String viewDoctor(@PathVariable String id, Model model) {
        model.addAttribute("doctor", doctorService.getDoctorById(id));
        return "doctors/detail";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable String id, Model model) {
        DoctorResponseDto doctor = doctorService.getDoctorById(id);
        DoctorRequestDto requestDto = new DoctorRequestDto();
        requestDto.setName(doctor.getName());
        requestDto.setEmail(doctor.getEmail());
        requestDto.setPhone(doctor.getPhone());
        requestDto.setSpecialization(doctor.getSpecialization());
        requestDto.setQualification(doctor.getQualification());
        requestDto.setExperience(doctor.getExperience());
        requestDto.setAvailability(doctor.getAvailability());

        model.addAttribute("doctor", doctor);
        model.addAttribute("doctorRequest", requestDto);
        model.addAttribute("specializations", Specialization.values());
        return "doctors/form";
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateDoctor(@PathVariable String id,
                               @Valid @ModelAttribute("doctorRequest") DoctorRequestDto requestDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctor", doctorService.getDoctorById(id));
            model.addAttribute("specializations", Specialization.values());
            return "doctors/form";
        }
        doctorService.updateDoctor(id, requestDto);
        return "redirect:/doctors";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDoctor(@PathVariable String id) {
        doctorService.deleteDoctor(id);
        return "redirect:/doctors";
    }
}