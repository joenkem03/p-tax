package org.bizzdeskgroup.Dtos.Query;

public class InvoiceDto {
    private String Payer;
    private String PayerEmail;
    private String PayerPhone;
    private String PayerTin;
    private String invoiceNo;
    private double invoiceAmount;
//    private int assessmentId;
    private String assesedService;
    private boolean isPaid;
    private String paymentChannel;
    private int businessId;
    private String business;
//    private int mdaId;
    private int serviceId;
    private String mda;
    private int month;
    private String year;
    private int customerId;
    private String payerFirstName;
    private String payerLastName;
    private String PayerType;
    private String tinType;

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

    public String getAssesedService() {
        return assesedService;
    }

    public void setAssesedService(String assesedService) {
        this.assesedService = assesedService;
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

//    public int getMdaId() {
//        return mdaId;
//    }
//
//    public void setMdaId(int mdaId) {
//        this.mdaId = mdaId;
//    }

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

//    public int getUserId() {
//        return userId;
//    }

//    public void setUserId(int userId) {
//        this.userId = userId;
//    }

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

    public String getPayerEmail() {
        return PayerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        PayerEmail = payerEmail;
    }

    public String getPayerPhone() {
        return PayerPhone;
    }

    public void setPayerPhone(String payerPhone) {
        PayerPhone = payerPhone;
    }

//    public String getPayerTin() {
//        return PayerTin;
//    }
//
//    public void setPayerTin(String payerTin) {
//        PayerTin = payerTin;
//    }


    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getPayerTin() {
        return PayerTin;
    }

    public void setPayerTin(String payerTin) {
        PayerTin = payerTin;
    }

    public String getPayerType() {
        return PayerType;
    }

    public void setPayerType(String payerType) {
        PayerType = payerType;
    }

    public String getTinType() {
        return tinType;
    }

    public void setTinType(String tinType) {
        this.tinType = tinType;
    }
}
