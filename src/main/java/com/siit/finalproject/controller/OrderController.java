package com.siit.finalproject.controller;

import com.siit.finalproject.actuator.CompanyContributor;
import com.siit.finalproject.dto.DestinationResponse;
import com.siit.finalproject.dto.OrderDto;
import com.siit.finalproject.entity.OrderEntity;
import com.siit.finalproject.enums.OrderEnum;
import com.siit.finalproject.exception.DataNotFound;
import com.siit.finalproject.service.OrderService;
import com.siit.finalproject.service.ShippingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final DestinationResponse destinationResponse;
    private final CompanyContributor companyContributor;
    private final ShippingService shippingService;


    public OrderController(OrderService service, DestinationResponse destinationResponse, CompanyContributor companyContributor, ShippingService shippingService) {
        this.orderService = service;
        this.destinationResponse = destinationResponse;
        this.companyContributor = companyContributor;
        this.shippingService = shippingService;
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadDestinationCsv(@RequestParam(name = "filePath") String filePath)
    {
        try {
            BufferedReader file = new BufferedReader(new FileReader(filePath));
            orderService.saveOrders(file);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid file path", HttpStatus.BAD_REQUEST);
        }
        if (destinationResponse.getResponse().size() >= 1) {
            return new ResponseEntity<>("The following destinations are not in DB. "
                    + destinationResponse.getResponse().toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Loading orders in DB. ", HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@Valid @RequestBody List<OrderDto> ordersDto) {
        ArrayList<OrderDto> failedOrders = new ArrayList<OrderDto>();
        ArrayList<OrderDto> successfulOrders = new ArrayList<OrderDto>();
        OrderEntity orderAdd;
        for (OrderDto order : ordersDto) {
            try {
                orderAdd = orderService.addOrder(order);
                if (orderAdd == null) {
                    failedOrders.add(order);
                    continue;
                }
                order.setId(orderAdd.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            successfulOrders.add(order);

        }
        return new ResponseEntity<>("SuccessfulOrders:" + successfulOrders + "\n FailedOrders:" + failedOrders, HttpStatus.OK);
    }


    @GetMapping("/status")
    public ResponseEntity<String> getStatus(@Valid @RequestParam(required = false) String date, @RequestParam(required = false) String destination) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate thisDate = null;
        if (date == null || date.equals("")) {

            thisDate = companyContributor.getCurrentDate();
        }

        if (destination == null || destination.equals("")) {

            thisDate = LocalDate.parse(date, formatter);
            destination = "all";
        }

        List<String> orders = orderService.getOrdersByDestinationAndDate(destination, thisDate);
        if (orders.size() == 0) {
            return new ResponseEntity<>("No orders for today: " + destination + " " + thisDate, HttpStatus.OK);
        }
        return new ResponseEntity<>(orders.toString(), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOrders(@Valid @RequestParam List<Long> ids) {

        ArrayList<Long> failedIds = new ArrayList<>();
        ArrayList<Long> successfulIds = new ArrayList<>();
        List<OrderEntity> orders = orderService.getOrdersByIds(ids);

        for (OrderEntity order : orders) {
            if (order.getStatus() == OrderEnum.NEW || order.getStatus() == OrderEnum.DELIVERING) {

                try {
                    shippingService.updateOrderStatus(order.getId(), OrderEnum.CANCELED);
                } catch (DataNotFound dataNotFound) {
                    dataNotFound.printStackTrace();
                }
                successfulIds.add(order.getId());
            } else {

                failedIds.add(order.getId());
            }

        }
        return new ResponseEntity<>("SuccessfulIds:" + successfulIds + "\n FailedIds:" + failedIds, HttpStatus.OK);

    }
}
