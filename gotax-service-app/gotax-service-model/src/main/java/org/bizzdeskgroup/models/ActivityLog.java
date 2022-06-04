package org.bizzdeskgroup.models;

public class ActivityLog extends BaseModel{
    private String action;
    private String actionDesc;
    private int affectedUser;
    private int businessId;
    private long timeStamp;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public int getAffectedUser() {
        return affectedUser;
    }

    public void setAffectedUser(int affectedUser) {
        this.affectedUser = affectedUser;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
