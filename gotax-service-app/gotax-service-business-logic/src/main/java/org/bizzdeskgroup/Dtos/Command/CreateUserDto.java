package org.bizzdeskgroup.Dtos.Command;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class CreateUserDto {
    @NotNull
    @NotEmpty
    private String firstName;

    @NotEmpty
    @NotNull
    private String lastName;

    @NotNull
    @NotEmpty
    @Size(min = 7, message = "invalid phone")
    private String phone;

    @NotEmpty
    @Email(message = "Email not valid")
    private String email;

    @NotNull
    @NotEmpty
    @Size(min = 8, message = "password should have at least 8 characters")
    private String password;

    @NotNull
    @NotEmpty
    @Size(min = 8, message = "password should have at least 8 characters")
    private String passwordConfirmation;
//    private int createdBy;
    @NotNull
    private String role;

    //If user is Sub-Admin1 or Agent
    private CreateMdaUserDto userMda;

    private NewUserProject project;

    private boolean isDefaultPass;

//    public CreateUserDto(String firstName, String lastName, String phone, String email, String password, String passwordConfirmation, String role, CreateMdaUserDto userMda, NewUserProject project) {
//        super();
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.phone = phone;
//        this.email = email;
//        this.password = password;
//        this.passwordConfirmation = passwordConfirmation;
//        this.role = role;
//        this.userMda = userMda;
//        this.project = project;
//    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public CreateMdaUserDto getUserMda() {
        return userMda;
    }

    public void setUserMda(CreateMdaUserDto userMda) {
        this.userMda = userMda;
    }

    public NewUserProject getProject() {
        return project;
    }

    public void setProject(NewUserProject project) {
        this.project = project;
    }

    public boolean isDefaultPass() {
        return isDefaultPass;
    }

    public void setDefaultPass(boolean defaultPass) {
        isDefaultPass = defaultPass;
    }
}
