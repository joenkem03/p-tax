package org.bizzdeskgroup.models;

import java.sql.Timestamp;

public class PosTransaction extends AuditedBaseModel{
    public int posId;
    public int operatorId;
    public int customerId;
    public String customerName;
    public String customerPhone;
    public String customerEmail;
    public int assessmentId;
    public int mdaServiceId;
    public String transactionId;
    public String offlineTransactionId;
    public Timestamp transactionDate;
    public double amountPaid;
    public int remittanceId;
    public String transactionStatus;
    public int mdaId;
    public int mdaOfficeId;
    public String invoiceNo;
    public int businessId;

    public int getPosId() {
        return posId;
    }

    public void setPosId(int posId) {
        this.posId = posId;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public int getMdaServiceId() {
        return mdaServiceId;
    }

    public void setMdaServiceId(int mdaServiceId) {
        this.mdaServiceId = mdaServiceId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOfflineTransactionId() {
        return offlineTransactionId;
    }

    public void setOfflineTransactionId(String offlineTransactionId) {
        this.offlineTransactionId = offlineTransactionId;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getRemittanceId() {
        return remittanceId;
    }

    public void setRemittanceId(int remittanceId) {
        this.remittanceId = remittanceId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public int getMdaId() {
        return mdaId;
    }

    public void setMdaId(int mdaId) {
        this.mdaId = mdaId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }
}
