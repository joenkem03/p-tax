package org.bizzdeskgroup.Dtos.Command;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class UpdateBusinessDto {
    public int id;
    @NotEmpty
    @NotBlank
    public String clientName;
    public String clientLogo;
    @Min(1)
    public int stateLgaId;
    @NotEmpty
    @NotBlank
    public String contactPerson;
    @NotEmpty
    @NotBlank
    @Min(8)
    public String contactPhone;
    @NotEmpty
    @NotBlank
    @Email
    public String contactEmail;
    public boolean isRebateEnabled;
}
