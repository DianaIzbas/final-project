package com.siit.finalproject.service;

import com.siit.finalproject.convertDto.OrderConverter;
import com.siit.finalproject.dto.OrderDto;
import com.siit.finalproject.entity.OrderEntity;
import com.siit.finalproject.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;

    public OrderService(OrderRepository orderRepository, OrderConverter orderConverter) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
    }


    public void saveOrders(BufferedReader br) throws IOException {

        OrderDto orderDto = new OrderDto();
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            orderDto.setName(values[0]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
            LocalDate localDate = LocalDate.parse(values[1], formatter);
            orderDto.setDate(localDate);
            OrderEntity orderEntity = orderConverter.fromDtoToEntity(orderDto);
            orderRepository.save(orderEntity);
        }

        //    private CompanyContributor companyContributor;
        //    public OrderService(CompanyContributor companyContributor) {
        //        this.companyContributor = companyContributor;
        //        companyContributor.incrementCurrentDate();
        //    }
    }
}
