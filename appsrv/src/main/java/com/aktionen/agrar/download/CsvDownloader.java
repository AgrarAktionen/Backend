package com.aktionen.agrar.download;


import com.aktionen.agrar.dao.ItemDao;
import com.aktionen.agrar.dao.PriceDao;
import com.aktionen.agrar.model.Item;
import com.aktionen.agrar.model.Price;
import com.opencsv.bean.CsvToBeanBuilder;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.scheduler.Scheduled;
import org.apache.commons.io.IOUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.io.*;
import java.net.URL;
import java.util.List;

@QuarkusMain
public class CsvDownloader {

    String fileName = "file.csv";
    List<Item> items = createItemList();
    List<Price> prices = createPriceList();

    public CsvDownloader() throws FileNotFoundException {
    }

    public static void main(String[] args) throws IOException {
        //fetchCSV(); //uncomment to get new API CSV DATA
        Quarkus.run(args);
    }

    public List<Item> createItemList() throws FileNotFoundException {
        List<Item> itemList = new CsvToBeanBuilder(
                new FileReader(fileName))
                .withSeparator(';')          // custom CSV parser
                .withType(Item.class)
                .withSkipLines(1)
                .build()
                .parse();

        return itemList;
    }

    public List<Price> createPriceList() throws FileNotFoundException {
        List<Price> priceList = new CsvToBeanBuilder(
                new FileReader(fileName))
                .withSeparator(';')          // custom CSV parser
                .withType(Price.class)
                .withSkipLines(1)
                .build()
                .parse();

        return priceList;
    }


    public void fetchCSV() throws IOException {
        InputStream inputStream = new URL("https://www.faie.at/backend/export/index/agraraktionen.csv?feedID=68&hash=1bfdc5718d84ebfd191e9ee6617a7764").openStream();
        FileOutputStream fileOS = new FileOutputStream(fileName);
        int i = IOUtils.copy(inputStream, fileOS);

    }

    @Inject
    ItemDao itemDao;

    @Inject
    PriceDao priceDao;


    @Inject
    UserTransaction userTransaction;

    @PostConstruct
    public void init() throws SystemException {
        //set a timeout as high as you need
        userTransaction.setTransactionTimeout(3600);
    }

    @Transactional

    @Scheduled(every = "2s")
    public void process() throws IOException {
        //csvDownloader.fetchCSV();
        Item item = firstItemElement();
        Price price = firstPriceElement();
        itemDao.insert(item, "Faie");
        priceDao.insertAll(price, item);
        deleteFirstElement();

    }


    private void updateList() throws FileNotFoundException {

    }

    private void deleteFirstElement() {
        items.remove(0);
        prices.remove(0);
    }

    private Item firstItemElement() throws FileNotFoundException {
        if(items.isEmpty()) {
            items = createItemList();
        }
        Item item = items.get(0);

        return item;
    }

    private Price firstPriceElement() throws FileNotFoundException {
        if(prices.isEmpty()) {
            prices = createPriceList();
        }
        Price price = prices.get(0);
        return price;
    }
}