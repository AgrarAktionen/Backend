package com.aktionen.agrar.categorize.rest;

import com.aktionen.agrar.categorize.dao.CategoriesDao;
import com.aktionen.agrar.model.Item;
import com.aktionen.agrar.users.model.User;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/categories")
public class CategoriesResource {

    @Inject
    CategoriesDao categoriesDao;

    @Path("/getAll")
    @GET
    public List<Item> getSecond(){
        return categoriesDao.selectAll();
    }

    @GET
    @Path("/{primeCategory}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional

    public List<Item> getPrimeSelection(@PathParam("primeCategory") String primeCategory) {
        return categoriesDao.getPrime(primeCategory);
    }
    @GET
    @Path("/{primeCategory}/{secondCategory}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional

    public List<Item> getPrimeSelection(@PathParam("primeCategory") String primeCategory, @PathParam("secondCategory") String secondCategory) {
        return categoriesDao.getSecond(primeCategory, secondCategory);
    }
    @GET
    @Path("/{primeCategory}/{secondCategory}/{thirdCategory}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional

    public List<Item> getPrimeSelection(@PathParam("primeCategory") String primeCategory, @PathParam("secondCategory") String secondCategory, @PathParam("secondCategory") String thirdCategory) {
        return categoriesDao.getThird(primeCategory, secondCategory, thirdCategory);
    }


}
