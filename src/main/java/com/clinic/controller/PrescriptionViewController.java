package com.clinic.controller;

import com.clinic.dto.PrescriptionRequestDto;
import com.clinic.dto.PrescriptionResponseDto;
import com.clinic.enums.PrescriptionStatus;
import com.clinic.service.AppointmentService;
import com.clinic.service.DoctorService;
import com.clinic.service.PatientService;
import com.clinic.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
public class PrescriptionViewController {

    private final PrescriptionService prescriptionService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @GetMapping
    public String listPrescriptions(Model model) {
        model.addAttribute("prescriptions", prescriptionService.getAllPrescriptions());
        model.addAttribute("statuses", PrescriptionStatus.values());
        return "prescriptions/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public String showCreateForm(Model model) {
        model.addAttribute("prescriptionRequest", new PrescriptionRequestDto());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        model.addAttribute("statuses", PrescriptionStatus.values());
        return "prescriptions/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public String createPrescription(@Valid @ModelAttribute("prescriptionRequest") PrescriptionRequestDto requestDto,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("appointments", appointmentService.getAllAppointments());
            model.addAttribute("statuses", PrescriptionStatus.values());
            return "prescriptions/form";
        }
        prescriptionService.createPrescription(requestDto);
        return "redirect:/prescriptions";
    }

    @GetMapping("/{id}")
    public String viewPrescription(@PathVariable String id, Model model) {
        model.addAttribute("prescription", prescriptionService.getPrescriptionById(id));
        return "prescriptions/detail";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public String showEditForm(@PathVariable String id, Model model) {
        PrescriptionResponseDto prescription = prescriptionService.getPrescriptionById(id);
        PrescriptionRequestDto requestDto = new PrescriptionRequestDto();
        requestDto.setAppointmentId(prescription.getAppointmentId());
        requestDto.setPatientId(prescription.getPatientId());
        requestDto.setDoctorId(prescription.getDoctorId());
        requestDto.setMedications(prescription.getMedications());
        requestDto.setDosage(prescription.getDosage());
        requestDto.setInstructions(prescription.getInstructions());
        requestDto.setStatus(prescription.getStatus());

        model.addAttribute("prescription", prescription);
        model.addAttribute("prescriptionRequest", requestDto);
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        model.addAttribute("statuses", PrescriptionStatus.values());
        return "prescriptions/form";
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public String updatePrescription(@PathVariable String id,
                                     @Valid @ModelAttribute("prescriptionRequest") PrescriptionRequestDto requestDto,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("prescription", prescriptionService.getPrescriptionById(id));
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("appointments", appointmentService.getAllAppointments());
            model.addAttribute("statuses", PrescriptionStatus.values());
            return "prescriptions/form";
        }
        prescriptionService.updatePrescription(id, requestDto);
        return "redirect:/prescriptions";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePrescription(@PathVariable String id) {
        prescriptionService.deletePrescription(id);
        return "redirect:/prescriptions";
    }
}