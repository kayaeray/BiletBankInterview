package com.flightproviderconsumer.repository;

import com.flightproviderconsumer.entity.RequestResponseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestResponseLogRepository extends JpaRepository<RequestResponseLog, Long> {
}