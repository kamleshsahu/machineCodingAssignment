package com.assignment.suprdaily.service.impl;

import com.assignment.suprdaily.entity.Item;
import com.assignment.suprdaily.entity.OrderRequest;
import com.assignment.suprdaily.exception.DataNotAvailableException;
import com.assignment.suprdaily.exception.OrderReservationException;
import com.assignment.suprdaily.repository.WarehouseRepository;
import com.assignment.suprdaily.service.OrderFulfilmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.assignment.suprdaily.constants.AppConstains.MESSAGE_INSUFFICIENT_QUANTITIES;


@Service
public class OrderFulfilmentServiceImpl implements OrderFulfilmentService {

    @Autowired
    private WarehouseRepository warehouse;

    @Override
    public Boolean canFulfilOrder(OrderRequest orderRequest) throws DataNotAvailableException {

        if (!areItemsAvailableInWareHouse(
                orderRequest.getWarehouseId(), orderRequest.getItems(), orderRequest.getDeliveryDate()))
            return false;

        if (!isQuantityByCategoryWithinLimits(orderRequest.getDeliveryDate(), orderRequest.getItems()))
            return false;

        return true;
    }

    @Override
    public void reserveOrder(OrderRequest orderRequest) throws OrderReservationException, DataNotAvailableException {

        if (!canFulfilOrder(orderRequest))
            throw new OrderReservationException(MESSAGE_INSUFFICIENT_QUANTITIES);

        Map<String, Integer> quantityByCategory = getQuantityByCategory(orderRequest.getItems());

        updateQuantityByCategoryAmongAllWarehouse(orderRequest.getDeliveryDate(), quantityByCategory);

        updateItemsQuantityInWarehouseOnGivenDate(orderRequest.getWarehouseId(),
                orderRequest.getItems(), orderRequest.getDeliveryDate());
    }

    private void updateItemsQuantityInWarehouseOnGivenDate(Integer warehouseId, List<Item> items, Date deliveryDate)
            throws DataNotAvailableException, OrderReservationException {
        for (Item item : items) {
            warehouse.updateQuantityAvailableByItemAndWarehouseAndDate(warehouseId,
                    item.getItemId(), deliveryDate, item.getQuantity());
        }
    }

    private void updateQuantityByCategoryAmongAllWarehouse(Date date, Map<String, Integer> quantityByCategory)
            throws DataNotAvailableException, OrderReservationException {
        for (Map.Entry<String, Integer> quantity : quantityByCategory.entrySet()) {
            warehouse.updateAvailabilityByCategoryAmongAllWarehouse(quantity.getKey(), date, quantity.getValue());
        }
    }

    private Boolean areItemsAvailableInWareHouse(Integer wareHouseId, List<Item> itemList, Date deliveryDate)
            throws DataNotAvailableException {
        for (Item item : itemList) {
            int availQuantity =
                    warehouse.getQuantityAvailableByItemAndWarehouseAndDate(wareHouseId, item.getItemId(), deliveryDate);
            if (item.getQuantity() > availQuantity)
                return false;
        }
        return true;
    }

    private Boolean isQuantityByCategoryWithinLimits(Date date, List<Item> items) throws DataNotAvailableException {
        Map<String, Integer> quantityByCategory = getQuantityByCategory(items);
        for (Map.Entry<String, Integer> entry : quantityByCategory.entrySet()) {
            int categoryQuantityLeft = warehouse.getAvailabilityByCategoryAmongAllWarehouse(entry.getKey(), date);
            if (entry.getValue() > categoryQuantityLeft)
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
