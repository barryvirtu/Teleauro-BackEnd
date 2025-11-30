package com.teleauro.datamanagement.model;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PricePlanPK {
    private Tier tier;
    private boolean business;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PricePlanPK pricePlanPK))
            return false;
        return tier == pricePlanPK.tier && business == pricePlanPK.business;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tier, business);
    }
}
