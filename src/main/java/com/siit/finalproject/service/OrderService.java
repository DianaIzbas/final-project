package com.siit.finalproject.service;

import com.siit.finalproject.actuator.CompanyContributor;
import com.siit.finalproject.convertDto.OrderConverter;
import com.siit.finalproject.dto.DestinationResponse;
import com.siit.finalproject.dto.OrderDto;
import com.siit.finalproject.entity.DestinationEntity;
import com.siit.finalproject.entity.OrderEntity;
import com.siit.finalproject.enums.OrderEnum;
import com.siit.finalproject.exception.DataNotFound;
import com.siit.finalproject.repository.DestinationRepository;
import com.siit.finalproject.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class OrderService {

    private final CompanyContributor companyContributor;
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final DestinationRepository destinationRepository;
    private final DestinationResponse destinationResponse;
    private final ShippingService shippingService;


    public OrderService(CompanyContributor companyContributor, OrderRepository orderRepository, OrderConverter orderConverter, DestinationRepository destinationRepository, DestinationResponse destinationResponse, ShippingService shippingService) {
        this.companyContributor = companyContributor;
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.destinationRepository = destinationRepository;
        this.destinationResponse = destinationResponse;
        this.shippingService = shippingService;
    }

    public void saveOrders(BufferedReader br) throws IOException
    {

        OrderDto orderDto = new OrderDto();
        String line;
        List<String> destinationsNotFound = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            Optional<DestinationEntity> destination = destinationRepository.findByName(values[0]);
            if (destination.isPresent()) {
                orderDto.setName(values[0]);
                LocalDate localDate = LocalDate.parse(values[1], formatter);
                orderDto.setDate(localDate);
                OrderEntity orderEntity = orderConverter.fromDtoToEntity(orderDto);
                orderEntity.setDestination(destination.get());
                orderEntity.setStatus(OrderEnum.NEW);
                orderRepository.save(orderEntity);
            } else {
                if (!destinationsNotFound.contains(values[0])) {
                    destinationsNotFound.add(values[0]);
                }
            }
        }

        destinationResponse.setResponse(destinationsNotFound);
    }

    @Transactional
    public OrderEntity addOrder(OrderDto orderDto) {
        Optional<DestinationEntity> destination = destinationRepository.findByName(orderDto.getName());
        List<String> destinationsNotFound = new ArrayList<>();
        if (destination.isPresent()) {
            OrderEntity order = orderConverter.fromDtoToEntity(orderDto);
            order.setDestination(destination.get());
            LocalDate localDate = order.getDeliveryDate();
            LocalDate date = LocalDate.of(2021, 12, 14);
            if (localDate.isBefore(date)) {

                return null;
            }
            order.setStatus(OrderEnum.NEW);
            return orderRepository.save(order);

        }
        return null;
    }


    public void shipping()
    {
        LocalDate thisDay;
        thisDay = companyContributor.newDay();
        orderRepository.findAll().stream()
                .filter(orderEntity -> orderEntity.getDeliveryDate().equals(thisDay))
                .forEach(orderEntity -> {
                    try {
                        if (orderRepository.findById(orderEntity.getId()).get().getStatus() != OrderEnum.CANCELED) {
                            shippingService.updateOrderStatus(orderEntity.getId(), OrderEnum.DELIVERING);
                        }

                    } catch (DataNotFound e) {
                        e.printStackTrace();
                    }
                });

        List<OrderEntity> todayOrders = orderRepository.findAll().stream()
                .filter(orderEntity -> orderEntity.getDeliveryDate().equals(thisDay)).toList();

        List<DestinationEntity> destinationEntityList = destinationRepository.findAll().stream().toList();

        List<String> todayOrdersName = new ArrayList<>();
        for (OrderEntity order : todayOrders) {
            if (!todayOrdersName.contains(order.getName())) {
                todayOrdersName.add(order.getName());
            }
        }
        log.info("New day starting: " + thisDay);
        log.info("Today we will be delivering to " + todayOrdersName);
        for (DestinationEntity destination : destinationEntityList) {
            List<Long> orderIds = new ArrayList<>();
            List<OrderEntity> orders = todayOrders.stream()
                    .filter(orderEntity -> orderEntity.getDestination().getId().equals(destination.getId())).toList();
            orders.forEach(orderEntity -> orderIds.add(orderEntity.getId()));

            shippingService.startDeliveries(destination, orderIds);

        }

    }

    public List<String> getOrdersByDestinationAndDate(String destination, LocalDate date) {
        List<String> selectedOrders = new ArrayList<>();
        if (destination.equals("all")) {
            List<OrderEntity> allOrders = orderRepository.findAll().stream()
                    .filter(orderEntity -> orderEntity.getDeliveryDate().equals(date)).toList();
            for (OrderEntity order : allOrders) {
                selectedOrders.add("Order with id:" +
                        order.getId().toString() + " with destination " +
                        order.getName() + " and status " +
                        order.getStatus().toString() +
                        "\n");
            }
        } else {
            List<OrderEntity> allOrders = orderRepository.findAll().stream()
                    .filter(orderEntity -> orderEntity.getDeliveryDate().equals(date))
                    .filter(orderEntity -> orderEntity.getName().equals(destination)).toList();
            for (OrderEntity order : allOrders) {
                selectedOrders.add("Order with id:" +
                        order.getId().toString() + " with destination " +
                        order.getName() + " and status " +
                        order.getStatus().toString() +
                        "\n");
            }
        }
        return selectedOrders;
    }

    public List<OrderEntity> getOrdersByIds(List<Long> ids) {
        List<OrderEntity> orders = new ArrayList<OrderEntity>();

        for (Long id : ids) {

            orders.add(orderRepository.findById(id).get());

        }
        return orders;
    }


    public void markDestinationAsNullByDestinationId(Long destinationId)
    {
        List<OrderEntity> allByDestination_id = orderRepository.findAllByDestination_Id(destinationId);
        for (OrderEntity orderEntity : allByDestination_id) {
            orderEntity.setDestination(null);
        }
        orderRepository.saveAll(allByDestination_id);
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

}

