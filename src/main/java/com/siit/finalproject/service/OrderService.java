package com.siit.finalproject.service;

import com.siit.finalproject.actuator.CompanyContributor;
import com.siit.finalproject.convertDto.OrderConverter;
import com.siit.finalproject.dto.DestinationResponse;
import com.siit.finalproject.dto.OrderDto;
import com.siit.finalproject.entity.DestinationEntity;
import com.siit.finalproject.entity.OrderEntity;
import com.siit.finalproject.enums.OrderEnum;
import com.siit.finalproject.exception.DataNotFound;
import com.siit.finalproject.enums.OrderEnum;
import com.siit.finalproject.repository.DestinationRepository;
import com.siit.finalproject.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final CompanyContributor companyContributor;
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final DestinationRepository destinationRepository;
    private final DestinationResponse destinationResponse;

    public OrderService(CompanyContributor companyContributor, OrderRepository orderRepository, OrderConverter orderConverter, DestinationRepository destinationRepository, DestinationResponse destinationResponse) {
        this.companyContributor = companyContributor;
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
        Optional<DestinationEntity> destination = destinationRepository.findByName(orderDto.getName());
        List<String> destinationsNotFound = new ArrayList<>();
        if(destination.isPresent())
        {
            OrderEntity order = orderConverter.fromDtoToEntity(orderDto);
            order.setDestination(destination.get());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            //postman trimite invers data?????
            LocalDate localDate = LocalDate.parse(order.getDeliveryDate(), formatter);
            LocalDate date = LocalDate.now();
            if (localDate.isBefore(date)){

               return 0L;
            }
            order.setStatus(OrderEnum.NEW);
            OrderEntity savedOrder = orderRepository.save(order);
            return savedOrder.getId();

        }
        return 0L;
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
    public void shipping()
    {
        String thisDay;
        thisDay = companyContributor.newDay().toString();
        orderRepository.findAll().stream()
                .filter(orderEntity -> orderEntity.getDeliveryDate().equals(thisDay))
                .forEach(orderEntity -> {
                    try {
                        updateOrderStatus(orderEntity.getId(), OrderEnum.DELIVERING);
                    } catch (DataNotFound e) {
                        e.printStackTrace();
                    }
                });

        List<OrderEntity> todayOrders = orderRepository.findAll().stream()
                .filter(orderEntity -> orderEntity.getDeliveryDate().equals(thisDay)).toList();

        List<DestinationEntity> destinationEntityList = destinationRepository.findAll().stream().toList();

        for(DestinationEntity destination : destinationEntityList)
        {
            List<Long> orderIds = new ArrayList<>();
            List<OrderEntity> orders = todayOrders.stream()
                    .filter(orderEntity -> orderEntity.getDestination().getId().equals(destination.getId())).toList();
            orders.forEach(orderEntity -> orderIds.add(orderEntity.getId()));
            startDeliveries( destination, orderIds);
    public List<OrderEntity>getAllOrders (){

        return (List<OrderEntity>) orderRepository.findAll();
    }
    public List<OrderEntity>getOrdersByDestinationAndDate(String Destination,String Date){

       List<OrderEntity>allOrders = this.getAllOrders();
       List<OrderEntity>selectedOrders = new ArrayList<>();
        for (OrderEntity order : allOrders) {

            if(Destination.equals("all") && order.getDeliveryDate().equals(Date)){
                selectedOrders.add(order);
            }
            else if (order.getDestination().getName().equals(Destination) && order.getDeliveryDate().equals(Date)) {
                selectedOrders.add(order);
            }
        }
        return selectedOrders;
    }

        //    private CompanyContributor companyContributor;
        //    public OrderService(CompanyContributor companyContributor) {
        //        this.companyContributor = companyContributor;
        //        companyContributor.incrementCurrentDate();
        //    }

        }



    }
    public void startDeliveries(DestinationEntity destination, List<Long> orderIds)
    {


    }

    public List<String> shippingTest()
    {
        String thisDay;
        thisDay = companyContributor.newDay().toString();
        orderRepository.findAll().stream()
                .filter(orderEntity -> orderEntity.getDeliveryDate().equals(thisDay))
                .forEach(orderEntity -> {
                    try {
                        updateOrderStatus(orderEntity.getId(), OrderEnum.DELIVERING);
                    } catch (DataNotFound e) {
                        e.printStackTrace();
                    }
                });

        List<OrderEntity> todayOrders = orderRepository.findAll().stream()
                .filter(orderEntity -> orderEntity.getDeliveryDate().equals(thisDay)).toList();

        List<DestinationEntity> destinationEntityList = destinationRepository.findAll().stream().toList();

        List<String> test = new ArrayList<>();

        for(DestinationEntity destination : destinationEntityList)
        {
            List<Long> orderIds = new ArrayList<>();
            List<OrderEntity> orders = todayOrders.stream()
                    .filter(orderEntity -> orderEntity.getDestination().getId().equals(destination.getId())).toList();
            orders.forEach(orderEntity -> orderIds.add(orderEntity.getId()));
            startDeliveries( destination, orderIds);
            test.add(destination.getName() + orderIds);
        }
        return test;
    }
}
