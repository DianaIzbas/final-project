package com.siit.finalproject.service;


import com.siit.finalproject.convertDto.DestinationConverter;
import com.siit.finalproject.dto.DestinationDto;
import com.siit.finalproject.entity.DestinationEntity;
import com.siit.finalproject.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;

@Service
public class DestinationService
{
    private final DestinationRepository destinationRepository;
    private final DestinationConverter destinationConverter;

    public DestinationService(DestinationRepository destinationRepository, DestinationConverter destinationConverter) {
        this.destinationRepository = destinationRepository;
        this.destinationConverter = destinationConverter;
    }


    public void saveDestination(BufferedReader br) throws IOException
    {

        DestinationDto destinationDto = new DestinationDto();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                destinationDto.setName(values[0]);
                destinationDto.setDistance(Integer.parseInt(values[1]));
                DestinationEntity destinationEntity = destinationConverter.fromDtoToEntity(destinationDto);
                destinationRepository.save(destinationEntity);
            }
    }
}
