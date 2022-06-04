package org.bizzdeskgroup.Dtos.PayStack;

import java.util.Date;

public class data {
    private int id;
    private double amount;
    private String currency;
    private Date transaction_date;
    private String status;
    private String reference;
    private String domain;
    private Object metadata;
    private String gateway_response;
    private String message;
    private String channel;
    private String ip_address;
    private Object log;
    private String fees;
    private String authorization;
    private customer customer;
    private String plan;
    private double requested_amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public String getGateway_response() {
        return gateway_response;
    }

    public void setGateway_response(String gateway_response) {
        this.gateway_response = gateway_response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public Object getLog() {
        return log;
    }

    public void setLog(Object log) {
        this.log = log;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public customer getCustomer() {
        return customer;
    }

    public void setCustomer(customer customer) {
        this.customer = customer;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public double getRequested_amount() {
        return requested_amount;
    }

    public void setRequested_amount(double requested_amount) {
        this.requested_amount = requested_amount;
    }

    @Override
    public String toString() {
        return "data{" +
                "id=" + id +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", transaction_date=" + transaction_date +
                ", status='" + status + '\'' +
                ", reference='" + reference + '\'' +
                ", domain='" + domain + '\'' +
                ", metadata=" + metadata +
                ", gateway_response='" + gateway_response + '\'' +
                ", message='" + message + '\'' +
                ", channel='" + channel + '\'' +
                ", ip_address='" + ip_address + '\'' +
                ", log=" + log +
                ", fees='" + fees + '\'' +
                ", authorization='" + authorization + '\'' +
                ", customer=" + customer +
                ", plan='" + plan + '\'' +
                ", requested_amount=" + requested_amount +
                '}';
    }
}


//{
//        "": 27000,
//        "": "NGN",
//        "": "2016-10-01T11:03:09.000Z",
//        "": "success",
//        "": "DG4uishudoq90LD",
//        "": "test",
//        "": 0,
//        "": "Successful",
//        "": null,
//        "": "card",
//        "": "41.1.25.1",

//        "": {
//        "time_spent": 9,
//        "attempts": 1,
//        "authentication": null,
//        "errors": 0,
//        "success": true,
//        "mobile": false,
//        "input": [],
//        "channel": null,
//        "history": [{
//        "type": "input",
//        "message": "Filled these fields: card number, card expiry, card cvv",
//        "time": 7
//        },
//        {
//        "type": "action",
//        "message": "Attempted to pay",
//        "time": 7
//        },
//        {
//        "type": "success",
//        "message": "Successfully paid",
//        "time": 8
//        },
//        {
//        "type": "close",
//        "message": "Page closed",
//        "time": 9
//        }
//        ]
//        }

//        "": null,

//        "": {
//        "authorization_code": "AUTH_8dfhjjdt",
//        "card_type": "visa",
//        "last4": "1381",
//        "exp_month": "08",
//        "exp_year": "2018",
//        "bin": "412345",
//        "bank": "TEST BANK",
//        "channel": "card",
//        "signature": "SIG_idyuhgd87dUYSHO92D",
//        "reusable": true,
//        "country_code": "NG",
//        "account_name": "BoJack Horseman"
//        },


//        "": "PLN_0as2m9n02cl0kp6",
//        "": 1500000
//        }