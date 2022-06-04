package org.bizzdeskgroup.models;

public class ChangeReq extends AuditedBaseModel{
    private int affectedUser;
    private String changeItem;
    private String oldValue;
    private String newValue;
    private boolean isPending;
    private boolean isApproved;
    private int businessId;


    public int getAffectedUser() {
        return affectedUser;
    }

    public void setAffectedUser(int affectedUser) {
        this.affectedUser = affectedUser;
    }

    public String getChangeItem() {
        return changeItem;
    }

    public void setChangeItem(String changeItem) {
        this.changeItem = changeItem;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }
}
