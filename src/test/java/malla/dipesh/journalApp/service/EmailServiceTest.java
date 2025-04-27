package malla.dipesh.journalApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;


public class EmailServiceTest
{
    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMail(){
        emailService.sendMail("novaluna1913@gmail.com",
                "Test Subject",
                "Test Body");

    }
}
