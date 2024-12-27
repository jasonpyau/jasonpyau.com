package com.jasonpyau.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.Metadata;

@Repository
public interface MetadataRepository extends CrudRepository<Metadata, Integer> {
    
}
