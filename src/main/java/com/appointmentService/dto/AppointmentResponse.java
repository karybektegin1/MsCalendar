package com.appointmentService.dto;

import com.appointmentService.domain.entity.Procedure;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private Long id;
    private Long externalPatientId;
    private String patientName;
    private String patientContact;
    private Long doctorId;
    private Long clinicId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer statusId;
    private String notes;
    private String telegramChatId;

    private ProcedureDTO procedure;

    @Data
    public static class ProcedureDTO{
        private Long id;
        private String name;
        private String description;
        private Integer defaultDurationMinutes;
    }


}
