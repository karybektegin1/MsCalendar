package com.appointmentService.dto;

import lombok.Data;

@Data
public class ProcedureUpdateRequest {
    private String name;
    private String description;
    private Integer defaultDurationMinutes;
}
