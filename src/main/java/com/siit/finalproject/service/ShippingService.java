package com.siit.finalproject.service;


import com.siit.finalproject.entity.DestinationEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShippingService
{



    @Async("threadPoolTaskExecutor")
    public void startDeliveries(DestinationEntity destination, List<Long> orderIds)
    {
        log.info("Starting " + orderIds.size()
                + "deliveries for " + destination.getName()
                + " on " + Thread.currentThread().getName()
                +" for " + destination.getDistance()
                + " km");
        try {
            Thread.sleep(destination.getDistance()* 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("");
    }

}
