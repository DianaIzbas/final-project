package com.siit.finalproject.repository;

import com.siit.finalproject.entity.DestinationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DestinationRepository extends CrudRepository<DestinationEntity, Long> {
    Optional<DestinationEntity> findByName(String name);

    List<DestinationEntity> findAll();
}
