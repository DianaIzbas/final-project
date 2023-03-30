package com.siit.finalproject.service;


import com.siit.finalproject.actuator.CompanyContributor;
import com.siit.finalproject.entity.DestinationEntity;
import com.siit.finalproject.entity.OrderEntity;
import com.siit.finalproject.enums.OrderEnum;
import com.siit.finalproject.exception.DataNotFound;
import com.siit.finalproject.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ShippingService {
    private final CompanyContributor companyContributor;
    private final OrderRepository orderRepository;

    public ShippingService(CompanyContributor companyContributor, OrderRepository orderRepository) {
        this.companyContributor = companyContributor;
        this.orderRepository = orderRepository;
    }

    @Async("threadPoolTaskExecutor")
    public void startDeliveries(DestinationEntity destination, List<Long> orderIds)
    {
        log.info("Starting " + orderIds.size()
                + " deliveries for " + destination.getName()
                + " on " + Thread.currentThread().getName()
                + " for " + destination.getDistance()
                + " km");
        try {
            Thread.sleep(destination.getDistance() * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int ordersNumber = 0;
        for (Long id : orderIds) {
            if (orderRepository.findById(id).get().getStatus() != OrderEnum.CANCELED) {
                ordersNumber++;
            }
        }

        companyContributor.setOverallProfit(companyContributor.getOverallProfit() + (destination.getDistance() * ordersNumber));

        for (Long id : orderIds) {
            try {
                updateOrderStatus(id, OrderEnum.DELIVERED);
            } catch (DataNotFound e) {
                e.printStackTrace();
            }
        }

        log.info(ordersNumber + " deliveries completed for " + destination.getName());
    }

    public void updateOrderStatus(Long orderId, OrderEnum orderEnum) throws DataNotFound
    {
        Optional<OrderEntity> orderEntityOptional = orderRepository.findById(orderId);
        if (orderEntityOptional.isEmpty()) {
            throw new DataNotFound(String.format("The student with id %s could not be found in database.", orderId));
        }
        OrderEntity order = orderEntityOptional.get();
        if (orderEnum != null) {
            order.setStatus(orderEnum);
        }
        orderRepository.save(order);
    }

}
