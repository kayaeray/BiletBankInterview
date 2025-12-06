package com.flightproviderconsumer.controller;


import com.flightproviderconsumer.dto.AllFlightsResponse;
import com.flightproviderconsumer.dto.SearchRequest;
import com.flightproviderconsumer.service.FlightSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
public class FlightController {

    @Autowired
    private final FlightSearchService flightSearchService;

    @PostMapping("search")
    public ResponseEntity<AllFlightsResponse> getAllFlights(@RequestBody SearchRequest searchRequest) {
        return new ResponseEntity<>(flightSearchService.getAllFlights(searchRequest), HttpStatus.OK);
    }

    @PostMapping("/cheapest")
    public ResponseEntity<AllFlightsResponse> getCheaphestFlights(@RequestBody SearchRequest searchRequest) {
        return new ResponseEntity<>(flightSearchService.getCheapestFlights(searchRequest), HttpStatus.OK);
    }

}