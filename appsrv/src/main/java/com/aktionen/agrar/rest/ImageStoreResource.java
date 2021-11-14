package com.aktionen.agrar.rest;


import com.aktionen.agrar.dao.ImageDao;
import com.aktionen.agrar.dao.ImageStoreDao;
import com.aktionen.agrar.model.Image;
import com.aktionen.agrar.model.ImageStore;
import com.aktionen.agrar.model.Item;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/storedImages")

public class ImageStoreResource {

    @Inject
    ImageStoreDao imageStoreDao;

    @Inject
    ImageDao imageDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    /*
    *   This function adds an the first image from the IMAGE table into the IMAGESTORE table.
    *   The function also deletes the first image of the IMAGE table to ensure that in the IMAGE table
    *   there is at least one entity.
    *   The function also returns the image which is added to the IMAGESTORE table.
    */
    public ImageStore addImageToImageStore() {
        ImageStore imageStore = new ImageStore();
        List<Image> images = imageDao.getAll();
        Image image = images.get(0);
        imageStore.setClassname(image.getClassname());
        imageStore.setClassification(image.getClassification());
        imageStore.setProbability(image.getProbability());
        imageStore.setBytes(image.getBytes());
        //Deletes the last uploaded Image
        imageDao.delete(image);
        return imageStoreDao.add(imageStore);
    }
    @GET
    @Path("/getAll")
    /*
     *  This function simply returns all the entities from the IMAGESTORE table.
     *  -> IDEA BEHIND: To be able to show the previously uploaded pictures from the mobile application
     */
    public List<ImageStore> all() {
        return imageStoreDao.getAll();
    }

    @Path("/{id:[0-9]+}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    /*
     *  This function grabs an entity from the IMAGESTORE table by it's ID and returns the selected entity.
     *  The function also creates an new image to save in the IMAGE table again.
     *  -> IDEA BEHIND: This function "http://localhost:8080/api/storedImages/$id" should get called if the user,
     *                  selects an previously uploaded picture which is shown in a gridview on the mobile application.
     *                  By selecting an previously uploaded picture, the url "http://localhost:8080/api/similarItems"
     *                  should be called to get the similar items of this picture.
     */
    public ImageStore getImageFromStore(@PathParam("id") int id) {
        //check if the IMAGE table is empty, otherwise delete all entities
        List<Image> deletableImages = imageDao.getAll();
        for(int i = 0; i < deletableImages.size(); i++){
            imageDao.delete(deletableImages.get(i));
        }
        //Select a previously uploaded picture by it's ID
        ImageStore selectedItemStore = imageStoreDao.get(id);
        //Create a new Image, by setting it's attributes with the data from the selected previously uploaded picture
        Image image = new Image();
        image.setClassname(selectedItemStore.getClassname());
        image.setClassification(selectedItemStore.getClassification());
        image.setBytes(selectedItemStore.getBytes());
        image.setProbability(selectedItemStore.getProbability());
        //Add new Image to the IMAGE table
        imageDao.add(image);
        return imageStoreDao.get(id);
    }

}
