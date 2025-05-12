package com.appointmentService.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CreateProcedureRequest {
    private String name;
    private String description;
    private Integer defaultDurationMinutes;
}
