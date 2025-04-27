package malla.dipesh.journalApp.service;

import malla.dipesh.journalApp.model.User;
import malla.dipesh.journalApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;



    @BeforeEach
    void setUp() {
        System.out.println("hi");
    }
    @BeforeAll
    static void setUpAll() {
        System.out.println("hi");
    }

    // similarly @AfterEach and @AfterAll




    @ParameterizedTest
//    @CsvSource({
//            "ram",
//            "ramd",
//    })
    @ValueSource(strings = {
            "ram",
            "ramd",
    })
//    @EnumSource({})
    public void testFindByUserName(String username) {
        assertTrue(userRepository.findByUsername(username).isEmpty(), "User should exist");
    }



    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1, 2, 3",
            "2, 3, 5",
            "3, 4, 7",
            "4, 5, 9"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a+b);
    }
}
