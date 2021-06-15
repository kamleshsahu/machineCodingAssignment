package com.assignment.suprdaily.repository;

import java.util.Date;

public interface WarehouseRepository {

    public int getQuantityAvailableByItemAndWarehouseAndDate(int wareHouseId, int itemId, Date date);

    public void updateQuantityAvailableByItemAndWarehouseAndDate(int wareHouseId, int itemId, Date date, int consumedQuantity);

    public int getAvailabilityByCategoryAmongAllWarehouse(String category, Date date);

    public void updateAvailabilityByCategoryAmongAllWarehouse(String category, Date date, int consumedQuantity);

}
