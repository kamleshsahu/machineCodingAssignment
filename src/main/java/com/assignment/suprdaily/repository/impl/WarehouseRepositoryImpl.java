package com.assignment.suprdaily.repository.impl;


import com.assignment.suprdaily.exception.DataNotAvailableException;
import com.assignment.suprdaily.exception.OrderReservationException;
import com.assignment.suprdaily.repository.WarehouseRepository;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class WarehouseRepositoryImpl implements WarehouseRepository {

   private final Map<Integer, Map<Integer, Map<String, Integer>>> wareHouseData;

   private final Map<String, Map<String, Integer>> quantityLeftByCategory;

    WarehouseRepositoryImpl() {
        wareHouseData = new HashMap<>();
        quantityLeftByCategory = new HashMap<>();
        addDummyData();
        addDummyCategoryData();
    }

    @Override
    public int getQuantityAvailableByItemAndWarehouseAndDate(int wareHouseId, int itemId, Date date) throws DataNotAvailableException {
        String dateToString = dateToString(date);

        areParamsValid(wareHouseId,itemId,dateToString);

        return wareHouseData.get(wareHouseId).get(itemId).get(dateToString);
    }

    @Override
    public void updateQuantityAvailableByItemAndWarehouseAndDate(int wareHouseId, int itemId, Date date, int consumedQuantity) throws DataNotAvailableException, OrderReservationException {
        String dateToString = dateToString(date);
        areParamsValid(wareHouseId,itemId,dateToString);
        int quantityAvailable = wareHouseData.get(wareHouseId).get(itemId).get(dateToString);
        isValidUpdate(quantityAvailable,consumedQuantity,false);
        wareHouseData.get(wareHouseId).get(itemId).put(dateToString, quantityAvailable - consumedQuantity);
    }

    @Override
    public int getAvailabilityByCategoryAmongAllWarehouse(String category, Date date) throws DataNotAvailableException {
        String dateToString = dateToString(date);
        areParamsValid(category,dateToString);
        return quantityLeftByCategory.get(category).get(dateToString);
    }

    @Override
    public void updateAvailabilityByCategoryAmongAllWarehouse(String category, Date date, int consumedQuantity) throws DataNotAvailableException, OrderReservationException {
        String dateToString = dateToString(date);
        areParamsValid(category,dateToString);
        int quantityAvailableByCategory = quantityLeftByCategory.get(category).get(dateToString(date));
        isValidUpdate(quantityAvailableByCategory,consumedQuantity,true);
        quantityLeftByCategory.get(category).put(dateToString(date), quantityAvailableByCategory - consumedQuantity);
    }

    private void areParamsValid( int wareHouseId,int itemId, String date) throws DataNotAvailableException {
        if(!wareHouseData.containsKey(wareHouseId)){
            String message = String.format("WareHouse:%s not present",wareHouseId);
            throw new DataNotAvailableException(message);
        }
        else if(!wareHouseData.get(wareHouseId).containsKey(itemId)){
            String message = String.format("item:%s not present in WareHouse:%s",itemId,wareHouseId);
            throw new DataNotAvailableException(message);
        }
        else if(!wareHouseData.get(wareHouseId).get(itemId).containsKey(date)){
            String message = String.format("item:%s not present in WareHouse:%s for date:%s",itemId,wareHouseId,date);
            throw new DataNotAvailableException(message);
        }
    }

    private void isValidUpdate(int quantityAvailable, int consumedQuantity, boolean isCateoryLimitCheck) throws OrderReservationException {
        if(consumedQuantity>quantityAvailable) {
            String message =
                    String.format("%s available:%s, required:%s", quantityAvailable, consumedQuantity,
                            (isCateoryLimitCheck)?"Category Limit Exceed":"Quantity unavailable in WareHouse");
            throw new OrderReservationException(message);
        }
    }

    private void areParamsValid(String category, String date) throws DataNotAvailableException {
        if(!quantityLeftByCategory.containsKey(category)){
            String message = String.format("Category:%s not present",category);
            throw new DataNotAvailableException(message);
        }
        else if(!quantityLeftByCategory.get(category).containsKey(date)){
            String message = String.format("category:%s details not present for date:%s",category,date);
            throw new DataNotAvailableException(message);
        }
    }

    private void addDummyData() {

        Map<String, Integer> quantityByDate = getQuantityByDate();

        Map<Integer, Map<String, Integer>> itemData = new HashMap<>();
        itemData.put(1, quantityByDate);
        itemData.put(2, quantityByDate);
        itemData.put(3, quantityByDate);
        wareHouseData.put(100, itemData);
        wareHouseData.put(101, itemData);
    }

    private void addDummyCategoryData() {
        Map<String, Integer> quantityByDate = getQuantityByDate();
        quantityLeftByCategory.put("F_N_V", quantityByDate);
        quantityLeftByCategory.put("Grocery", quantityByDate);
    }

    private Map<String, Integer> getQuantityByDate() {
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
