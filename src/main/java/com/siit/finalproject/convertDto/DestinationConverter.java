package com.siit.finalproject.convertDto;

import com.siit.finalproject.dto.DestinationDto;
import com.siit.finalproject.entity.DestinationEntity;
import org.springframework.stereotype.Component;


@Component
public class DestinationConverter
{
    public DestinationEntity fromDtoToEntity(DestinationDto destinationDto)
    {
        DestinationEntity destinationEntity = new DestinationEntity();

        if(destinationDto.getId() != 0)
        {
            destinationEntity.setId(destinationDto.getId());
        }

        destinationEntity.setName((destinationDto.getName()));
        destinationEntity.setDistance((destinationDto.getDistance()));

        return destinationEntity;
    }
}
