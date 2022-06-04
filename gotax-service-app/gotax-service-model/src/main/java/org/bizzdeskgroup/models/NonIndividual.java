package org.bizzdeskgroup.models;

import java.sql.Timestamp;

public class NonIndividual extends AuditedBaseModel{
    public String cacRegNo;
    public String companyName;
    public String companyAddress;
    public int payerId;
    public String city;
    public int lgaId;
    public String phoneNumber1;
    public String phoneNumber2;
    public String email;
    public String website;
//    public int numberOfWorkers;
    public String temporaryTin;
    public String jtbTin;
    public String nin;
    public Timestamp companyRegistrationDate;
    public Timestamp companyCommencementDate;
    public String businessType;
    public int businessId;

//    public String latLong;

    public String getCacRegNo() {
        return cacRegNo;
    }

    public void setCacRegNo(String cacRegNo) {
        this.cacRegNo = cacRegNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getLgaId() {
        return lgaId;
    }

    public void setLgaId(int lgaId) {
        this.lgaId = lgaId;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

//    public int getNumberOfWorkers() {
//        return numberOfWorkers;
//    }
//
//    public void setNumberOfWorkers(int numberOfWorkers) {
//        this.numberOfWorkers = numberOfWorkers;
//    }

    public String getTemporaryTin() {
        return temporaryTin;
    }

    public void setTemporaryTin(String temporaryTin) {
        this.temporaryTin = temporaryTin;
    }

    public String getJtbTin() {
        return jtbTin;
    }

    public void setJtbTin(String jtbTin) {
        this.jtbTin = jtbTin;
    }

    public Timestamp getCompanyRegistrationDate() {
        return companyRegistrationDate;
    }

    public void setCompanyRegistrationDate(Timestamp companyRegistrationDate) {
        this.companyRegistrationDate = companyRegistrationDate;
    }

    public Timestamp getCompanyCommencementDate() {
        return companyCommencementDate;
    }

    public void setCompanyCommencementDate(Timestamp companyCommencementDate) {
        this.companyCommencementDate = companyCommencementDate;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

//    public String getLatLong() {
//        return latLong;
//    }

//    public void setLatLong(String latLong) {
//        this.latLong = latLong;
//    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }
}
