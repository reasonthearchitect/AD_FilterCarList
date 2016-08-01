package com.rta.filtercarlist.test.rest

import com.rta.filtercarlist.dto.Car
import com.rta.filtercarlist.rest.FilterCarListRest
import com.rta.filtercarlist.stream.CarSource

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification


class CarRestSpec extends Specification {

    FilterCarListRest carRest;

    def setup() {
        this.carRest        = new FilterCarListRest();
        this.carRest.source = Mock(CarSource);
    }

    def "simple test for the rest endpoint"() {

        when:
        ResponseEntity responseEntity = this.carRest.process([] as Car);

        then:
        1 * this.carRest.source.send(_);
        responseEntity != null;
        responseEntity.getStatusCode() == HttpStatus.CREATED;
    }
}
