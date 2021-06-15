
package com.assignment.suprdaily.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Item {

    @JsonProperty("item_id")
    public Integer itemId;
    @JsonProperty("item_name")
    public String itemName;
    @JsonProperty("category")
    public String category;
    @JsonProperty("quantity")
    public Integer quantity;

}
