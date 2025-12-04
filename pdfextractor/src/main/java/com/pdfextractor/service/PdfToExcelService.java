package com.pdfextractor.service;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfToExcelService {

    public byte[] convertPdfToExcel(MultipartFile pdfFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            
            return createExcelFromText(text);
        }
    }

    private byte[] createExcelFromText(String text) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("PDF Data");
        
        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Create data style
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setWrapText(true);
        
        // Split text into lines and process
        String[] lines = text.split("\n");
        int rowNum = 0;
        
        // Add header row
        Row headerRow = sheet.createRow(rowNum++);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Content");
        headerCell.setCellStyle(headerStyle);
        
        // Add data rows
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                Row row = sheet.createRow(rowNum++);
                Cell cell = row.createCell(0);
                cell.setCellValue(line.trim());
                cell.setCellStyle(dataStyle);
            }
        }
        
        // Auto-size columns
        sheet.autoSizeColumn(0);
        
        // Convert to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }

    public List<String> extractTablesFromPdf(MultipartFile pdfFile) throws IOException {
        List<String> tables = new ArrayList<>();
        
        try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            
            // Simple table detection based on patterns
            String[] lines = text.split("\n");
            StringBuilder currentTable = new StringBuilder();
            boolean inTable = false;
            
            for (String line : lines) {
                line = line.trim();
                if (isTableRow(line)) {
                    if (!inTable) {
                        inTable = true;
                        currentTable = new StringBuilder();
                    }
                    currentTable.append(line).append("\n");
                } else {
                    if (inTable && currentTable.length() > 0) {
                        tables.add(currentTable.toString());
                        currentTable = new StringBuilder();
                        inTable = false;
                    }
                }
            }
            
            // Add the last table if exists
            if (inTable && currentTable.length() > 0) {
                tables.add(currentTable.toString());
            }
        }
        
        return tables;
    }

    private boolean isTableRow(String line) {
        // Simple heuristic to detect table rows
        // Look for multiple spaces or tabs separating values
        return line.contains("  ") || line.contains("\t") || 
               Pattern.matches(".*\\s{2,}.*", line);
    }

    public byte[] convertTablesToExcel(List<String> tables) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        
        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        int sheetIndex = 1;
        for (String table : tables) {
            Sheet sheet = workbook.createSheet("Table " + sheetIndex++);
            String[] rows = table.split("\n");
            
            int rowNum = 0;
            for (String row : rows) {
                if (!row.trim().isEmpty()) {
                    Row excelRow = sheet.createRow(rowNum++);
                    String[] cells = row.split("\\s{2,}|\t");
                    
                    for (int i = 0; i < cells.length; i++) {
                        Cell cell = excelRow.createCell(i);
                        cell.setCellValue(cells[i].trim());
                        
                        // Apply header style to first row
                        if (rowNum == 1) {
                            cell.setCellStyle(headerStyle);
                        }
                    }
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < 10; i++) { // Auto-size first 10 columns
                sheet.autoSizeColumn(i);
            }
        }
        
        // Convert to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }
}
