package org.bizzdeskgroup.Dtos.Query;

import java.sql.Timestamp;

public class PosTransactionResponseDto {
    public String posDeviceSerial;
    public String operator;
//    public int CustomerId;
    public String customerName;
    public String customerPhone;
    public String customerEmail;
    public String assessmentInvoice;
    public String mda;
    public String mdaService;
    public String transactionId;
    public String transactionStatus;
    public String transactionDate;
    public String createdDate;
    public double amountPaid;
    public double pendingBalance;
}
