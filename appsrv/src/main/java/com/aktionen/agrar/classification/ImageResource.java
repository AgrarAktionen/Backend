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
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;


@Path("/file")
public class ImageResource {

    @Inject
    ImageDao imageDao;

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
                imageDao.save(image);
                //com.aktionen.agrar.classification of the uploaded Image
                List<Image> imageList=imageDao.getAll();
                imageDao.insertAll(imageList);
                System.out.println("Done");



        }

        return Response.status(200)
                .entity("uploadFile is called, Uploaded file name : " + uploadForm.toString()).build();


    }
}
