package com.assignment.suprdaily.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CanFulfilOrderResponse{

    @JsonProperty("can_fulfil")
    public Boolean canFulfil;

    @JsonProperty("message")
    public String message;
}
