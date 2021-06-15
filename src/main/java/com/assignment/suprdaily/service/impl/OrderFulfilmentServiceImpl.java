package com.assignment.suprdaily.service.impl;

import com.assignment.suprdaily.Entity.Item;
import com.assignment.suprdaily.Entity.OrderRequest;
import com.assignment.suprdaily.exception.OrderReservationException;
import com.assignment.suprdaily.repository.WarehouseRepository;
import com.assignment.suprdaily.service.OrderFulfilmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class OrderFulfilmentServiceImpl implements OrderFulfilmentService {
    private static String MESSAGE_INSUFFICIENT_QUANTITIES = "Insufficient quantities!";

    @Autowired
    WarehouseRepository warehouse;

    @Override
    public Boolean canFulfilOrder(OrderRequest orderRequest) {

        if (!areItemsAvailableInWareHouse(
                orderRequest.getWarehouseId(), orderRequest.getItems(), orderRequest.getDeliveryDate()))
            return false;

        if (!isQuantityByCategoryWithinThreshhold(orderRequest.getDeliveryDate(), orderRequest.getItems()))
            return false;

        return true;
    }

    @Override
    public void reserveOrder(OrderRequest orderRequest) throws OrderReservationException {

        if (!canFulfilOrder(orderRequest))
            throw new OrderReservationException(MESSAGE_INSUFFICIENT_QUANTITIES);

        Map<String, Integer> quantityByCategory = getQuantityByCategory(orderRequest.getItems());

        updateQuantityByCategoryAmongAllWarehouse(orderRequest.getDeliveryDate(), quantityByCategory);

        updateItemsQuantityInWarehouseOnGivenDate(orderRequest.getWarehouseId(),
                orderRequest.getItems(), orderRequest.getDeliveryDate());
    }

    private void updateItemsQuantityInWarehouseOnGivenDate(Integer warehouseId, List<Item> items, Date deliveryDate) {
        for (Item item : items) {
            warehouse.updateQuantityAvailableByItemAndWarehouseAndDate(warehouseId,
                    item.getItemId(), deliveryDate, item.getQuantity());
        }
    }

    private void updateQuantityByCategoryAmongAllWarehouse(Date date, Map<String, Integer> quantityByCategory) {
        for (Map.Entry<String, Integer> quantity : quantityByCategory.entrySet()) {
            warehouse.updateAvailabilityByCategoryAmongAllWarehouse(quantity.getKey(), date, quantity.getValue());
        }
    }

    private Boolean areItemsAvailableInWareHouse(Integer wareHouseId, List<Item> itemList, Date deliveryDate) {
        for (Item item : itemList) {
            int availQuantity =
                    warehouse.getQuantityAvailableByItemAndWarehouseAndDate(wareHouseId, item.getItemId(), deliveryDate);
            if (item.getQuantity() > availQuantity)
                return false;
        }
        return true;
    }

    private Boolean isQuantityByCategoryWithinThreshhold(Date date, List<Item> items) {
        Map<String, Integer> quantityByCategory = getQuantityByCategory(items);
        for (Map.Entry<String, Integer> entry : quantityByCategory.entrySet()) {
            int categoryThreshHold = warehouse.getAvailabilityByCategoryAmongAllWarehouse(entry.getKey(), date);
            if (entry.getValue() > categoryThreshHold)
                return false;
        }
        return true;
    }

    private Map<String, Integer> getQuantityByCategory(List<Item> items) {
        Map<String, Integer> quantityByCategory = new HashMap<>();
        for (Item item : items) {
            String category = item.getCategory();
            quantityByCategory.put(category, quantityByCategory.getOrDefault(category, 0) + item.getQuantity());
        }
        return quantityByCategory;
    }
}
