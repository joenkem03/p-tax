package org.bizzdeskgroup.Dtos.Query;

public class InvoiceIntDto {
    private String Payer;
    private String invoiceNo;
    private double invoiceAmount;
//    private int assessmentId;
    private String service;
    private boolean isPaid;
    private String paymentChannel;
    private int businessId;
    private String business;
    private int mdaId;
    private String mda;
    private int month;
    private String year;
    private int userId;
    private String payerFirstName;
    private String payerLastName;
    public String assesedService;
    public String assesedServiceCode;
    private int recordsPerPage;

    public String getPayer() {
        return Payer;
    }

    public void setPayer(String payer) {
        Payer = payer;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

//    public int getAssessmentId() {
//        return assessmentId;
//    }
//
//    public void setAssessmentId(int assessmentId) {
//        this.assessmentId = assessmentId;
//    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public int getMdaId() {
        return mdaId;
    }

    public void setMdaId(int mdaId) {
        this.mdaId = mdaId;
    }

    public String getMda() {
        return mda;
    }

    public void setMda(String mda) {
        this.mda = mda;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPayerFirstName() {
        return payerFirstName;
    }

    public void setPayerFirstName(String payerFirstName) {
        this.payerFirstName = payerFirstName;
    }

    public String getPayerLastName() {
        return payerLastName;
    }

    public void setPayerLastName(String payerLastName) {
        this.payerLastName = payerLastName;
    }

    public String getAssesedService() {
        return assesedService;
    }

    public void setAssesedService(String assesedService) {
        this.assesedService = assesedService;
    }

    public String getAssesedServiceCode() {
        return assesedServiceCode;
    }

    public void setAssesedServiceCode(String assesedServiceCode) {
        this.assesedServiceCode = assesedServiceCode;
    }

    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }
}
