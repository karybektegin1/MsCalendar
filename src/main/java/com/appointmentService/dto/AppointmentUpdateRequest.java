package com.appointmentService.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentUpdateRequest {
    private Long externalPatientId;  // Optional, может быть null
    private String patientName;
    private String patientContact;
    private Long doctorId;
    private Long clinicId;
    private Long procedureId;  // Идентификатор процедуры
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer statusId;
    private String notes;
}
