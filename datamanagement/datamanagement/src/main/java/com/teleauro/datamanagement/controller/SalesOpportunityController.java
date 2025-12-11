
package com.teleauro.datamanagement.controller;

import com.teleauro.datamanagement.model.SalesOpportunity;
import com.teleauro.datamanagement.service.SalesOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data/opportunities")
@CrossOrigin(origins = "*") // Allow Angular frontend
public class SalesOpportunityController {

    @Autowired
    private SalesOpportunityService service;

    // CREATE
    @PostMapping
    public ResponseEntity<SalesOpportunity> create(@RequestBody SalesOpportunity opportunity) {
        SalesOpportunity created = service.create(opportunity);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // READ ALL UPRNs
    @GetMapping("/uprns")
    public ResponseEntity<List<Long>> getAllUprns() {
        List<Long> uprns = service.readAll()
                                  .stream()
                                  .map(SalesOpportunity::getUprn)
                                  .toList();
        return ResponseEntity.ok(uprns);
    }

    // READ ALL Opportunities with coordinates
    @GetMapping("/all")
    public ResponseEntity<List<SalesOpportunity>> getAllOpportunities() {
        List<SalesOpportunity> opportunities = service.readAll();
        return ResponseEntity.ok(opportunities);
    }

    // READ BY ID
    @GetMapping("/{uprn}")
    public ResponseEntity<SalesOpportunity> readById(@PathVariable Long uprn) {
        return service.readById(uprn)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{uprn}")
    public ResponseEntity<SalesOpportunity> update(@PathVariable Long uprn, @RequestBody SalesOpportunity updated) {
        SalesOpportunity result = service.update(uprn, updated);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{uprn}")
    public ResponseEntity<Void> delete(@PathVariable Long uprn) {
        boolean deleted = service.delete(uprn);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
