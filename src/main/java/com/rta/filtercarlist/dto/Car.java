package com.rta.filtercarlist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Car {

    private String id;
    private String vin;
    private String seller;
    private BigDecimal year;
    private String make;
    private String model;
    private BigDecimal odometer;
    private BigDecimal openingBid;
    private BigDecimal stockNumber;
    private String comments;
    private String additionalComments;
    private BigDecimal highestBid;
    private boolean myBid;
}

