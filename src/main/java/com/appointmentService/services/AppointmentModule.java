package com.appointmentService.services;

import com.appointmentService.domain.entity.Appointment;
import com.appointmentService.domain.entity.Procedure;
import com.appointmentService.dto.AppointmentResponse;
import com.appointmentService.dto.AppointmentUpdateRequest;
import com.appointmentService.dto.CreateAppointmentRequest;
import com.appointmentService.repository.AppointmentRepository;
import com.appointmentService.repository.ProcedureRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentModule {
    private final AppointmentRepository appointmentRepository;
    private final ProcedureRepository procedureRepository;

    public AppointmentResponse getAppointmentById(Long id) throws EntityNotFoundException{
        Appointment appointment = appointmentRepository.findById(id)
                .orElse(null);

        if (appointment == null) {
            return null;
        }

        AppointmentResponse dto = new AppointmentResponse();
        dto.setId(appointment.getId());
        dto.setExternalPatientId(appointment.getExternalPatientId());
        dto.setPatientName(appointment.getPatientName());
        dto.setPatientContact(appointment.getPatientContact());
        dto.setDoctorId(appointment.getDoctorId());
        dto.setClinicId(appointment.getClinicId());
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setStatusId(appointment.getStatusId());
        dto.setNotes(appointment.getNotes());
        dto.setTelegramChatId(appointment.getTelegramChatId());

        AppointmentResponse.ProcedureDTO procedureDTO = new AppointmentResponse.ProcedureDTO();
        procedureDTO.setId(appointment.getProcedure().getId());
        procedureDTO.setName(appointment.getProcedure().getName());
        procedureDTO.setDescription(appointment.getProcedure().getDescription());
        procedureDTO.setDefaultDurationMinutes(appointment.getProcedure().getDefaultDurationMinutes());

        dto.setProcedure(procedureDTO);
        return dto;

    }

    public Appointment createAppointment(CreateAppointmentRequest request){
        Procedure procedure = procedureRepository.findById(request.getProcedureId()).
                orElse(null);
        if(procedure == null){
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setExternalPatientId(request.getExternalPatientId());
        appointment.setPatientName(request.getPatientName());
        appointment.setPatientContact(request.getPatientContact());
        appointment.setDoctorId(request.getDoctorId());
        appointment.setClinicId(request.getClinicId());
        appointment.setProcedure(procedure);
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());
        appointment.setStatusId(request.getStatusId());
        appointment.setNotes(request.getNotes());

        return appointmentRepository.save(appointment);
    }

    public void updateAppointment(Appointment appointment, AppointmentUpdateRequest request){
        if (request.getPatientName() != null) {
            appointment.setPatientName(request.getPatientName());
        }
        if (request.getPatientContact() != null) {
            appointment.setPatientContact(request.getPatientContact());
        }
        if (request.getDoctorId() != null) {
            appointment.setDoctorId(request.getDoctorId());
        }
        if (request.getClinicId() != null) {
            appointment.setClinicId(request.getClinicId());
        }
        if (request.getStartTime() != null) {
            appointment.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            appointment.setEndTime(request.getEndTime());
        }
        if (request.getStatusId() != null) {
            appointment.setStatusId(request.getStatusId());
        }
        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }
        if(request.getExternalPatientId() != null){
            appointment.setExternalPatientId(request.getExternalPatientId());
        }
    }

}
