package org.bizzdeskgroup.Dtos.Query;

//@XmlRootElement(name = "RemittanceDto")
public class RemittanceIntDto {
    private String remittanceNo;
    private double totalAmount;
    private String generatedDate;
    private String operatorFirstName;
    private String operatorLastName;
    private int agentId;
    private int mdaId;
    private String posSerial;
    private String remittanceStatus;
    private String paymentDate;

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

    public String getRemittanceNo() {
        return remittanceNo;
    }

    public void setRemittanceNo(String remittanceNo) {
        this.remittanceNo = remittanceNo;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getMdaId() {
        return mdaId;
    }

    public void setMdaId(int mdaId) {
        this.mdaId = mdaId;
    }

    public String getPosSerial() {
        return posSerial;
    }

    public void setPosSerial(String posSerial) {
        this.posSerial = posSerial;
    }

    public String getRemittanceStatus() {
        return remittanceStatus;
    }

    public void setRemittanceStatus(String remittanceStatus) {
        this.remittanceStatus = remittanceStatus;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
