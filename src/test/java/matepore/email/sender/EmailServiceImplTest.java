package matepore.email.sender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import matepore.email.sender.exception.EmailSendException;
import matepore.email.sender.service.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private final String addressFrom = "test@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailServiceImpl(addressFrom, mailSender);
    }

    //Tests the functionality of sendEmail with correct values
    @Test
    @DisplayName("testing the method sendEmail successfully")
    void test_send_email_successfully() {
        String[] toAddress = {"to.address@example.com"};
        String subject = "Test Subject";
        String message = "This is a test email message.";

        emailService.sendEmail(toAddress, subject, message);

        //This captures the SimpleMailMessage sent to the mailSender
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertArrayEquals(toAddress, sentMessage.getTo());
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(message, sentMessage.getText());
        assertEquals(addressFrom, sentMessage.getFrom());
    }

    @Test
    void send_email_with_file_should_send_mail_with_attachment() throws Exception {
        String[] toAddress = {"to.address@example.com"};
        String subject = "Test Subject";
        String message = "This is a test email message.";
        File file = new File("test.txt");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        //We use a spy to verify the helper internally
        try (MockedConstruction<MimeMessageHelper> helperMocked = mockConstruction(
                MimeMessageHelper.class,
                (helper, context) -> {
                    doNothing().when(helper).setFrom(anyString());
                    doNothing().when(helper).setTo(any(String[].class));
                    doNothing().when(helper).setSubject(anyString());
                    doNothing().when(helper).setText(anyString());
                    doNothing().when(helper).addAttachment(anyString(), any(File.class));
                })) {

            emailService.sendEmailWithFile(toAddress, subject, message, file);

            verify(mailSender, times(1)).send(mimeMessage);

            MimeMessageHelper helper = helperMocked.constructed().getFirst();
            verify(helper, times(1)).setFrom(addressFrom);
            verify(helper, times(1)).setTo(toAddress);
            verify(helper, times(1)).setSubject(subject);
            verify(helper, times(1)).setText(message);
            verify(helper, times(1)).addAttachment(file.getName(), file);
        }
    }

    @Test
    void send_email_with_file_should_throw_email_send_exception_When_messaging_exception_occurs() throws Exception {
        String[] toAddress = {"to.address@example.com"};
        String subject = "Test Exception";
        String message = "Error case";
        File file = new File("test.txt");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        //We force an exception when we construct the helper
        try (MockedConstruction<MimeMessageHelper> helperMocked = mockConstruction(
                MimeMessageHelper.class,
                (helper, context) -> {
                    doThrow(new MessagingException("Simulated error")).when(helper).addAttachment(anyString(), any(File.class));
                })) {

            EmailSendException ex = assertThrows(EmailSendException.class, () -> emailService.sendEmailWithFile(toAddress, subject, message, file));

            assertTrue(ex.getMessage().contains("Error sending email with attachment"));
            assertInstanceOf(MessagingException.class, ex.getCause());
            verify(mailSender, never()).send(any(MimeMessage.class));
        }
    }
}
