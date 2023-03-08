package com.siit.finalproject.repository;

import com.siit.finalproject.entity.DestinationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DestinationRepository extends CrudRepository<DestinationEntity, Long>
{

}
