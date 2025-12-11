package com.teleauro.datamanagement.model;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@IdClass(PricePlanPK.class)
@Table(name = "price_plans")
@Data
public class PricePlan {
    @Id
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Tier tier;

    @Id
    @Column(name = "business")
    private boolean business;

    @Column(name = "name")
    private String name;

    @Column(name = "speed")
    private Integer speed;

    @Column(name = "data")
    private Integer data;

    @Column(name = "price")
    private String price;

    @Column(name = "features")
    private String features;
}
