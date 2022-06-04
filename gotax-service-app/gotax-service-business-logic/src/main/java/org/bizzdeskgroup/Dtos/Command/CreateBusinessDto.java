package org.bizzdeskgroup.Dtos.Command;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CreateBusinessDto {
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
    @NotEmpty
    @NotBlank
    public double comission;
    @NotEmpty
    @NotBlank
    public boolean isCommissionPercent;

    public List<Integer> paymentChannels;
}
