package com.rta.filtercarlist.rest; 

import com.rta.filtercarlist.dto.CarBuyerIsWatchingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rta.filtercarlist.stream.CarSource;
import com.rta.filtercarlist.dto.Car;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/filtercarlist")
public class CarRest {

        @Autowired
        private CarSource source;

        private Map<String, String> vars = new HashMap<>();

        private RestTemplate carWatchService;

        private RestTemplate carStoreService;

        private String carWatchServiceUrl = new String("http://localhost/watchingcars/getwatchlist/{name}");

        private String carStoreUrlWatching = new String("http://localhost/api/carlistwatching/");

        private String carStoreUrlNotWatching = new String("http://localhost/api/carlistnotwatching/");



        public CarRest() {
                this.carWatchService = new RestTemplate();
                this.carWatchService.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                this.carStoreService = new RestTemplate();
                this.carStoreService.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }

        @RequestMapping(value = "/buyernotwatching/{name}",
                method = RequestMethod.GET,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<List<Car>> getCarsBuyerNotWatching(@PathVariable String name) throws URISyntaxException {
                CarBuyerIsWatchingDto carsBuyerIsWatchingDto = new CarBuyerIsWatchingDto();
                carsBuyerIsWatchingDto.setVins(this.getCarBuyerIsWatchingList(name));
                @SuppressWarnings("unchecked")
                List<Car> returns = this.carStoreService.postForObject(carStoreUrlNotWatching, carsBuyerIsWatchingDto, ((Class<List<Car>>)(Object)List.class), vars);
                HttpHeaders httpHeaders = new HttpHeaders();
                return new ResponseEntity<>(returns, httpHeaders, HttpStatus.OK);
        }

        @RequestMapping(value = "/buyerwatching/{name}",
                method = RequestMethod.GET,
                produces = MediaType.APPLICATION_JSON_VALUE)
        @SuppressWarnings("unchecked")
        public ResponseEntity<List<Car>> getCarsBuyerWatching(@PathVariable String name) throws URISyntaxException {
                CarBuyerIsWatchingDto carsBuyerIsWatchingDto = new CarBuyerIsWatchingDto();
                carsBuyerIsWatchingDto.setVins(this.getCarBuyerIsWatchingList(name));
                List<Car> returns = null;
                if(carsBuyerIsWatchingDto.getVins() == null || carsBuyerIsWatchingDto.getVins().isEmpty() ) {
                        returns = new ArrayList<>();
                } else {

                        returns = this.carStoreService.postForObject(this.carStoreUrlWatching, carsBuyerIsWatchingDto, ((Class<List<Car>>) (Object) List.class), vars);
                }
                HttpHeaders httpHeaders = new HttpHeaders();
                return new ResponseEntity<>(returns, httpHeaders, HttpStatus.OK);
        }

        private List<String> getCarBuyerIsWatchingList(String name) {
                return this.carWatchService.getForObject(carWatchServiceUrl, List.class, name);
        }
}
