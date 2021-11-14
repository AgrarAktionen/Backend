package com.aktionen.agrar.model;

import lombok.Data;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;


@Data
@Entity
public class Image {

    @Id
    @GeneratedValue
    private int id;
    private String classification;
    private String classname;
    private double probability;

    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Lob
    private byte[] bytes;


}


