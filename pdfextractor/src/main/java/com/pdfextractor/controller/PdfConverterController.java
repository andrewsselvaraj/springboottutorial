package com.pdfextractor.controller;

import com.example.pdfconverter.service.PdfToExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class PdfConverterController {

    @Autowired
    private com.pdfextractor.service.PdfToExcelService pdfToExcelService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
            return "redirect:/";
        }

        if (!file.getContentType().equals("application/pdf")) {
            redirectAttributes.addFlashAttribute("error", "Please upload a PDF file.");
            return "redirect:/";
        }

        try {
            // Convert PDF to Excel
            byte[] excelData = pdfToExcelService.convertPdfToExcel(file);
            
            // Store the Excel data in session for download
            redirectAttributes.addFlashAttribute("excelData", excelData);
            redirectAttributes.addFlashAttribute("originalFileName", file.getOriginalFilename());
            redirectAttributes.addFlashAttribute("success", "PDF converted to Excel successfully!");
            
            return "redirect:/result";
            
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error processing PDF file: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/result")
    public String result(Model model) {
        return "result";
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel(@SessionAttribute("excelData") byte[] excelData,
                                              @SessionAttribute("originalFileName") String originalFileName) {
        
        String fileName = originalFileName.replace(".pdf", ".xlsx");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    @PostMapping("/extract-tables")
    public String extractTables(@RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
            return "redirect:/";
        }

        if (!file.getContentType().equals("application/pdf")) {
            redirectAttributes.addFlashAttribute("error", "Please upload a PDF file.");
            return "redirect:/";
        }

        try {
            // Extract tables from PDF
            List<String> tables = pdfToExcelService.extractTablesFromPdf(file);
            
            if (tables.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No tables found in the PDF file.");
                return "redirect:/";
            }
            
            // Convert tables to Excel
            byte[] excelData = pdfToExcelService.convertTablesToExcel(tables);
            
            // Store the Excel data in session for download
            redirectAttributes.addFlashAttribute("excelData", excelData);
            redirectAttributes.addFlashAttribute("originalFileName", file.getOriginalFilename());
            redirectAttributes.addFlashAttribute("success", "Tables extracted and converted to Excel successfully!");
            redirectAttributes.addFlashAttribute("tableCount", tables.size());
            
            return "redirect:/result";
            
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error processing PDF file: " + e.getMessage());
            return "redirect:/";
        }
    }
}
