package com.flighproviderb;

import com.flighproviderb.service.SearchService;
import jakarta.xml.ws.Endpoint;

public class FlighProviderBPublisher {
    private static String app_url = "http://localhost:9002/flightproviderb";

    public static void main(String[] args) {

        Endpoint.publish(app_url, new SearchService());
        System.out.println(" 🚀 App launched on : " + app_url);
    }
}