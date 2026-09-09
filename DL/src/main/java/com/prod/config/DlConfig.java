package com.prod.config;

import ai.djl.Application;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DlConfig {

    @Bean
    public ZooModel<Image, Classifications> imageClassificationModel() throws Exception {
        Criteria<Image, Classifications> criteria = Criteria.builder()
                .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                .setTypes(Image.class, Classifications.class)
                .optEngine("PyTorch")               // Explicitly use PyTorch engine
                .optFilter("flavor", "v1")           // DJL PyTorch ResNet standard filter
                .optProgress(new ProgressBar())
                .build();

        return criteria.loadModel();
    }
}