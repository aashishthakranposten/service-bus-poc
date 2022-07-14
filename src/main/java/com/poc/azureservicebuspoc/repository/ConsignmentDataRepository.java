package com.poc.azureservicebuspoc.repository;

import com.poc.azureservicebuspoc.model.ConsignmentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsignmentDataRepository extends JpaRepository<ConsignmentData, UUID> {
}
