package com.siit.finalproject.order;

import com.siit.finalproject.dto.DestinationResponse;
import com.siit.finalproject.dto.OrderDto;
import com.siit.finalproject.entity.OrderEntity;
import com.siit.finalproject.repository.OrderRepository;
import com.siit.finalproject.service.OrderService;
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
    private final OrderRepository repository;
    private final OrderService service;
    private final DestinationResponse destinationResponse;

    public OrderController(OrderRepository repository, OrderService service, DestinationResponse destinationResponse) {
        this.repository = repository;
        this.service = service;
        this.destinationResponse = destinationResponse;
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadDestinationCsv(@RequestParam(name = "filePath") String filePath) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(filePath));
            service.saveOrders(file);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Please select a file with orders to upload. ", HttpStatus.BAD_REQUEST);
        }
        if (destinationResponse.getResponse().size() >= 1) {
            return new ResponseEntity<>("The following destinations are not in DB. "
                    + destinationResponse.getResponse().toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Loading orders in DB. ", HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@Valid @RequestBody List<OrderDto> ordersDto) {
        ArrayList<OrderDto> FailedOrders = new ArrayList<OrderDto>();
        ArrayList<OrderDto> SuccessfulOrders = new ArrayList<OrderDto>();
        Long orderAdd = null;
        for (OrderDto order : ordersDto) {

            try {
                orderAdd = service.addOrder(order);
                if (orderAdd == 0L) {
                    FailedOrders.add(order);
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            SuccessfulOrders.add(order);

        }
        return new ResponseEntity<>("SuccessfulOrders:" + SuccessfulOrders.toString() + "\n FailedOrders:" + FailedOrders.toString(), HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<Integer> getStatus(@Valid @RequestParam(required = false) String date, @RequestParam(required = false) String destination){

        if (date == null || date.equals("")){

            date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
        }

        if (destination == null || destination.equals("")){

            destination = "all";
        }

        List<OrderEntity> orders = service.getOrdersByDestinationAndDate(destination, date);


        return new ResponseEntity<>(orders.size(), HttpStatus.OK);





    }



//    @PutMapping("/current-date")
//    public ResponseEntity<Resource> updateResource(@PathVariable Long id, @RequestBody Resource updatedResource, @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date currentDate) {
//        // logic to update the resource using the updatedResource object, the id parameter, and the currentDate parameter
//        Resource savedResource = .updateResource(id, updatedResource, currentDate);
//
//        return ResponseEntity.ok(savedResource);
//    }
}
