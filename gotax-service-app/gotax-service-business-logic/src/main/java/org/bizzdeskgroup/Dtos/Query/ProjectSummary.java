package org.bizzdeskgroup.Dtos.Query;

public class ProjectSummary {
    private double TotalAmountPaid;
    private double ServiceAmount;
    private double Commission;
    private double Fee;

    public double getTotalAmountPaid() {
        return TotalAmountPaid;
    }

    public void setTotalAmountPaid(double totalAmountPaid) {
        TotalAmountPaid = totalAmountPaid;
    }

    public double getServiceAmount() {
        return ServiceAmount;
    }

    public void setServiceAmount(double serviceAmount) {
        ServiceAmount = serviceAmount;
    }

    public double getCommission() {
        return Commission;
    }

    public void setCommission(double commission) {
        Commission = commission;
    }

    public double getFee() {
        return Fee;
    }

    public void setFee(double fee) {
        Fee = fee;
    }
}
