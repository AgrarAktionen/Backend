package com.aktionen.agrar.categorize.dao;

import com.aktionen.agrar.model.APILink;
import com.aktionen.agrar.model.Item;


import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

@Dependent
@Named
public class CategoriesDao {

    @Inject
    EntityManager em;

    public List<Item> selectAll(){
        List<Item> itemList = em.createQuery("SELECT i from Item i", Item.class).getResultList();
        return itemList;
    }
    //____________________________Functions for getting the Items of the wanted category/categories____________________________//
    public List<Item> getPrime(String primeCategory) {
        primeCategory.replace("%20", " ");
        List<Item> itemList = em.createQuery("select i from Item i where i.primeCategory = :primcat", Item.class)
                .setParameter("primcat", primeCategory)
                .getResultList();

        return itemList;
    }

    public List<Item> getSecond(String primeCategory, String secondCategory) {
        secondCategory.replace("%20", " ");
        List<Item> itemList = em.createQuery("select i from Item i where i.primeCategory = :primcat and i.secondCategory =:seccat", Item.class)
                .setParameter("primcat", primeCategory)
                .setParameter("seccat", secondCategory)
                .getResultList();

        return itemList;
    }
    public List<Item> getThird(String primeCategory, String secondCategory, String thirdCategory) {
        thirdCategory.replace("%20", " ");
        List<Item> itemList = em.createQuery("select i from Item i where i.primeCategory = :primcat and i.secondCategory =:secocat and i.thirdCategory =:thircat", Item.class)
                .setParameter("primcat", primeCategory)
                .setParameter("secocat", secondCategory)
                .setParameter("thircat", thirdCategory)
                .getResultList();

        return itemList;
    }

    //___________________________________Functions for selecting the correct categories___________________________________//

    public String selectPrime(Item item) {

        String output= "";
        String primeCategory = item.getKategoriepfad();

        int posFrom = primeCategory.indexOf("");
        if (posFrom != -1) //if found char
        {
            int posTo = primeCategory.indexOf(">", posFrom + 1);
            if (posTo != -1) //if found char
            {
                output = primeCategory.substring(posFrom, posTo - posFrom);


            }

        }

        return output;
    }
    public String selectSecond(Item item) {

        String output = "";
        String primeCategory = item.getPrimeCategory();
        String itemCategoryPath = item.getKategoriepfad();
        String secondCategory = itemCategoryPath.replaceFirst(primeCategory, "");
        //String correctedSecondCategory = secondCategory.replaceFirst(">","");
        String correctedSecondCategory = secondCategory.substring(1);

        if(correctedSecondCategory.equals(">") || correctedSecondCategory.equals(">>") || correctedSecondCategory.equals("")){
            output = primeCategory;
        }else {
            int posFrom = correctedSecondCategory.indexOf("");
            if (posFrom != -1) //if found char
            {
                int posTo = correctedSecondCategory.indexOf(">", posFrom + 1);
                if (posTo != -1) //if found char
                {
                    output = correctedSecondCategory.substring(posFrom, posTo - posFrom);


                }
            }
        }
        return output;

    }
    public String selectThird(Item item) {

        String output = "";
        String primeCategory = item.getPrimeCategory();
        String itemCategoryPath = item.getKategoriepfad().replaceFirst(primeCategory, "");
        String secondCategory = item.getSecondCategory();
        String thirdCategory = itemCategoryPath.replace(secondCategory, "");
        //String correctedThirdCategory = thirdCategory.replaceFirst(">", "");
        String correctedThirdCategory = thirdCategory.substring(2);
        if(correctedThirdCategory.equals(">") || correctedThirdCategory.equals(">>") || correctedThirdCategory.equals("")){
            output = secondCategory;
        }else {
            int posFrom = correctedThirdCategory.indexOf("");
            if (posFrom != -1) //if found char
            {
                int posTo = correctedThirdCategory.indexOf(">", posFrom + 1);
                if (posTo != -1) //if found char
                {
                    output = correctedThirdCategory.substring(posFrom, posTo - posFrom);


                }
            }
        }
        return output;
    }
    //____________________________________________________________________________________________________________________//
}
