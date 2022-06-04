package org.bizzdeskgroup.models;

import java.sql.Timestamp;

public class AuditedBaseModel extends BaseModel{
    public int updatedBy;
    public Timestamp updatedDate;

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
