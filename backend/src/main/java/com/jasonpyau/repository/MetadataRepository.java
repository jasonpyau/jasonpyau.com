package com.jasonpyau.repository;

import org.springframework.data.repository.CrudRepository;

import com.jasonpyau.entity.Metadata;

public interface MetadataRepository extends CrudRepository<Metadata, Integer> {
    
}
