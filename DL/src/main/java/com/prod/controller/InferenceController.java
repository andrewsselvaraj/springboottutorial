package com.prod.controller;
 

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.prod.service.ImageInferenceService;

@RestController
@RequestMapping("/api/v1/vision")
public class InferenceController {

    private final ImageInferenceService inferenceService;

    public InferenceController(ImageInferenceService inferenceService) {
        this.inferenceService = inferenceService;
    }

    @PostMapping("/classify")
    public ResponseEntity<String> classifyImage(@RequestParam("file") MultipartFile file) {
        String classification = inferenceService.predict(file);
        return ResponseEntity.ok(classification);
    }
}