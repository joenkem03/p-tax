package org.bizzdeskgroup.models;

import java.sql.Timestamp;

public class User extends BaseModel {
    public String firstName;
    public String lastName;
    public String otherNames;
    public String email;
    public String phone;
    public Timestamp lastUpdatedDate;
    public String role;
    public Timestamp lastLoginDate;
    public Timestamp loginFailedDate;
    public int loginFailedCount;
    public boolean isEmailConfirmed;
    public boolean isPhoneConfirmed;
    public Timestamp emailConfirmedDate;
    public Timestamp phoneConfirmedDate;
    public String emailConfirmationCode;
    public String phoneConfirmationCode;
    public Timestamp resetPasswordRequestDate;
    public String resetPasswordCode;
    public int resetPasswordCount;
    public byte[] passwordHash;
    public byte[] passwordSalt;
    public int businessId;
    public int updatedBy;
    public Timestamp updatedDate;
    public boolean isDefaultPass;



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Timestamp getLoginFailedDate() {
        return loginFailedDate;
    }

    public void setLoginFailedDate(Timestamp loginFailedDate) {
        this.loginFailedDate = loginFailedDate;
    }

    public int getLoginFailedCount() {
        return loginFailedCount;
    }

    public void setLoginFailedCount(int loginFailedCount) {
        this.loginFailedCount = loginFailedCount;
    }

    public boolean isEmailConfirmed() {
        return isEmailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        isEmailConfirmed = emailConfirmed;
    }

    public boolean isPhoneConfirmed() {
        return isPhoneConfirmed;
    }

    public void setPhoneConfirmed(boolean phoneConfirmed) {
        isPhoneConfirmed = phoneConfirmed;
    }

    public Timestamp getEmailConfirmedDate() {
        return emailConfirmedDate;
    }

    public void setEmailConfirmedDate(Timestamp emailConfirmedDate) {
        this.emailConfirmedDate = emailConfirmedDate;
    }

    public Timestamp getPhoneConfirmedDate() {
        return phoneConfirmedDate;
    }

    public void setPhoneConfirmedDate(Timestamp phoneConfirmedDate) {
        this.phoneConfirmedDate = phoneConfirmedDate;
    }

    public String getEmailConfirmationCode() {
        return emailConfirmationCode;
    }

    public void setEmailConfirmationCode(String emailConfirmationCode) {
        this.emailConfirmationCode = emailConfirmationCode;
    }

    public String getPhoneConfirmationCode() {
        return phoneConfirmationCode;
    }

    public void setPhoneConfirmationCode(String phoneConfirmationCode) {
        this.phoneConfirmationCode = phoneConfirmationCode;
    }

    public Timestamp getResetPasswordRequestDate() {
        return resetPasswordRequestDate;
    }

    public void setResetPasswordRequestDate(Timestamp resetPasswordRequestDate) {
        this.resetPasswordRequestDate = resetPasswordRequestDate;
    }

    public String getResetPasswordCode() {
        return resetPasswordCode;
    }

    public void setResetPasswordCode(String resetPasswordCode) {
        this.resetPasswordCode = resetPasswordCode;
    }

    public int getResetPasswordCount() {
        return resetPasswordCount;
    }

    public void setResetPasswordCount(int resetPasswordCount) {
        this.resetPasswordCount = resetPasswordCount;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    //    public boolean isActive() {
//        return IsActive;
//    }

//    public void setActive(boolean active) {
//        IsActive = active;
//    }


    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isDefaultPass() {
        return isDefaultPass;
    }

    public void setDefaultPass(boolean defaultPass) {
        isDefaultPass = defaultPass;
    }
}
