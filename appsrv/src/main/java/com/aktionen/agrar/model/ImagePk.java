package com.aktionen.agrar.model;

import lombok.Data;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class ImagePk implements Serializable {
    
    @GeneratedValue
    private int imageId;
    private String prediction;

    public ImagePk(int imageId, String prediction) {

        this.imageId = imageId;
        this.prediction = prediction;

    }

    public ImagePk() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImagePk)) return false;
        ImagePk imagePk = (ImagePk) o;
        return imageId == imagePk.imageId &&
                prediction == imagePk.prediction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, prediction);
    }
}

