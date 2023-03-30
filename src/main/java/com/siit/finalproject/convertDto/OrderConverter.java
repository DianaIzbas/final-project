package com.siit.finalproject.convertDto;

import com.siit.finalproject.dto.OrderDto;
import com.siit.finalproject.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {

    public OrderEntity fromDtoToEntity(OrderDto orderDto)
    {
        OrderEntity orderEntity = new OrderEntity();

        if (orderDto.getId() != null && orderDto.getId() != 0) {
            orderEntity.setId(orderDto.getId());
        }

        orderEntity.setName((orderDto.getName()));
        orderEntity.setDeliveryDate(orderDto.getDate());

        return orderEntity;
    }

    public OrderDto fromEntityToDto(OrderEntity orderEntity)
    {
        OrderDto orderDto = new OrderDto();

        if (orderEntity.getId() != null && orderEntity.getId() != 0) {
            orderDto.setId(orderEntity.getId());
        }

        orderDto.setName((orderEntity.getName()));
        orderDto.setDate(orderEntity.getDeliveryDate());

        return orderDto;
    }
}
