package org.bizzdeskgroup.Dtos.Command;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

public class CustomerAssessmentDto {
    public int payerId;
//    public double balance;
//    public String invoiceNumber;
    public String payerTin;
    public String referenceNumber;
//    public boolean isSettled;
//    public int taxTypeId;//
    public String taxYear;
    public int taxMonth;
    public double grossIncomeAmount;
//    public double totalAmount;
//    public boolean isObjected;
    public String assessmentOfficerRecommendation;
//    public Timestamp RecommendationDate;
    public double recommendedAmount;
    public int assessedServiceId;
}
