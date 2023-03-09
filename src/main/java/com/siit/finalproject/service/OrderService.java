package com.siit.finalproject.service;

import com.siit.finalproject.convertDto.OrderConverter;
import com.siit.finalproject.dto.DestinationResponse;
import com.siit.finalproject.dto.OrderDto;
import com.siit.finalproject.entity.DestinationEntity;
import com.siit.finalproject.entity.OrderEntity;
import com.siit.finalproject.repository.DestinationRepository;
import com.siit.finalproject.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final DestinationRepository destinationRepository;
    private final DestinationResponse destinationResponse;

    public OrderService(OrderRepository orderRepository, OrderConverter orderConverter, DestinationRepository destinationRepository, DestinationResponse destinationResponse) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.destinationRepository = destinationRepository;

        this.destinationResponse = destinationResponse;
    }


    public void saveOrders(BufferedReader br) throws IOException
    {

        OrderDto orderDto = new OrderDto();
        String line;
        List<String> destinationsNotFound = new ArrayList<>();
        int i = 0;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            Optional<DestinationEntity> destination = destinationRepository.findByName(values[0]);
            if (destination.isPresent()) {
                orderDto.setName(values[0]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate localDate = LocalDate.parse(values[1], formatter);
                orderDto.setDate(localDate);
                OrderEntity orderEntity = orderConverter.fromDtoToEntity(orderDto);
                orderEntity.setDestination(destination.get());
                orderRepository.save(orderEntity);
            } else {
                if(!destinationsNotFound.contains(values[0]))
                {
                    destinationsNotFound.add(values[0]);
                }
            }
        }

        destinationResponse.setResponse(destinationsNotFound);
    }

    @Transactional
    public Long addOrder(OrderDto orderDto) {
        OrderEntity order = orderConverter.fromDtoToEntity(orderDto);
        OrderEntity savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

        //    private CompanyContributor companyContributor;
        //    public OrderService(CompanyContributor companyContributor) {
        //        this.companyContributor = companyContributor;
        //        companyContributor.incrementCurrentDate();
        //    }

}
