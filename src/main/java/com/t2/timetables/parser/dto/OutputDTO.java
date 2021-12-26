package com.t2.timetables.parser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@AllArgsConstructor
public class OutputDTO {
    private String date;
    private String name;
    private String entry;
    private String exit;
    private String hours;

    public OutputDTO(InputDTO from) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy", new Locale("tr", "TR"));
        DateTimeFormatter entryExitFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", new Locale("tr", "TR"));

        this.date   = from.getEntry().format(dateFormatter);
        this.name   = from.getName();
        this.entry  = from.getEntry().format(entryExitFormatter);
        this.exit   = from.getEntry().format(entryExitFormatter);
        this.hours  = BigDecimal.valueOf(from.getIntervalHours()).setScale(2, RoundingMode.HALF_UP).toString();
    }
}
