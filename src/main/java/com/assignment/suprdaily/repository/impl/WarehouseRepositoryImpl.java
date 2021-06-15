package com.assignment.suprdaily.repository.impl;


import com.assignment.suprdaily.repository.WarehouseRepository;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class WarehouseRepositoryImpl implements WarehouseRepository {

    Map<Integer, Map<Integer, Map<String, Integer>>> wareHouseData = new HashMap<>();
    Map<String, Map<String, Integer>> quantityLeftByCategory = new HashMap<>();

    WarehouseRepositoryImpl() throws Exception {
        addDummyData();
        addDummyCategoryData();
    }

    @Override
    public int getQuantityAvailableByItemAndWarehouseAndDate(int wareHouseId, int itemId, Date date) {
        return wareHouseData.get(wareHouseId).get(itemId).get(dateToString(date));
    }

    @Override
    public void updateQuantityAvailableByItemAndWarehouseAndDate(int wareHouseId, int itemId, Date date, int consumedQuantity) {
        int quantityAvailable = wareHouseData.get(wareHouseId).get(itemId).get(dateToString(date));
        wareHouseData.get(wareHouseId).get(itemId).put(dateToString(date), quantityAvailable - consumedQuantity);
    }

    @Override
    public int getAvailabilityByCategoryAmongAllWarehouse(String category, Date date) {
        return quantityLeftByCategory.get(category).get(dateToString(date));
    }

    @Override
    public void updateAvailabilityByCategoryAmongAllWarehouse(String category, Date date, int consumedQuantity) {
        int quantityAvailableByCategory = quantityLeftByCategory.get(category).get(dateToString(date));
        quantityLeftByCategory.get(category).put(dateToString(date), quantityAvailableByCategory - consumedQuantity);
    }


    private void addDummyData() throws Exception {

        Map<String, Integer> quantityByDate = getQuantityByDate();

        Map<Integer, Map<String, Integer>> itemData = new HashMap<>();
        itemData.put(1, quantityByDate);
        itemData.put(2, quantityByDate);
        itemData.put(3, quantityByDate);
        wareHouseData.put(100, itemData);
        wareHouseData.put(101, itemData);
    }

    private void addDummyCategoryData() throws Exception {
        Map<String, Integer> quantityByDate = getQuantityByDate();
        quantityLeftByCategory.put("F_N_V", quantityByDate);
        quantityLeftByCategory.put("Grocery", quantityByDate);
    }

    private Map<String, Integer> getQuantityByDate() throws ParseException {
        String date1 = "2021-06-14";
        String date2 = "2021-06-15";

        Map<String, Integer> quantityByDate = new HashMap<>();
        quantityByDate.put(date1, 100);
        quantityByDate.put(date2, 150);
        return quantityByDate;
    }

    private String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        return strDate;
    }

}
