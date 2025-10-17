package com.teleauro.datamanagement.repository;

import com.teleauro.datamanagement.model.RawAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawAddressRepository extends JpaRepository<RawAddress, Long> {
    // You can add custom query methods here if needed
}
