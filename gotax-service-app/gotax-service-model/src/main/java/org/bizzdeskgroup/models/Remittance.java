package org.bizzdeskgroup.models;

public class Remittance extends AuditedBaseModel{
    public String remittanceNo;
    public double totalAmount;
    public int userId;
    public int terminalId;
    public boolean status;
    public int mdaId;
    public int businessId;
    public int mdaOfficeId;

    public String getRemittanceNo() {
        return remittanceNo;
    }

    public void setRemittanceNo(String remittanceNo) {
        this.remittanceNo = remittanceNo;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getMdaId() {
        return mdaId;
    }

    public void setMdaId(int mdaId) {
        this.mdaId = mdaId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getMdaOfficeId() {
        return mdaOfficeId;
    }

    public void setMdaOfficeId(int mdaOfficeId) {
        this.mdaOfficeId = mdaOfficeId;
    }
}
