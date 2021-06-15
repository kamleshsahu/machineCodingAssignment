
package com.assignment.suprdaily.Entity;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
