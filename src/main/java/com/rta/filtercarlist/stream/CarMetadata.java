package com.rta.filtercarlist.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface CarMetadata {
	 
		@Output("newbid")
    	MessageChannel post();
}