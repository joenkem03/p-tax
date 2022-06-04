package org.bizzdeskgroup.models;

public class Business extends AuditedBaseModel{
    public String clientName;
    public String clientLogo;
    public int stateLgaId;
    public String contactPerson;
    public String contactPhone;
    public String contactEmail;
    public boolean isRebateEnabled;
    public double commission;
    public boolean  isCommissionPercent;
    public double fee;


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientLogo() {
        return clientLogo;
    }

    public void setClientLogo(String clientLogo) {
        this.clientLogo = clientLogo;
    }

    public int getStateLgaId() {
        return stateLgaId;
    }

    public void setStateLgaId(int stateLgaId) {
        this.stateLgaId = stateLgaId;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public boolean isRebateEnabled() {
        return isRebateEnabled;
    }

    public void setRebateEnabled(boolean rebateEnabled) {
        isRebateEnabled = rebateEnabled;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public boolean isCommissionPercent() {
        return isCommissionPercent;
    }

    public void setCommissionPercent(boolean commissionPercent) {
        isCommissionPercent = commissionPercent;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
