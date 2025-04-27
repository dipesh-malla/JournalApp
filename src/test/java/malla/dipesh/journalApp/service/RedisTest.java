package malla.dipesh.journalApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

//    @BeforeEach
//    void setUp(){
//       MockitoAnnotations.openMocks(this);
//    }



    @Test
    void testSendMail(){
        redisTemplate.opsForValue().set("email","test@gmail.com");
        Object salary = redisTemplate.opsForValue().get("name");
        int a =1;

    }
}
