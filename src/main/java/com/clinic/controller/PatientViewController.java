package com.clinic.controller;

import com.clinic.dto.PatientRequestDto;
import com.clinic.dto.PatientResponseDto;
import com.clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientViewController {

    private final PatientService patientService;

    @GetMapping
    public String listPatients(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "patients/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("patientRequest", new PatientRequestDto());
        return "patients/form";
    }

    @PostMapping("/create")
    public String createPatient(@ModelAttribute PatientRequestDto requestDto) {
        patientService.createPatient(requestDto);
        return "redirect:/patients";
    }

    @GetMapping("/{id}")
    public String viewPatient(@PathVariable String id, Model model) {
        model.addAttribute("patient", patientService.getPatientById(id));
        return "patients/detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        PatientResponseDto patient = patientService.getPatientById(id);
        PatientRequestDto requestDto = new PatientRequestDto();
        requestDto.setName(patient.getName());
        requestDto.setEmail(patient.getEmail());
        requestDto.setPhone(patient.getPhone());
        requestDto.setGender(patient.getGender());
        requestDto.setDateOfBirth(patient.getDateOfBirth());
        requestDto.setAddress(patient.getAddress());
        requestDto.setBloodGroup(patient.getBloodGroup());

        model.addAttribute("patient", patient);
        model.addAttribute("patientRequest", requestDto);
        return "patients/form";
    }

    @PostMapping("/{id}/update")
    public String updatePatient(@PathVariable String id, @ModelAttribute PatientRequestDto requestDto) {
        patientService.updatePatient(id, requestDto);
        return "redirect:/patients";
    }

    @GetMapping("/{id}/delete")
    public String deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }
}