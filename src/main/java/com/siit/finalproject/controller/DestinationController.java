package com.siit.finalproject.controller;

import com.siit.finalproject.dto.DestinationDto;
import com.siit.finalproject.exception.DataNotFound;
import com.siit.finalproject.service.DestinationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/destinations")
public class DestinationController {
    private final DestinationService service;

    public DestinationController(DestinationService service) {
        this.service = service;
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadDestinationCsv(@RequestParam(name = "filePath") String filePath)
    {
        try {
            BufferedReader file = new BufferedReader(new FileReader(filePath));
            service.saveDestination(file);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid file path", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Loading destinations in DB. ", HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDestination(@Valid @RequestBody List<DestinationDto> destinationDtoList)
    {

        List<Long> addedIds = new ArrayList<>(service.addDestination(destinationDtoList));
        return new ResponseEntity<>("Destinations has been updated: " + addedIds, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateDestination(@RequestBody List<DestinationDto> destinationDtoList) throws DataNotFound
    {
        List<Long> updateIds;
        try {

            updateIds = service.updateDestination(destinationDtoList);
        } catch (DataNotFound e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Destinations has been updated: " + updateIds, HttpStatus.OK);

    }

    @GetMapping()
    public ResponseEntity<String> getAllDestinations()
    {
        List<DestinationDto> destinations = new ArrayList<>(service.getAllDestinations());
        return new ResponseEntity<>("Destinations:" + destinations.toString(), HttpStatus.OK);
    }

    @GetMapping("/byId/{destinationId}")
    public ResponseEntity<String> getDestinationById(@PathVariable(name = "destinationId") Long id)
    {
        DestinationDto destinations;
        try {

            destinations = service.getDestinationsById(id);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid id", HttpStatus.BAD_REQUEST);
        }
        if (destinations == null) {
            return new ResponseEntity<>("Destination with id: " + id + " does not exist in DB", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Destination is: " + destinations, HttpStatus.OK);
    }

    @DeleteMapping("/byId/{destinationId}")
    public ResponseEntity<String> deleteDestinationById(@PathVariable(name = "destinationId") Long id)
    {
        try {

            service.deleteDestinationById(id);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid id", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Destination with id: " + id + " has been deleted", HttpStatus.OK);
    }
}
