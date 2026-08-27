package com.clinic.controller;

import com.clinic.dto.AppointmentRequestDto;
import com.clinic.dto.AppointmentResponseDto;
import com.clinic.enums.AppointmentStatus;
import com.clinic.service.AppointmentService;
import com.clinic.service.DoctorService;
import com.clinic.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentViewController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public String showCreateForm(Model model) {
        model.addAttribute("appointmentRequest", new AppointmentRequestDto());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public String createAppointment(@Valid @ModelAttribute("appointmentRequest") AppointmentRequestDto requestDto,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("statuses", AppointmentStatus.values());
            return "appointments/form";
        }
        appointmentService.createAppointment(requestDto);
        return "redirect:/appointments";
    }

    @GetMapping("/{id}")
    public String viewAppointment(@PathVariable String id, Model model) {
        model.addAttribute("appointment", appointmentService.getAppointmentById(id));
        return "appointments/detail";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public String showEditForm(@PathVariable String id, Model model) {
        AppointmentResponseDto appointment = appointmentService.getAppointmentById(id);
        AppointmentRequestDto requestDto = new AppointmentRequestDto();
        requestDto.setPatientId(appointment.getPatientId());
        requestDto.setDoctorId(appointment.getDoctorId());
        requestDto.setAppointmentDate(appointment.getAppointmentDate());
        requestDto.setAppointmentTime(appointment.getAppointmentTime());
        requestDto.setStatus(appointment.getStatus());
        requestDto.setNotes(appointment.getNotes());

        model.addAttribute("appointment", appointment);
        model.addAttribute("appointmentRequest", requestDto);
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/form";
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public String updateAppointment(@PathVariable String id,
                                    @Valid @ModelAttribute("appointmentRequest") AppointmentRequestDto requestDto,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("appointment", appointmentService.getAppointmentById(id));
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("statuses", AppointmentStatus.values());
            return "appointments/form";
        }
        appointmentService.updateAppointment(id, requestDto);
        return "redirect:/appointments";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAppointment(@PathVariable String id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }
}