package org.bizzdeskgroup.Dtos.Query;

import java.sql.Timestamp;

public class UserAdminDto {
    public int id;
    public String firstName;
    public String lastName;
    public String otherNames;
    public String email;
    public String phone;
    public Timestamp createdDate;
    public Timestamp lastUpdatedDate;
    public String role;
    public Timestamp lastLoginDate;
    public String state;
    public String project;
    public String mda;
    public String office;
//    public Timestamp LoginFailedDate;
//    public int LoginFailedCount;
//    public boolean IsEmailConfirmed;
//    public boolean IsPhoneConfirmed;
//    public Timestamp EmailConfirmedDate;
//    public Timestamp PhoneConfirmedDate;
//    public String EmailConfirmationCode;
//    public String PhoneConfirmationCode;
//    public Timestamp ResetPasswordRequestDate;
//    public String ResetPasswordCode;
//    public int ResetPasswordCount;
    public boolean isActive;
}
