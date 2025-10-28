package matepore.email.sender.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Data transfer object for email information")
public class EmailDto {
    // Array of recipient email addresses
    @Schema(description = "Array of recipient email addresses")
    private String[] toAddress;

    // Email subject line
    @Schema(description = "Subject of the email")
    private String subject;

    // Main email content
    @Schema(description = "Body content of the email")
    private String message;
}
