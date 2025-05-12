package com.appointmentService.dto;

import lombok.Data;

@Data
public class GetProcedureResponse {
    private String name;
    private String description;
    private Integer defaultDurationMinutes;
}
