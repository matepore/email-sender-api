package matepore.email.sender.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailFileDto {
    private String[] toAddress;
    private String subject;
    private String message;
    private MultipartFile file;
}
