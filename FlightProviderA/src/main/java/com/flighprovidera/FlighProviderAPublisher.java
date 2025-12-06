package com.flighprovidera;

import com.flighprovidera.service.SearchService;
import jakarta.xml.ws.Endpoint;

public class FlighProviderAPublisher {

    private static String app_url = "http://localhost:9001/flightprovidera";

    public static void main(String[] args) {

        Endpoint.publish(app_url, new SearchService());
        System.out.println(" 🚀 App launched on : " + app_url);
    }

}