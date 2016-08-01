package com.rta.filtercarlist.dto;


import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponseBuyerWatching {

    private List<Car> cars;

    Map<String, Bid> highestBidMap;
}
