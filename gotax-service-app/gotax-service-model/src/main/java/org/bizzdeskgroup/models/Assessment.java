package org.bizzdeskgroup.models;

import java.sql.Timestamp;

public class Assessment extends AuditedBaseModel{

    /***
     * CREATE TABLE `posmetas` (
     *   `Id` int(11) NOT NULL AUTO_INCREMENT,
     *   `UserId` int(11),
     *   `Balance` text,
     *   `InvoiceNumber` text,
     *   `PayerTin` text,
     *   `ReferenceNumber` text,
     *   `IsSettled` text,
     *   `TaxType` text,
     *   `TaxYear` text,
     *   `TaxMonth` text,
     *   `TotalAmount` text,
     *   `IsObjected` tinyint(1),
     *   `CollectionOfficerRecommendation` text,
     *   `RecommendationDate` timestamp,
     *   `RecommendedAmount` text,
     *   `CreatedBy` int,
     *   `CreatedDate` timestamp,
     *   PRIMARY KEY (`Id`)
     * ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
     * SELECT * FROM eigr_taraba.assessment;
     */


    public int payerId;
    public double balance;
    public String invoiceNumber;
    public String payerTin;
    public String referenceNumber;
    public boolean isSettled;
//    public int taxTypeId;
    public String taxYear;
    public String taxMonth;
    public double taxAmount;
    public boolean isObjected;
    public boolean objectionRequest;
    public String objectionReason;
    public int objectionAuthorizedBy;
    public String assessmentOfficerRecommendation;
    public Timestamp recommendationDate;
    public double recommendedAmount;
    public int recommendationBy;
    public int assessedServiceId;
    public double AmountPaid;
//
    public double grossIncomeAmount;
    public int businessId;
    public int mdaId;
    public int mdaOfficeId;

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPayerTin() {
        return payerTin;
    }

    public void setPayerTin(String payerTin) {
        this.payerTin = payerTin;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public boolean isSettled() {
        return isSettled;
    }

    public void setSettled(boolean settled) {
        isSettled = settled;
    }

//    public int getTaxTypeId() {
//        return taxTypeId;
//    }

//    public void setTaxTypeId(int taxTypeId) {
//        this.taxTypeId = taxTypeId;
//    }

    public String getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(String taxYear) {
        this.taxYear = taxYear;
    }

    public String getTaxMonth() {
        return taxMonth;
    }

    public void setTaxMonth(String taxMonth) {
        this.taxMonth = taxMonth;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public boolean isObjected() {
        return isObjected;
    }

    public void setObjected(boolean objected) {
        isObjected = objected;
    }

    public String getAssessmentOfficerRecommendation() {
        return assessmentOfficerRecommendation;
    }

    public void setAssessmentOfficerRecommendation(String assessmentOfficerRecommendation) {
        this.assessmentOfficerRecommendation = assessmentOfficerRecommendation;
    }

    public Timestamp getRecommendationDate() {
        return recommendationDate;
    }

    public void setRecommendationDate(Timestamp recommendationDate) {
        this.recommendationDate = recommendationDate;
    }

    public double getRecommendedAmount() {
        return recommendedAmount;
    }

    public void setRecommendedAmount(double recommendedAmount) {
        this.recommendedAmount = recommendedAmount;
    }

    public int getAssessedServiceId() {
        return assessedServiceId;
    }

    public void setAssessedServiceId(int assessedServiceId) {
        this.assessedServiceId = assessedServiceId;
    }

    public double getAmountPaid() {
        return AmountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        AmountPaid = amountPaid;
    }

    public double getGrossIncomeAmount() {
        return grossIncomeAmount;
    }

    public void setGrossIncomeAmount(double grossIncomeAmount) {
        this.grossIncomeAmount = grossIncomeAmount;
    }

    public boolean isObjectionRequest() {
        return objectionRequest;
    }

    public void setObjectionRequest(boolean objectionRequest) {
        this.objectionRequest = objectionRequest;
    }

    public String getObjectionReason() {
        return objectionReason;
    }

    public void setObjectionReason(String objectionReason) {
        this.objectionReason = objectionReason;
    }

    public int getObjectionAuthorizedBy() {
        return objectionAuthorizedBy;
    }

    public void setObjectionAuthorizedBy(int objectionAuthorizedBy) {
        this.objectionAuthorizedBy = objectionAuthorizedBy;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getRecommendationBy() {
        return recommendationBy;
    }

    public void setRecommendationBy(int recommendationBy) {
        this.recommendationBy = recommendationBy;
    }

    public int getMdaId() {
        return mdaId;
    }

    public void setMdaId(int mdaId) {
        this.mdaId = mdaId;
    }

    public int getMdaOfficeId() {
        return mdaOfficeId;
    }

    public void setMdaOfficeId(int mdaOfficeId) {
        this.mdaOfficeId = mdaOfficeId;
    }
}
