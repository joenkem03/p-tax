package org.bizzdeskgroup.Dtos.Command;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Date;

public class CreatePosTransactionDto {
    public int posId;
//    public int OperatorId;
    public int customerId;
    public String customerName;
    public String customerPhone;
    public String customerEmail;
    public int assessmentId;
    public int mdaServiceId;
    public String invoiceNo;
    public String offlineTransactionId;
    public Date transactionDate;
    public double amountPaid;
    public boolean isCard;
    public boolean isAssessment;
}
