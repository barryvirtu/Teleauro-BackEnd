package com.teleauro.datamanagement.controller;

import java.util.List;

import com.teleauro.model.priceplan.PricePlan;
import com.teleauro.model.tier.Tier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teleauro.service.priceplan.PricePlanService;

@RestController
@RequestMapping("/api/price-plans")
@CrossOrigin(origins = "*") // Allow Angular frontend
@RequiredArgsConstructor
public class PricePlanController {

    private final PricePlanService service;

    @GetMapping("/all")
    public ResponseEntity<List<PricePlan>> getAllPricePlans() {
        List<PricePlan> pricePlans = service.readAll();
        return ResponseEntity.ok(pricePlans);
    }

    @GetMapping("/business/{business}")
    public ResponseEntity<List<PricePlan>> getByBusiness(@PathVariable boolean business) {
        List<PricePlan> pricePlans = service.readByBusiness(business);
        return ResponseEntity.ok(pricePlans);
    }

    @GetMapping("/business/{business}/tier/{tier}")
    public ResponseEntity<PricePlan> getPricePlan(@PathVariable boolean business, @PathVariable Tier tier) {
        return service.readByTierAndBusiness(tier, business)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
