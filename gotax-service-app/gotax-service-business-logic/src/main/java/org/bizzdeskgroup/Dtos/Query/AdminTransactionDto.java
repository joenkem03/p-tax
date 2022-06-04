package org.bizzdeskgroup.Dtos.Query;

public class AdminTransactionDto {
    private String transactionId;
    private String transactionReference;
    private String transactionDate;
    private String transactionDescription;
    private double totalAmountPaid;
    private double serviceAmount;
    private double fee;
    private double commission;
    private String transactionType;
    private String channel;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String terminalId;
    private String pan;
    private int mdaId;
    private String  mdaName;
    private String project;
    private String individualPayerTempTin;
    private String individualPayerJtbTin;
    private String corporatePayerTempTin;
    private String corporatePayerJtbTin;
    private int payerId;


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

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
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

    public double getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(double serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
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
}
