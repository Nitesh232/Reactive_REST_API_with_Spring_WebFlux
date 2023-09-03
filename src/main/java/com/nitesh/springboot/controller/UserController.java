package com.nitesh.springboot.controller;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nitesh.springboot.binding.UserInfo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
public class UserController {
	
	
	@GetMapping(value = "user", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Mono<UserInfo>> getInfo(){
		
		UserInfo info = new UserInfo("Jayesh", new Date());
		
		Mono<UserInfo> monoInfo = Mono.just(info);
		
		return new ResponseEntity<Mono<UserInfo>>(monoInfo, HttpStatus.OK);
		
	}
	
	
	
	@GetMapping(value = "users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<Flux<UserInfo>> getUsers(){
		
		UserInfo info = new UserInfo("Jayesh", new Date());
		
		// Creating Stream for binding Object
		Stream<UserInfo> userStream = Stream.generate(() -> info);		
		
		// Create Flux object using Stream
		Flux<UserInfo> userFlux = Flux.fromStream(userStream);
		
		// Setting response interval
		Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(3));
		
		// Combine interval Flux and userFlux 
		Flux<Tuple2<UserInfo,Long>> zip = Flux.zip(userFlux, intervalFlux);
		
		// Getting Tuple value as T1
		Flux<UserInfo> map = zip.map(Tuple2::getT1);
		
		// Sending Response
		return new ResponseEntity<Flux<UserInfo>>(map, HttpStatus.OK);
		
	}

}
