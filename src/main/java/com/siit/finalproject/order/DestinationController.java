package com.siit.finalproject.order;

import com.siit.finalproject.service.DestinationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;


@RestController
@RequestMapping("/destination")
public class DestinationController
{
    private final DestinationService service;

    public DestinationController(DestinationService service) {
        this.service = service;
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadDestinationCsv(@RequestParam(name = "filePath") String filePath)
    {
        try
        {   BufferedReader br = new BufferedReader(new FileReader(filePath));
            service.saveDestination(br);

        } catch (Exception e) {
            return new ResponseEntity<>("Please select CSV file to upload. ", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("CVS file is uploading", HttpStatus.OK);
    }
}
