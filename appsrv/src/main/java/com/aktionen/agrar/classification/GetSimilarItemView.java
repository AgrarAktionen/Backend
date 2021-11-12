package com.aktionen.agrar.classification;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import com.aktionen.agrar.dao.GetSimilarItemDao;
import com.aktionen.agrar.dao.ImageDao;
import com.aktionen.agrar.model.Item;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.apache.commons.imaging.ImageReadException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Transactional
@Path("/getSimilarItemView")
public class GetSimilarItemView {

    @Inject
    Template getSimilarItems;

    @Inject
    GetSimilarItemDao getSimilarItemDao;

    @Inject
    ImageDao imageDao;

    @GET
    public TemplateInstance templateInstance() throws URISyntaxException, IOException, ImageReadException, ModelException, TranslateException {

        //GET Request of the classified images
        List<Item> items = getSimilarItemDao.getAll();
        return getSimilarItems.data("items", items);
    }
}
