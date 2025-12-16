package com.teleauro.repository.priceplan;


import java.util.List;

import com.teleauro.model.priceplan.PricePlan;
import com.teleauro.model.priceplan.PricePlanPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricePlanRepository extends JpaRepository<PricePlan, PricePlanPK> {
    public List<PricePlan> findByBusiness(boolean business);
}
