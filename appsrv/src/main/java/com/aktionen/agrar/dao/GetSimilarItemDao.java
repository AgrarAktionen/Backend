package com.aktionen.agrar.dao;

import com.aktionen.agrar.model.Image;
import com.aktionen.agrar.model.Item;
import com.aktionen.agrar.model.PredictedItem;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

@Named
@Dependent
public class GetSimilarItemDao {

    @Inject
    EntityManager em;

    public List<Item> getAll() { //This just selects the first Image from the POST Request


        List<Image> imageList = em.createQuery("select i from Image i").getResultList();
        //System.out.println(imageList.get(0).getClassification().startsWith("probability:"));
        //System.out.println(imageList.get(0).getClassification().startsWith("probability:"));

        /*
            List<PredictedItem> predictedItemList = em.createQuery("select p from PredictedItem p where p.predictedItemPk.prediction = :prediction", PredictedItem.class)
                    .setParameter("prediction", imageList.get(0).getClassification())
                    .getResultList();

         */
        List<PredictedItem> predictedItemList = em.createQuery("select p from PredictedItem p where p.classname = :classname", PredictedItem.class)
                .setParameter("classname", imageList.get(0).getClassname())
                .getResultList();

            List<Item> itemList = new LinkedList<>();


            for (PredictedItem predictedItem : predictedItemList) {
                if(predictedItem.getProbability() >= 0.9) { //Minimal Probability

                    Item item = em.createQuery("select i from Item i where i.itemId = :predictedItemId", Item.class)
                            .setParameter("predictedItemId", predictedItem.getPredictedItemPk().getItemId())
                            .getSingleResult();
                    itemList.add(item);
                    System.out.println(itemList);
                }

            }

            return itemList;

        }


}