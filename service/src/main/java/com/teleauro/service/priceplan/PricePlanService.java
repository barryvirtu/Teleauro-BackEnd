package com.teleauro.service.priceplan;

import java.util.List;
import java.util.Optional;

import com.teleauro.model.priceplan.PricePlan;
import com.teleauro.model.priceplan.PricePlanPK;
import com.teleauro.model.tier.Tier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.teleauro.repository.priceplan.PricePlanRepository;

@Service
@RequiredArgsConstructor
public class PricePlanService {

    private final PricePlanRepository repository;

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
