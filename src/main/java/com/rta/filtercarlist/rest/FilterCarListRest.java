package com.rta.filtercarlist.rest; 

import com.rta.filtercarlist.dto.Bid;
import com.rta.filtercarlist.dto.CarBuyerIsWatchingDto;
import com.rta.filtercarlist.dto.ResponseBuyerWatching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.rta.filtercarlist.stream.CarSource;
import com.rta.filtercarlist.dto.Car;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.*;

@RestController
@RequestMapping("/filtercarlist")
public class FilterCarListRest {

        @Autowired
        private CarSource source;

        private Map<String, String> vars = new HashMap<>();

        private RestTemplate carWatchService;

        private RestTemplate carStoreService;

        private RestTemplate bidStoreService;

        @Value("${demo.domainname}")
        private String domainname;

        private String carWatchServiceUrl;

        private String carStoreUrlWatching;

        private String carStoreUrlNotWatching;

        private String bidStoreUrl;



        public FilterCarListRest() {
                this.carWatchService = new RestTemplate();
                this.carWatchService.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                this.carStoreService = new RestTemplate();
                this.carStoreService.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                this.bidStoreService = new RestTemplate();
                this.bidStoreService.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }

        @PostConstruct
        public void setupurls() {
            String http = "http://";
            this.carWatchServiceUrl       = http + domainname + "/watchingcars/getwatchlist/{name}";
            this.carStoreUrlWatching      = http + domainname + "/api/carlistwatching/";
            this.carStoreUrlNotWatching   = http + domainname + "/api/carlistnotwatching/";
            this.bidStoreUrl              = http + domainname + "/bidstore/getbidsforlist/";
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
        public ResponseEntity<ResponseBuyerWatching> getCarsBuyerWatching(@PathVariable String name) throws URISyntaxException {
            CarBuyerIsWatchingDto carsBuyerIsWatchingDto = new CarBuyerIsWatchingDto();
            carsBuyerIsWatchingDto.setVins(this.getCarBuyerIsWatchingList(name));
            ResponseBuyerWatching responseBuyerWatching = new ResponseBuyerWatching();
            if(carsBuyerIsWatchingDto.getVins() == null || carsBuyerIsWatchingDto.getVins().isEmpty() ) {
                responseBuyerWatching.setCars(new ArrayList<>());
                responseBuyerWatching.setHighestBidMap(new HashMap<>());
            } else {
                responseBuyerWatching.setCars( this.carStoreService.postForObject(this.carStoreUrlWatching, carsBuyerIsWatchingDto, ((Class<ArrayList<Car>>) (Object) ArrayList.class), vars));
                responseBuyerWatching.setHighestBidMap( this.bidStoreService.postForObject(this.bidStoreUrl, carsBuyerIsWatchingDto.getVins(), ((Class<Map<String, Bid>>) (Object) Map.class), vars));
            }
            HttpHeaders httpHeaders = new HttpHeaders();
            return new ResponseEntity<>(responseBuyerWatching, httpHeaders, HttpStatus.OK);
        }

        private List<String> getCarBuyerIsWatchingList(String name) {
                return this.carWatchService.getForObject(carWatchServiceUrl, List.class, name);
        }
}
