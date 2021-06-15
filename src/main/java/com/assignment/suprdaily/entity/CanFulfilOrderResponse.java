package com.assignment.suprdaily.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CanFulfilOrderResponse{

    @JsonProperty("can_fulfil")
    public Boolean canFulfil;
}
