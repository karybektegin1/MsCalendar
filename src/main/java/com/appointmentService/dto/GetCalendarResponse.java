package com.appointmentService.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetCalendarResponse {
    private long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer statusId;
    private String procedureName;
}
