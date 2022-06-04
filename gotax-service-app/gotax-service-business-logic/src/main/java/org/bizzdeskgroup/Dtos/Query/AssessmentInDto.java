package org.bizzdeskgroup.Dtos.Query;

import java.sql.Timestamp;

public class AssessmentInDto {
    public int payerId;
    public String payer;
    public String payerFirstName;
    public String payerLastName;
//    public double Balance;
    public String invoiceNumber;
    public String payerTin;
//    public String ReferenceNumber;
    public boolean isSettled;
    public String taxType;
    public String taxYear;
    public String taxMonth;
    public double grossIncomeAmount;
    public boolean isObjected;
    public String assessmentOfficerRecommendation;
    public Timestamp recommendationDate;
    public double recommendedAmount;
    public String assesedService;
    public String assesedServiceCode;
//    public String AssessedBy;
    public int assesor;
    public int mdaId;
    public int projectId;
    public String phone;
    public int recordsPerPage;
    public int from;
    public int officeId;
}
