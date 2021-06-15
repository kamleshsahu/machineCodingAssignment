package com.assignment.suprdaily.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReserveOrderResponse {

    @JsonProperty("code")
    public String code;
    @JsonProperty("data")
    public Data data;

}