package com.assignment.suprdaily.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class Data {

    @JsonProperty("reserved")
    public Boolean reserved;
    @JsonProperty("message")
    public String message;

}