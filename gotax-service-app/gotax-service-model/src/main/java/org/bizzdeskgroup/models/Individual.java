package org.bizzdeskgroup.models;

import java.sql.Timestamp;

public class Individual extends AuditedBaseModel{
    public String title;
    public int userId;
    public Timestamp dateOfBirth;
    public String maritalStatus;
    public String nationality;
    public int residenceLgaId;
    public int residenceStateId;
    public String residentialAddress;
//    public int numberOfChildren;
    public String occupation;
    public String officeAddress;
    public String employerName;
    public String temporaryTin;
    public String jtbTin;
    public String nin;
//    public String latLong;
    public int businessId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getResidenceLgaId() {
        return residenceLgaId;
    }

    public void setResidenceLgaId(int residenceLgaId) {
        this.residenceLgaId = residenceLgaId;
    }

    public int getResidenceStateId() {
        return residenceStateId;
    }

    public void setResidenceStateId(int residenceStateId) {
        this.residenceStateId = residenceStateId;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

//    public int getNumberOfChildren() {
//        return numberOfChildren;
//    }
//
//    public void setNumberOfChildren(int numberOfChildren) {
//        this.numberOfChildren = numberOfChildren;
//    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

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

//    public String getLatLong() {
//        return latLong;
//    }
//
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
