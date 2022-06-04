package org.bizzdeskgroup.models;

import java.sql.Timestamp;

public class MdaService extends BaseModel{
    public int mdaId;
    public String name;
    public String code;
    public boolean isAssessable;
    public boolean isFixedAmount;
    public double amount;
    public boolean isRetaining;
    public boolean isRetainingByPercentage;
    public double retainingValue;
    public int businessId;
    public int updatedBy;
    public Timestamp updatedDate;

    public int getMdaId() {
        return mdaId;
    }

    public void setMdaId(int mdaId) {
        this.mdaId = mdaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isAssessable() {
        return isAssessable;
    }

    public void setAssessable(boolean assessable) {
        isAssessable = assessable;
    }

    public boolean isFixedAmount() {
        return isFixedAmount;
    }

    public void setFixedAmount(boolean fixedAmount) {
        isFixedAmount = fixedAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

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
