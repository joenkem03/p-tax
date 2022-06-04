package org.bizzdeskgroup.Dtos.Query;

import java.sql.Timestamp;

public class PosTransactionDto {


//    public String Id;
//    public String PosId;
//    public String OperatorId;
//    public String CustomerId;
    private String transactionId;
    private String transactionDate;
    private String amountPaid;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
//    private String assessmentId;
//    private String mdaServiceId;
    private String transactionStatus;
//    private String offlineTransactionId;
    private String assesedService;
    private String assesedServiceCode;
    private String posDeviceSerial;
    private String operatorFirstName;
    private String operatorLastName;
    private String mdaName;
    private String mdaOffice;
    private int paymentStatus;
    
//
//    public String PosDeviceSerial;
//    public String Operator;
////    public int CustomerId;
//    public String CustomerName;
//    public String CustomerPhone;
//    public String CustomerEmail;
//    public int AssessmentInvoice;
//    public int Mda;
//    public int MdaService;
//    public String TransactionId;
//    public Timestamp CreatedDate;
//    public double AmountPaid;


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

//    public String getAssessmentId() {
//        return assessmentId;
//    }
//
//    public void setAssessmentId(String assessmentId) {
//        this.assessmentId = assessmentId;
//    }
//
//    public String getMdaServiceId() {
//        return mdaServiceId;
//    }
//
//    public void setMdaServiceId(String mdaServiceId) {
//        this.mdaServiceId = mdaServiceId;
//    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

//    public String getOfflineTransactionId() {
//        return offlineTransactionId;
//    }
//
//    public void setOfflineTransactionId(String offlineTransactionId) {
//        this.offlineTransactionId = offlineTransactionId;
//    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
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

    public String getPosDeviceSerial() {
        return posDeviceSerial;
    }

    public void setPosDeviceSerial(String posDeviceSerial) {
        this.posDeviceSerial = posDeviceSerial;
    }

    public String getOperatorFirstName() {
        return operatorFirstName;
    }

    public void setOperatorFirstName(String operatorFirstName) {
        this.operatorFirstName = operatorFirstName;
    }

    public String getOperatorLastName() {
        return operatorLastName;
    }

    public void setOperatorLastName(String operatorLastName) {
        this.operatorLastName = operatorLastName;
    }

    public String getMdaName() {
        return mdaName;
    }

    public void setMdaName(String mdaName) {
        this.mdaName = mdaName;
    }

    public String getMdaOffice() {
        return mdaOffice;
    }

    public void setMdaOffice(String mdaOffice) {
        this.mdaOffice = mdaOffice;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
