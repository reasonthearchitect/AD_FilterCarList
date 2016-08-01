package com.rta.filtercarlist.dto;

import lombok.Data;

import java.util.List;

@Data
public class CarsNotBeingWatchedListDto {

    private List<Car> carsNotBeingWatched;
}
