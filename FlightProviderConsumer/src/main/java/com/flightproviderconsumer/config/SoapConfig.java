package com.flightproviderconsumer.config;

import org.springframework.boot.webservices.client.HttpWebServiceMessageSenderBuilder;
import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.time.Duration;

@Configuration
public class SoapConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.flightproviderconsumer.client.providera",
                "com.flightproviderconsumer.client.providerb");
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(WebServiceTemplateBuilder builder) {
        return builder.messageSenders(new HttpWebServiceMessageSenderBuilder()
                .setConnectTimeout(Duration.ofMinutes(10)).setReadTimeout(Duration.ofMinutes(10)).build()).build();
    }
}