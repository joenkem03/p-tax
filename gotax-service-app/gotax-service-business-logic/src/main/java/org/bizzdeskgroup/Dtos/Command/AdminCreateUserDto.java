package org.bizzdeskgroup.Dtos.Command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;

public class AdminCreateUserDto {
    @NotNull
    public String firstName;
    @NotNull
    public String lastName;
    @NotNull
    public String phone;

    @Email(message = "Email not valid")
    public String email;

//    @NotNull()
//    public String password;
//    @NotNull
//    public String passwordConfirmation;
////    public int createdBy;
    @NotNull
    public String role;

    //If user is Sub-Admin1 or Agent
    public CreateMdaUserDto userMda;
    public NewUserProject project;
}
