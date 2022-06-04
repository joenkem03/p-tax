package org.bizzdeskgroup.models;

import java.sql.Timestamp;

public class Mda extends BaseModel {
    public int businessId;
    public String name;
    public String abbreviation;
    public String mdaCode;
    //    public int CreatedBy;
    public boolean isRetaining;
    public boolean isRetainingByPercentage;
    public double retainingValue;
//    public boolean IsActive;
    public int updatedBy;
    public Timestamp updatedDate;


    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getMdaCode() {
        return mdaCode;
    }

    public void setMdaCode(String mdaCode) {
        this.mdaCode = mdaCode;
    }

    public boolean isRetaining() {
        return isRetaining;
    }

    public void setRetaining(boolean retaining) {
        isRetaining = retaining;
    }

    public boolean isRetainingByPercentage() {
        return isRetainingByPercentage;
    }

    public void setRetainingByPercentage(boolean retainingByPercentage) {
        isRetainingByPercentage = retainingByPercentage;
    }

    public double getRetainingValue() {
        return retainingValue;
    }

    public void setRetainingValue(double retainingValue) {
        this.retainingValue = retainingValue;
    }

//    public boolean isActive() {
//        return IsActive;
//    }
//
//    public void setActive(boolean active) {
//        IsActive = active;
//    }


    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
