package com.t2.timetables.parser.service;

import com.t2.timetables.parser.dto.InputDTO;
import com.t2.timetables.parser.dto.OutputDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ParsingService {
    private List<InputDTO> parse(InputStream stream) throws IOException {
        Workbook workbook = WorkbookFactory.create(stream);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm:ss", new Locale("tr", "TR"));

        List<InputDTO> inputs = new LinkedList<>();

        boolean skipTitle = true;

        for (Row row : sheet) {
            if (skipTitle) {
                skipTitle = false;
                continue;
            }

            if (!StringUtils.hasText(formatter.formatCellValue(row.getCell(0)))) {
                break;
            }

            inputs.add(new InputDTO(
                    Long.parseLong(formatter.formatCellValue(row.getCell(0))),
                    formatter.formatCellValue(row.getCell(1)),
                    LocalDateTime.parse(formatter.formatCellValue(row.getCell(2)), dtf),
                    LocalDateTime.parse(formatter.formatCellValue(row.getCell(3)), dtf),
                    Double.parseDouble(formatter.formatCellValue(row.getCell(4)).replaceAll(",", ".")),
                    Double.parseDouble(formatter.formatCellValue(row.getCell(5)).replaceAll(",", "."))
            ));
        }

        workbook.close();

        return inputs;
    }

    private Resource convert(MultipartFile input) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<OutputDTO> output = parse(input.getInputStream()).stream().map(OutputDTO::new).collect(Collectors.toList());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Row heading = sheet.createRow(0);
        heading.createCell(0).setCellValue("Tarih");
        heading.createCell(1).setCellValue("İsim");
        heading.createCell(2).setCellValue("Giriş");
        heading.createCell(3).setCellValue("Çıkış");
        heading.createCell(4).setCellValue("Günlük Toplam Giriş-Çıkış Süresi");

        for (int i = 0; i < output.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(output.get(i).getDate());
            row.createCell(1).setCellValue(output.get(i).getName());
            row.createCell(2).setCellValue(output.get(i).getEntry());
            row.createCell(3).setCellValue(output.get(i).getEntry());
            row.createCell(4).setCellValue(output.get(i).getHours());
        }

        workbook.write(outputStream);

        outputStream.close();
        workbook.close();

        return new ByteArrayResource(outputStream.toByteArray());
    }

    public void convertMultiple(MultipartFile[] files, OutputStream outputStream) throws IOException {
        ZipOutputStream output = new ZipOutputStream(outputStream);

        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            Resource converted = convert(file);
            ZipEntry entry = new ZipEntry(Objects.requireNonNull(filename).substring(0, filename.lastIndexOf(".")) + "_converted.xlsx");
            entry.setSize(converted.contentLength());
            output.putNextEntry(entry);
            StreamUtils.copy(converted.getInputStream(), output);
            output.closeEntry();
        }

        output.finish();
    }
}
