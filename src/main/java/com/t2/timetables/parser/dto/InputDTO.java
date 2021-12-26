package com.t2.timetables.parser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InputDTO {
    private long id;
    private String name;
    private LocalDateTime entry;
    private LocalDateTime exit;
    private double intervalMinutes;
    private double intervalHours;
}
