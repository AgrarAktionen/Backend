package com.aktionen.agrar.dao;


import ai.djl.ModelException;
import ai.djl.engine.Engine;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import com.aktionen.agrar.model.*;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Named
@Dependent
public class ImageDao {

    @Inject
    EntityManager em;

    public Image save(@NotNull Image image) {
        return em.merge(image);
    }

    public Image get(int id) {
        return em.find(Image.class, id);

    }

    public void insertAll(List<Image> images) throws TranslateException, IOException, ModelException, ImageReadException, URISyntaxException {

        //int currentId = 1000;
        final float[] MEAN = {103.939f, 116.779f, 123.68f};
        final float[] STD = {1f, 1f, 1f};

        //Implement an Function to detect all images which uploaded from POST Request
        int currentId = images.get(0).getId();
        for (Image predictedImage : images) {
            Image image = em.createQuery("select i from Image i where i.id = :ids", Image.class)
                    .setParameter("ids", currentId)
                    .getSingleResult();
            if (!image.equals(null)) {
                /*Classification Algorithm*/
                ai.djl.modality.cv.Image predictImage;
                try (InputStream is = new ByteArrayInputStream(image.getBytes())) {
                    predictImage = ImageFactory.getInstance().fromImage(Imaging.getBufferedImage(is));
                }

                if (predictImage != null) {
                    Criteria<ai.djl.modality.cv.Image, Classifications> criteria;
                    if ("TensorFlow".equals(Engine.getInstance().getEngineName())) {
                        Translator<ai.djl.modality.cv.Image, Classifications> translator = ImageClassificationTranslator.builder()
                                .addTransform(new Resize(224))
                                .addTransform(new Normalize(MEAN, STD))
                                .build();
                        criteria =
                                Criteria.builder()
                                        .setTypes(ai.djl.modality.cv.Image.class, Classifications.class)
                                        .optArtifactId("resnet")
                                        .optTranslator(translator)
                                        .optProgress(new ProgressBar())
                                        .build();
                    } else {
                        criteria =
                                Criteria.builder()
                                        .setTypes(ai.djl.modality.cv.Image.class, Classifications.class)
                                        .optArtifactId("resnet")
                                        .optProgress(new ProgressBar())
                                        .build();
                    }

                    try (ZooModel<ai.djl.modality.cv.Image, Classifications> model = ModelZoo.loadModel(criteria);
                         Predictor<ai.djl.modality.cv.Image, Classifications> predictor = model.newPredictor()) {

                        Classifications result = predictor.predict(predictImage);
                        /*Classification Algorithm*/
                        System.out.println(result);

                        predictedImage.setClassification(String.valueOf(result.best()));
                        predictedImage.setClassname(String.valueOf(result.best().getClassName()));
                        predictedImage.setProbability(result.best().getProbability());
                        //em.persist(item);
                        em.merge(predictedImage);
                        em.flush();
                        currentId++;
                    }
                } else {
                    predictedImage.setClassification("empty");
                    predictedImage.setClassname("empty");
                    predictedImage.setProbability(0.0);
                    //em.persist(item);
                    em.merge(predictedImage);
                    em.flush();
                    currentId++;
                }
            } else {
                currentId++;
                System.out.println("No Entity for ID found!");
            }
        }

    }


    public List<Image> getAll() {
        return em.createQuery("select i from Image i ", Image.class).getResultList();
    }


}
