package com.clinic.controller;

import com.clinic.dto.MedicalRecordRequestDto;
import com.clinic.dto.MedicalRecordResponseDto;
import com.clinic.service.DoctorService;
import com.clinic.service.MedicalRecordService;
import com.clinic.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medical-records")
@RequiredArgsConstructor
public class MedicalRecordViewController {

    private final MedicalRecordService medicalRecordService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping
    public String listMedicalRecords(Model model) {
        model.addAttribute("records", medicalRecordService.getAllMedicalRecords());
        return "medical-records/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public String showCreateForm(Model model) {
        model.addAttribute("recordRequest", new MedicalRecordRequestDto());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "medical-records/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public String createMedicalRecord(@Valid @ModelAttribute("recordRequest") MedicalRecordRequestDto requestDto,
                                      BindingResult bindingResult,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            return "medical-records/form";
        }
        medicalRecordService.createMedicalRecord(requestDto);
        return "redirect:/medical-records";
    }

    @GetMapping("/{id}")
    public String viewMedicalRecord(@PathVariable String id, Model model) {
        model.addAttribute("record", medicalRecordService.getMedicalRecordById(id));
        return "medical-records/detail";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public String showEditForm(@PathVariable String id, Model model) {
        MedicalRecordResponseDto record = medicalRecordService.getMedicalRecordById(id);
        MedicalRecordRequestDto requestDto = new MedicalRecordRequestDto();
        requestDto.setPatientId(record.getPatientId());
        requestDto.setDoctorId(record.getDoctorId());
        requestDto.setDiagnosis(record.getDiagnosis());
        requestDto.setTreatment(record.getTreatment());
        requestDto.setNotes(record.getNotes());

        model.addAttribute("record", record);
        model.addAttribute("recordRequest", requestDto);
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "medical-records/form";
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public String updateMedicalRecord(@PathVariable String id,
                                      @Valid @ModelAttribute("recordRequest") MedicalRecordRequestDto requestDto,
                                      BindingResult bindingResult,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("record", medicalRecordService.getMedicalRecordById(id));
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            return "medical-records/form";
        }
        medicalRecordService.updateMedicalRecord(id, requestDto);
        return "redirect:/medical-records";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteMedicalRecord(@PathVariable String id) {
        medicalRecordService.deleteMedicalRecord(id);
        return "redirect:/medical-records";
    }
}