package com.siit.finalproject.repository;

import com.siit.finalproject.entity.OrderEntity;
import com.siit.finalproject.enums.OrderEnum;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    List<OrderEntity> findAll();

    @Modifying
    @Query(value = "UPDATE orders o SET o.status = ?2 WHERE o.id = ?1")
    void setStatus(Long orderId, OrderEnum status);

    List<OrderEntity> findAllByDestination_Id(Long destinationId);
}
