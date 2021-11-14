package com.aktionen.agrar.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Data
@Entity
public class ImageStore {

    @Id
    @GeneratedValue
    private int id;
    private String classification;
    private String classname;
    private double probability;

    @Lob
    private byte[] bytes;
}