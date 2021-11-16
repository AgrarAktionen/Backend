package com.aktionen.agrar.classification;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import com.aktionen.agrar.dao.ImageDao;
import com.aktionen.agrar.model.Image;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;


@Path("/image")
public class ImageResource {

    @Inject
    ImageDao imageDao;


    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Image> all() {
        return imageDao.getAll();
    }

    @GET
    @Path("/getImagesForView")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Image> getAlreadyUsedImages() {
        return imageDao.getAllAlreadyUsedImages();
    }



    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    @Transactional
    public Response uploadFile(MultipartFormDataInput input) throws IOException, URISyntaxException, ModelException, ImageReadException, TranslateException {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        for (InputPart inputPart : inputParts) {

                MultivaluedMap<String, String> header = inputPart.getHeaders();

                //convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class,null);

                byte [] bytes = IOUtils.toByteArray(inputStream);
            //write to Database
            Image image = new Image();
            image.setBytes(bytes);
            //Set the usability of the image to true
            image.setUsable(true);
            //This image wasn't used ever before
            image.setAlreadyUsed(false);
            imageDao.add(image);
            //com.aktionen.agrar.classification of the uploaded Image
            List<Image> imageList=imageDao.getAll();
            imageDao.insertAll(imageList);
            System.out.println("Directly inserted");

        }

        return Response.status(200)
                .entity("uploadFile is called, Uploaded file name : " + uploadForm.toString()).build();


    }
    //This function should be called after you opened the gridView of the previously uploaded pictures,
    //when you click on an item to select the similarity result of the clicked picture.
    @Path("/{id:[0-9]+}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Image getImageById(@PathParam("id") int id) {

        Image image = imageDao.get(id);
        image.setUsable(true);

        return imageDao.get(id);
    }
}
