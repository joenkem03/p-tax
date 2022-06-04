package org.bizzdeskgroup.Dtos.Command;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class AgentCreateUserDto {
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
//    public int createdBy;
    @NotNull
    public String role;

}
