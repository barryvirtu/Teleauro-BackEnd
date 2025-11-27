
package com.teleauro.datamanagement.repository;

import com.teleauro.datamanagement.model.SalesOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOpportunityRepository extends JpaRepository<SalesOpportunity, Long> {
}
