package com.appointmentService.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class CreateAppointmentRequest {

    private Long externalPatientId;
    private String patientName;
    private String patientContact;
    private Long doctorId;
    private Long clinicId;
    private Long procedureId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer statusId;
    private String notes;

}
