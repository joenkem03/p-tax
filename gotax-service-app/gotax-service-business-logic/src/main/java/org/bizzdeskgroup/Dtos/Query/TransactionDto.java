package org.bizzdeskgroup.Dtos.Query;

import java.sql.Timestamp;

public class TransactionDto {
    private String transactionId;
    private String transactionReference;
    private Timestamp transactionDate;
    private String transactionDescription;
    private double totalAmountPaid;
//    public String transactionType;
    private String channel;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String terminalId;
    private String pan;
    private int mdaId;
    private String mdaName;
    private int mdaOfficeId;
    private String mdaOfficeName;
    private String project;
    private String individualPayerTempTin;
    private String individualPayerJtbTin;
    private String corporatePayerTempTin;
    private String corporatePayerJtbTin;
    private int agentId;
    private String agentName;
    private int payerId;
    private String cashTransactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public double getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(double totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

        public int getMdaId() {
        return mdaId;
    }

    public void setMdaId(int mdaId) {
        this.mdaId = mdaId;
    }

    public String getMdaName() {
        return mdaName;
    }

    public void setMdaName(String mdaName) {
        this.mdaName = mdaName;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getIndividualPayerTempTin() {
        return individualPayerTempTin;
    }

    public void setIndividualPayerTempTin(String individualPayerTempTin) {
        this.individualPayerTempTin = individualPayerTempTin;
    }

    public String getIndividualPayerJtbTin() {
        return individualPayerJtbTin;
    }

    public void setIndividualPayerJtbTin(String individualPayerJtbTin) {
        this.individualPayerJtbTin = individualPayerJtbTin;
    }

    public String getCorporatePayerTempTin() {
        return corporatePayerTempTin;
    }

    public void setCorporatePayerTempTin(String corporatePayerTempTin) {
        this.corporatePayerTempTin = corporatePayerTempTin;
    }

    public String getCorporatePayerJtbTin() {
        return corporatePayerJtbTin;
    }

    public void setCorporatePayerJtbTin(String corporatePayerJtbTin) {
        this.corporatePayerJtbTin = corporatePayerJtbTin;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public String getCashTransactionId() {
        return cashTransactionId;
    }

    public void setCashTransactionId(String cashTransactionId) {
        this.cashTransactionId = cashTransactionId;
    }

    public int getMdaOfficeId() {
        return mdaOfficeId;
    }

    public void setMdaOfficeId(int mdaOfficeId) {
        this.mdaOfficeId = mdaOfficeId;
    }

    public String getMdaOfficeName() {
        return mdaOfficeName;
    }

    public void setMdaOfficeName(String mdaOfficeName) {
        this.mdaOfficeName = mdaOfficeName;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
