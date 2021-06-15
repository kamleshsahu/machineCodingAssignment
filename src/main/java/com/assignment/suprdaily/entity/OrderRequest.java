
package com.assignment.suprdaily.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


@Getter
@Setter
@Builder
public class OrderRequest {

    @JsonProperty("customer_id")
    public Integer customerId;
    @JsonProperty("delivery_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date deliveryDate;
    @JsonProperty("warehouse_id")
    public Integer warehouseId;
    @JsonProperty("items")
    public List<Item> items = null;

}
