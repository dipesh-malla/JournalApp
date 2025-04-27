package malla.dipesh.journalApp.service;

import malla.dipesh.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import malla.dipesh.journalApp.model.User;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
@ActiveProfiles("dev")
public class CustomUserDetailServiceTest {

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUserNameTest(){
        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(Optional.of(User.builder().username("ram").password("ram").roles(new ArrayList<>()).build()));
        UserDetails userDetails = customUserDetailService.loadUserByUsername("ram");
        Assertions.assertNotNull(userDetails);
    }


}
