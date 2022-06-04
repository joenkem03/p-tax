package org.bizzdeskgroup.Dtos.Command;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateSelfUserDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String firstName;
    @NotNull
    @NotEmpty
    @NotBlank
    private String lastName;
    @NotNull
    @NotEmpty
    @NotBlank
    private String phone;

    @NotEmpty
    @NotBlank
    @Email(message = "Email not valid")
    private String email;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    @NotBlank
    private String passwordConfirmation;
    private int createdBy;
    @NotNull
    @NotEmpty
    @NotBlank
    private String role;

    @NotNull
    @NotEmpty
    @NotBlank
    private NewUserProject project;

//    public CreateSelfUserDto(String firstName, String lastName, String phone, String email, String password, String passwordConfirmation, int createdBy, String role) {
//        super();
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.phone = phone;
//        this.email = email;
//        this.password = password;
//        this.passwordConfirmation = passwordConfirmation;
//        this.createdBy = createdBy;
//        this.role = role;
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

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public NewUserProject getProject() {
        return project;
    }

    public void setProject(NewUserProject project) {
        this.project = project;
    }
}
