package org.bizzdeskgroup.Dtos.Query;

import java.sql.Timestamp;
import java.util.List;

public class InternalUserDto {
    public int id;
    public int mdaId;
    public int projectId;
    public int businessId;
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
    public String tokenString;
    public String mda;
    public String project;
    public String clientLogo;
    public boolean isDefaultPass;
    public int mdaOfficeId;
    public List<String> projectPaymentChannels;
}
