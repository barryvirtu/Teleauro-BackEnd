package com.teleauro.model.salesopportunity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sales_opportunity_table")
public class SalesOpportunity {

    @Id
    private Long uprn;

    @Column(name = "single_line_address")
    private String singleLineAddress;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "local_authority_district")
    private String localAuthorityDistrict;

    @Column(name = "constituency")
    private String constituency;

    @Column(name = "lot_number")
    private Integer lotNumber;

    @Column(name = "lot_name")
    private String lotName;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "x_coordinate")
    private Double xCoordinate;

    @Column(name = "y_coordinate")
    private Double yCoordinate;

    @Column(name = "existing_speeds")
    private String existingSpeeds;

    @Column(name = "note")
    private String note;

    @Column(name = "planned")
    private String planned;

    @Column(name = "occ_comment")
    private String occComment;

    @Column(name = "building_type")
    private String buildingType;

    @Column(name = "building_use")
    private String buildingUse;

    @Column(name = "connectivity")
    private String connectivity;

    @Column(name = "is_main")
    private Boolean isMain;

    @Column(name = "area_m2")
    private Double areaM2;


@Column(name = "maxbbpredicteddown")
private Double maxBbPredictedDown;

@Column(name = "maxbbpredictedup")
private Double maxBbPredictedUp;

@Column(name = "maxsfbbpredicteddown")
private Double maxSfbbPredictedDown;

@Column(name = "maxsfbbpredictedup")
private Double maxSfbbPredictedUp;

@Column(name = "maxufbbpredicteddown")
private Double maxUfbbPredictedDown;

@Column(name = "maxufbbpredictedup")
private Double maxUfbbPredictedUp;

@Column(name = "maxpredicteddown")
private Double maxPredictedDown;

@Column(name = "maxpredictedup")
private Double maxPredictedUp;

}
