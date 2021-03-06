package com.aktionen.agrar.dao;

import com.aktionen.agrar.model.APILink;
import com.aktionen.agrar.model.Item;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;

@Named
@Dependent
public class ItemDao {
    @Inject
    EntityManager em;
/*
        public void insertAll(List<Item> items, String name){
            APILink apiLink = em.createQuery("select a from APILink a where a.description = :desc", APILink.class)
                    .setParameter("desc", name)
                    .getSingleResult();
            for(Item item:items) {
                item.setApiLink(apiLink);

                em.persist(item);
                //em.merge(item);
                em.flush();
            }
        }

 */
    public void insert(Item item, String name){

        APILink apiLink = em.createQuery("select a from APILink a where a.description = :desc", APILink.class)
                .setParameter("desc", name)
                .getSingleResult();


        if((em.createQuery("select i from Item i where i.artikelnummer = :artikelnummer", Item.class)
                .setParameter("artikelnummer", item.getArtikelnummer())
                .getResultList().size()) > 0){


            em.createQuery("update Item i set i.hersteller = :hersteller where i.artikelnummer = :artikelnummer")
                    .setParameter("hersteller", item.getHersteller())
                    .setParameter("artikelnummer", item.getArtikelnummer()).executeUpdate();

            //em.remove(item);
            //item.setApiLink(apiLink);
            //em.merge(item);
            //em.flush();

/*
            if ((em.createQuery("select i from Item i where i.artikelnummer = :desc", Item.class)
                    .setParameter("desc", item.getArtikelnummer())
                    .getSingleResult().getArtikelnummer()).equals(item.getArtikelnummer())) {

                //item.setApiLink(apiLink);
                //em.merge(item);
                //em.flush();
            } else {
                item.setApiLink(apiLink);
                em.persist(item);
                em.flush();
            }


 */


        }else {
            item.setApiLink(apiLink);
            em.persist(item);
            em.flush();
        }



    }



    public List<Item> getAll() {
        return em.createQuery("select i from Item i ", Item.class).getResultList();
    }


    public Item add(@NotNull Item item) {
        return em.merge(item);
    }

    public Item get(int id) {
        return em.find(Item.class, id);

    }

    public void delete(Item item) {
        em.remove(item);
    }

}

