package org.bizzdeskgroup.models;

import java.sql.Timestamp;

public class MdaOffice extends BaseModel{
    public int mdaId;
    public String name;
    public String officeCode;
    public boolean isHq;
    private int updatedBy;
    private Timestamp updatedDate;
    private int businessId;
//    public boolean IsActive;

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

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public boolean isHq() {
        return isHq;
    }

    public void setHq(boolean hq) {
        isHq = hq;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    //    public boolean isActive() {
//        return IsActive;
//    }
//
//    public void setActive(boolean active) {
//        IsActive = active;
//    }
}
