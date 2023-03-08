package com.siit.finalproject.convertDto;

import com.siit.finalproject.dto.OrderDto;
import com.siit.finalproject.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {

    public OrderEntity fromDtoToEntity(OrderDto orderDto)
    {
        OrderEntity orderEntity = new OrderEntity();

        if(orderDto.getId() != 0)
        {
            orderEntity.setId(orderDto.getId());
        }

        orderEntity.setName((orderDto.getName()));
        orderEntity.setDeliveryDate((orderDto.getDate().toString()));

        return orderEntity;
    }
}
