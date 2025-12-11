package com.teleauro.datamanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teleauro.datamanagement.model.PricePlan;
import com.teleauro.datamanagement.model.PricePlanPK;
import com.teleauro.datamanagement.model.Tier;
import com.teleauro.datamanagement.repository.PricePlanRepository;

@Service
public class PricePlanService {

    @Autowired
    private PricePlanRepository repository;

    public PricePlan create(PricePlan pricePlan) {
        return repository.save(pricePlan);
    }

    public List<PricePlan> readAll() {
        return repository.findAll();
    }

    public Optional<PricePlan> readByTierAndBusiness(Tier tier, boolean business) {
        return repository.findById(new PricePlanPK(tier, business));
    }

    public List<PricePlan> readByBusiness(boolean business) {
        return repository.findByBusiness(business);
    }
}
