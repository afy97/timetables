package com.t2.timetables.parser.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ParsingService {
    public void parse(InputStream stream) throws IOException {
        Workbook workbook = WorkbookFactory.create(stream);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        bailout:
        for (Row row : sheet) {
            for (Cell cell : row) {
                String cellValue = formatter.formatCellValue(cell);

                if (StringUtils.hasText(cellValue)) {
                    System.out.print(cellValue + "\t");
                } else {
                    break bailout;
                }
            }
            System.out.println();
        }

        stream.close();
    }
}
