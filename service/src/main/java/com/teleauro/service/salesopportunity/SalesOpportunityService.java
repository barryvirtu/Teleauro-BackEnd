package com.teleauro.service.salesopportunity;

import com.teleauro.model.salesopportunity.SalesOpportunity;
import com.teleauro.repository.salesopportunity.SalesOpportunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesOpportunityService {

    private final SalesOpportunityRepository repository;

    public SalesOpportunity create(SalesOpportunity opportunity) {
        return repository.save(opportunity);
    }

    public List<SalesOpportunity> readAll() {
        return repository.findAll();
    }

    public Optional<SalesOpportunity> readById(Long uprn) {
        return repository.findById(uprn);
    }

    public SalesOpportunity update(Long uprn, SalesOpportunity updated) {
        if (repository.existsById(uprn)) {
            updated.setUprn(uprn);
            return repository.save(updated);
        }
        return null;
    }

    public boolean delete(Long uprn) {
        if (repository.existsById(uprn)) {
            repository.deleteById(uprn);
            return true;
        }
        return false;
    }
}
