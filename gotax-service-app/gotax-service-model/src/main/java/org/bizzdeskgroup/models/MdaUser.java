package org.bizzdeskgroup.models;

public class MdaUser extends AuditedBaseModel{
    public int userId;
    public int mdaId;
    public int mdaOfficeId;
    public boolean canCollect;
    public double collectionLimit;
//    public int UpdatedBy;
//    public Timestamp UpdatedDate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMdaId() {
        return mdaId;
    }

    public void setMdaId(int mdaId) {
        this.mdaId = mdaId;
    }

    public boolean CanView;

    public boolean isCanCollect() {
        return canCollect;
    }

    public void setCanCollect(boolean canCollect) {
        this.canCollect = canCollect;
    }

    public double getCollectionLimit() {
        return collectionLimit;
    }

    public void setCollectionLimit(double collectionLimit) {
        this.collectionLimit = collectionLimit;
    }

    public boolean isCanView() {
        return CanView;
    }

    public void setCanView(boolean canView) {
        CanView = canView;
    }

    public int getMdaOfficeId() {
        return mdaOfficeId;
    }

    public void setMdaOfficeId(int mdaOfficeId) {
        this.mdaOfficeId = mdaOfficeId;
    }

    //    public int getUpdatedBy() {
//        return UpdatedBy;
//    }

//    public void setUpdatedBy(int updatedBy) {
//        UpdatedBy = updatedBy;
//    }

//    public Timestamp getUpdatedDate() {
//        return UpdatedDate;
//    }

//    public void setUpdatedDate(Timestamp updatedDate) {
//        UpdatedDate = updatedDate;
//    }
}
