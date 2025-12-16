
package com.teleauro.repository.salesopportunity;

import com.teleauro.model.salesopportunity.SalesOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOpportunityRepository extends JpaRepository<SalesOpportunity, Long> {
}
