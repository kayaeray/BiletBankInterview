package com.flightproviderconsumer.service;

import com.flightproviderconsumer.entity.RequestResponseLog;
import com.flightproviderconsumer.repository.RequestResponseLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestResponseLogService {

    private final RequestResponseLogRepository repository;

    public void log(RequestResponseLog log) {
        repository.save(log);
    }
}