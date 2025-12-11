package com.teleauro.datamanagement.repository;

import com.teleauro.datamanagement.model.PricePlan;
import com.teleauro.datamanagement.model.PricePlanPK;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricePlanRepository extends JpaRepository<PricePlan, PricePlanPK> {
    public List<PricePlan> findByBusiness(boolean business);
}
