package com.siit.finalproject.controller;

import com.siit.finalproject.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/shipping")
public class ShippingController {
    private final OrderService orderService;

    public ShippingController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/new-day")
    public ResponseEntity<String> shipping()
    {

        try {
            orderService.shipping();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Orders are being delivered ", HttpStatus.OK);
    }
}
