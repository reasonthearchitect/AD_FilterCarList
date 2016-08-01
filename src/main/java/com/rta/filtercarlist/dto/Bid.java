package com.rta.filtercarlist.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Bid implements Serializable {

    private String vin;

    private String id;

    private BigDecimal amount;
}
