package com.siit.finalproject.service;


import com.siit.finalproject.convertDto.DestinationConverter;
import com.siit.finalproject.dto.DestinationDto;
import com.siit.finalproject.entity.DestinationEntity;
import com.siit.finalproject.enums.OrderEnum;
import com.siit.finalproject.exception.DataNotFound;
import com.siit.finalproject.repository.DestinationRepository;
import com.siit.finalproject.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {
    private final OrderService orderService;
    private final DestinationRepository destinationRepository;
    private final DestinationConverter destinationConverter;
    private final OrderRepository orderRepository;

    public DestinationService(OrderService orderService, DestinationRepository destinationRepository, DestinationConverter destinationConverter, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.destinationRepository = destinationRepository;
        this.destinationConverter = destinationConverter;
        this.orderRepository = orderRepository;
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

    public List<Long> addDestination(List<DestinationDto> destinationDtoList) {
        List<Long> addedIds = new ArrayList<>();
        for (DestinationDto destinationDto : destinationDtoList) {
            DestinationEntity destinationEntity = destinationConverter.fromDtoToEntity(destinationDto);
            destinationRepository.save(destinationEntity);
            addedIds.add(destinationEntity.getId());
        }
        return addedIds;
    }

    public List<Long> updateDestination(List<DestinationDto> destinationDtoList) throws DataNotFound {
        List<Long> updateIds = new ArrayList<>();
        for (DestinationDto destinationDto : destinationDtoList) {
            Optional<DestinationEntity> destinationEntityOptional = destinationRepository.findById(destinationDto.getId());

            if (destinationEntityOptional.isEmpty()) {
                throw new DataNotFound(String.format("The destination with id %s could not be found in database.", destinationDto.getId()));
            }

            DestinationEntity destinationEntity = destinationConverter.fromDtoToEntity(destinationDto);
            destinationRepository.save(destinationEntity);
            updateIds.add(destinationEntity.getId());
        }
        return updateIds;
    }

    public List<DestinationDto> getAllDestinations()
    {
        List<DestinationEntity> destinationEntities = destinationRepository.findAll();
        return destinationEntities.stream()
                .map(destinationConverter::fromEntityToDto).toList();
    }

    public DestinationDto getDestinationsById(Long id) throws DataNotFound
    {
        Optional<DestinationEntity> destinationEntityOptional = destinationRepository.findById(id);
        if (destinationEntityOptional.isEmpty()) {
            throw new DataNotFound(String.format("The destination with id %s could not be found in database.", id));
        }

        return destinationEntityOptional.map(destinationConverter::fromEntityToDto).orElse(null);
    }

    public void deleteDestinationById(Long id) throws DataNotFound
    {
        Optional<DestinationEntity> destinationEntityOptional = destinationRepository.findById(id);
        if (destinationEntityOptional.isEmpty()) {
            throw new DataNotFound(String.format("The destination with id %s could not be found in database.", id));
        }
        orderRepository.findAll().stream()
                .filter(orderEntity -> orderEntity.getDestination().getId().equals(id))
                .filter(orderEntity -> orderEntity.getStatus() != OrderEnum.DELIVERED)
                .forEach(orderEntity -> orderService.deleteOrderById(orderEntity.getId()));
        orderService.markDestinationAsNullByDestinationId(id);
        destinationRepository.deleteById(id);
    }
}
