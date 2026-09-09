package com.prod.service;
 

 

import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.repository.zoo.ZooModel;

@Service
public class ImageInferenceService {

    private final ZooModel<Image, Classifications> model;

    public ImageInferenceService(ZooModel<Image, Classifications> model) {
        this.model = model;
    }

    public String predict(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Predictor<Image, Classifications> predictor = model.newPredictor()) {

            Image img = ImageFactory.getInstance().fromInputStream(is);
            Classifications result = predictor.predict(img);

            return result.best().getClassName() + " (" + 
                   String.format("%.2f%%", result.best().getProbability() * 100) + ")";

        } catch (Exception e) {
            throw new RuntimeException("Error processing image inference", e);
        }
    }
}
