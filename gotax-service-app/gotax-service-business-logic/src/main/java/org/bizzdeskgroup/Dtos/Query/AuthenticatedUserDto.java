package org.bizzdeskgroup.Dtos.Query;

import org.eclipse.microprofile.jwt.JsonWebToken;

import java.sql.Timestamp;
import java.util.List;

public class AuthenticatedUserDto {
    public String firstName;
    public String lastName;
    public String otherNames;
    public String email;
    public String phone;
    public Timestamp createdDate;
    public Timestamp lastUpdatedDate;
    public String role;
    public Timestamp lastLoginDate;
//    public JsonWebToken Token;
    public String token;
    public String mda;
    public String project;
    public List<String> projectPaymentChannels;
    public boolean isDefaultPass;
//    public String clientLogo;
//    public String tokenString;
}
