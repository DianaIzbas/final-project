package com.siit.finalproject.convertDto;

import com.siit.finalproject.dto.DestinationDto;
import com.siit.finalproject.entity.DestinationEntity;
import org.springframework.stereotype.Component;

@Component
public class DestinationConverter {
    public DestinationEntity fromDtoToEntity(DestinationDto destinationDto)
    {
        DestinationEntity destinationEntity = new DestinationEntity();

        if (destinationDto.getId() != null && destinationDto.getId() != 0) {
            destinationEntity.setId(destinationDto.getId());
        }

        destinationEntity.setName((destinationDto.getName()));
        destinationEntity.setDistance((destinationDto.getDistance()));

        return destinationEntity;
    }

    public DestinationDto fromEntityToDto(DestinationEntity destinationEntity)
    {
        DestinationDto destinationDto = new DestinationDto();

        if (destinationEntity.getId() != null && destinationEntity.getId() != 0) {
            destinationDto.setId(destinationEntity.getId());
        }

        destinationDto.setName((destinationEntity.getName()));
        destinationDto.setDistance((destinationEntity.getDistance()));

        return destinationDto;
    }
}
