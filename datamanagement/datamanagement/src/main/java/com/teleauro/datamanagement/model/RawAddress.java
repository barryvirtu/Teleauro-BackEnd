package com.teleauro.datamanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "raw_addresses")
public class RawAddress {
    @Id
    private Long uprn;

    private String singleLineAddress;
    private String postcode;
    private String localAuthorityDistrict;
    private Double longitude;
    private Double latitude;
    private String planned;
    private String occComment;

    // Getters and Setters

    public Long getUprn() {
        return uprn;
    }

    public void setUprn(Long uprn) {
        this.uprn = uprn;
    }

    public String getSingleLineAddress() {
        return singleLineAddress;
    }

    public void setSingleLineAddress(String singleLineAddress) {
        this.singleLineAddress = singleLineAddress;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLocalAuthorityDistrict() {
        return localAuthorityDistrict;
    }

    public void setLocalAuthorityDistrict(String localAuthorityDistrict) {
        this.localAuthorityDistrict = localAuthorityDistrict;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPlanned() {
        return planned;
    }

    public void setPlanned(String planned) {
        this.planned = planned;
    }

    public String getOccComment() {
        return occComment;
    }

    public void setOccComment(String occComment) {
        this.occComment = occComment;
    }
}
