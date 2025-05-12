package com.appointmentService.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetCalendarRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}
