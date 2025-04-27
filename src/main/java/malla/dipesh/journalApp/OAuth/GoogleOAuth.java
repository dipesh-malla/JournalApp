package malla.dipesh.journalApp.OAuth;

import malla.dipesh.journalApp.model.User;
import malla.dipesh.journalApp.service.CustomUserDetailService;
import malla.dipesh.journalApp.service.JwtService;
import malla.dipesh.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class GoogleOAuth {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    public ResponseEntity<?> googleAuthService(String code) {
        try {
            // Step 1: Exchange code for access and ID tokens
            String tokenEndPoint = "https://oauth2.googleapis.com/token";
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", "https://developers.google.com/oauthplayground"); // Make sure this matches what you authorized

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndPoint, requestEntity, Map.class);

            if (tokenResponse.getStatusCode() != HttpStatus.OK || tokenResponse.getBody() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error response from token endpoint: " + tokenResponse.getBody());
            }

            // Step 2: Use access token to fetch user info
            String accessToken = (String) tokenResponse.getBody().get("access_token");

            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);
            HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v2/userinfo",
                    HttpMethod.GET,
                    userInfoRequest,
                    Map.class
            );

            if (userInfoResponse.getStatusCode() == HttpStatus.OK && userInfoResponse.getBody() != null) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                String name = (String) userInfo.get("name");

                if (email == null || name == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or name not available in user info.");
                }

                UserDetails userDetails;
                try {
                    userDetails = customUserDetailService.loadUserByUsername(email);
                } catch (Exception e) {
                    System.out.println("User not found: " + e.getMessage());
                    // Register the user if not found
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(name);
                    newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // random password
                    newUser.setRoles(List.of("USER"));
                    userService.saveUser(newUser);

                    userDetails = customUserDetailService.loadUserByUsername(email);
                }

                // Step 3: Generate JWT tokens
                String jwtAccessToken = jwtService.generateAccessToken(email);
                String jwtRefreshToken = jwtService.generateRefreshToken(email);

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, createRefreshTokenCookie(jwtRefreshToken))
                        .body(Map.of(
                                "accessToken", jwtAccessToken,
                                "email", email,
                                "name", name
                        ));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to retrieve user info from Google.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + e.getMessage());
        }
    }



    private String createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh-token")
                .maxAge(7 * 24 * 60 * 60)
                .build().toString();
    }
}
