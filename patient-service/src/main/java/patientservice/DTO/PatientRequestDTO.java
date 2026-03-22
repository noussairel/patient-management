package patientservice.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import patientservice.DTO.validators.CreatePatientValidationGroup;

@Getter
@Setter
public class PatientRequestDTO {
    @NotBlank
    @Size(max = 100, message = "Name could not exceed 100 characters")
    private String name;
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank
    private String address;
    @NotBlank
    private String dateOfBirth;

    @NotBlank(groups = CreatePatientValidationGroup.class)
    private String registeredDate;
}
