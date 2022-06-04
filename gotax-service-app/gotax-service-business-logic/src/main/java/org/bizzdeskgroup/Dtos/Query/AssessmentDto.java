package org.bizzdeskgroup.Dtos.Query;

import java.sql.Timestamp;

public class AssessmentDto {
    public int id;
    public String payerFirstName;
    public String payerLastName;
//    public double Balance;
//    public String invoiceNumber;
    public String payerTin;
//    public String ReferenceNumber;
    public boolean isSettled;
    public String taxType;
    public String taxYear;
    public String taxMonth;
    public double grossIncomeAmount;
    public double taxAmount;
    public boolean isObjected;
    public String assessmentOfficerRecommendation;
    public Timestamp recommendationDate;
    public double recommendedAmount;
    public String assesedService;
    public String assesedServiceCode;
    public String assessedBy;
//    public int createdBy;
    public int mdaId;
    public double balance;
    public double amountPaid;
    public String objectionReason;
    public String objectionAuthorizedBy;
    public String mdaName;
    public String mdaOffice;
    public int hasInvoice;
}
