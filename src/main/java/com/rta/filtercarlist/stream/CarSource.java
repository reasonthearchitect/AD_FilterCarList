package com.rta.filtercarlist.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.rta.filtercarlist.dto.Car;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import lombok.extern.java.Log;

@Log
@Component
@EnableBinding(CarMetadata.class)
public class CarSource {

        @Autowired @Qualifier("newbid")
        private MessageChannel post;

        @Autowired
        ObjectMapper mapper;

        public void send(Car car) {
                try {
                        Message<?> message = MessageBuilder.withPayload(
                                        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(car)
                                )
                                .setHeader("contentType", "application/json")
                                .build();
                        post.send(message);
                } catch (Exception ex) {
                        log.log(Level.SEVERE, "Error trying to send a message to a queue: ", ex);
                }        
        }

}
