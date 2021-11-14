package com.aktionen.agrar.dao;

import com.aktionen.agrar.model.Image;
import com.aktionen.agrar.model.ImageStore;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;

@Named
@Dependent
public class ImageStoreDao {

    @Inject
    EntityManager em;

    public ImageStore add(@NotNull ImageStore imageStore) {
        return em.merge(imageStore);
    }

    public void delete(ImageStore imageStore) {
        em.remove(imageStore);
    }

    public List<ImageStore> getAll() {
        return em.createQuery("select i from ImageStore i ", ImageStore.class).getResultList();
    }

    public ImageStore get(int id) {
        return em.find(ImageStore.class, id);

    }
}
