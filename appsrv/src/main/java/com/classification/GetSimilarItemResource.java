package com.classification;

import com.aktionen.agrar.dao.GetSimilarItemDao;
import com.aktionen.agrar.model.Item;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Transactional
@Produces(MediaType.APPLICATION_JSON)
@Path("/similarItems")
public class GetSimilarItemResource {

    @Inject
    GetSimilarItemDao getSimilarItemDao;


    @GET
    @Path("/")
    public List<Item> all() {
        return getSimilarItemDao.getAll();
    }
}
