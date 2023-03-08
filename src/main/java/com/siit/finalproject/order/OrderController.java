package com.siit.finalproject.order;

import com.siit.finalproject.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;


@RestController
@RequestMapping("/order")
public class OrderController
{
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadDestinationCsv(@RequestParam(name = "filePath") String filePath)
    {
        try
        {   BufferedReader br = new BufferedReader(new FileReader(filePath));
            service.saveOrders(br);

        } catch (Exception e) {
            return new ResponseEntity<>("Please select CSV file to upload. ", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("CVS file is uploading", HttpStatus.OK);
    }

//    @PutMapping("/current-date")
//    public ResponseEntity<Resource> updateResource(@PathVariable Long id, @RequestBody Resource updatedResource, @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date currentDate) {
//        // logic to update the resource using the updatedResource object, the id parameter, and the currentDate parameter
//        Resource savedResource = .updateResource(id, updatedResource, currentDate);
//
//        return ResponseEntity.ok(savedResource);
//    }
}
