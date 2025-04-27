package malla.dipesh.journalApp.controller;

import malla.dipesh.journalApp.OAuth.GoogleOAuth;
import malla.dipesh.journalApp.model.User;
import malla.dipesh.journalApp.service.CustomUserDetailService;
import malla.dipesh.journalApp.service.JwtService;
import malla.dipesh.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;



@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
   private GoogleOAuth googleOAuth;

    @GetMapping("/google/oauth")
    public ResponseEntity<?> getGoogleOAuth(
            @RequestParam String code
    ) {
        return googleOAuth.googleAuthService(code);
    }

}
