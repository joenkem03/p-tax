package org.bizzdeskgroup.Dtos.Command;

import java.util.Date;

public class CreatePosCardTransactionDto {
    public String transactionId;
    public String transactionReference;
    public Date transactionDate;
    public String pan;
    public String transactionDescription;
    public double totalAmountPaid;
    public double serviceAmount;
    public double fee;
    public double commission;
    public String transactionType;
    public String channel;
    public String customerName;
    public String customerPhone;
    public String customerEmail;
    public int mdaId;
    public int businessId;
    public String terminalId;
    public int operatorId;
    public int customerId;
}
