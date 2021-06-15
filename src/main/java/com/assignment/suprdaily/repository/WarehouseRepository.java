package com.assignment.suprdaily.repository;

import com.assignment.suprdaily.exception.DataNotAvailableException;
import com.assignment.suprdaily.exception.OrderReservationException;

import java.util.Date;

public interface WarehouseRepository {

    public int getQuantityAvailableByItemAndWarehouseAndDate(int wareHouseId, int itemId, Date date) throws DataNotAvailableException;

    public void updateQuantityAvailableByItemAndWarehouseAndDate(int wareHouseId, int itemId, Date date, int consumedQuantity) throws DataNotAvailableException, OrderReservationException;

    public int getAvailabilityByCategoryAmongAllWarehouse(String category, Date date) throws DataNotAvailableException;

    public void updateAvailabilityByCategoryAmongAllWarehouse(String category, Date date, int consumedQuantity) throws DataNotAvailableException, OrderReservationException;

}
