package com.ticket.monolithticketmonster.presentation.controller;

import com.ticket.monolithticketmonster.application.service.UserService;
import com.ticket.monolithticketmonster.infrastructure.security.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/oauth2")
public class OAuthController {

  private static final String GOOGLE_USERINFO_ENDPOINT =
      "https://www.googleapis.com/oauth2/v3/userinfo";
  private final RestTemplate restTemplate;
  private final JwtProvider jwtProvider;
  private final UserService userService;

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String clientSecret;

  @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
  private String redirectUri;

  @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
  private String authorizationUri;

  @Value("${spring.security.oauth2.client.provider.google.token-uri}")
  private String tokenUri;

  public OAuthController(
      RestTemplateBuilder restTemplateBuilder, JwtProvider jwtProvider, UserService userService) {
    this.restTemplate = restTemplateBuilder.build();
    this.jwtProvider = jwtProvider;
    this.userService = userService;
  }

  // Endpoint to initiate the OAuth2 flow by redirecting to Google
  @GetMapping("/redirect/google")
  public void redirectToGoogle(HttpServletResponse response) throws IOException {
    String authorizationUrl =
        String.format(
            "%s?client_id=%s&redirect_uri=%s&response_type=code&scope=openid%%20profile%%20email",
            authorizationUri, clientId, redirectUri);
    response.sendRedirect(authorizationUrl);
  }

  // Endpoint to handle the OAuth2 callback from Google
  @GetMapping("/callback/google-pkce")
  public ResponseEntity<?> handleGoogleCallback(@RequestParam("code") String authorizationCode) {

    // Prepare the request to the OAuth2 provider's token endpoint
    MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("client_id", clientId);
    requestParams.add("client_secret", clientSecret);
    requestParams.add("grant_type", "authorization_code");
    requestParams.add("code", authorizationCode);
    requestParams.add("redirect_uri", redirectUri);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<>(requestParams, headers);

    // Exchange the authorization code for an access token
    ResponseEntity<Map<String, Object>> responseEntity =
        restTemplate.exchange(
            tokenUri, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      Map<String, Object> tokenResponse = responseEntity.getBody();

      if (tokenResponse != null) {
        String accessToken = (String) tokenResponse.get("access_token");

        // Fetch the user's details using the access token
        HttpHeaders userDetailHeaders = new HttpHeaders();
        userDetailHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> userDetailRequest = new HttpEntity<>(userDetailHeaders);
        ResponseEntity<Map<String, Object>> userInfoResponse =
            restTemplate.exchange(
                GOOGLE_USERINFO_ENDPOINT,
                HttpMethod.GET,
                userDetailRequest,
                new ParameterizedTypeReference<>() {});

        Map<String, Object> userInfo = userInfoResponse.getBody();
        if (userInfo != null) {
          String email = (String) userInfo.get("email");
          String username = (String) userInfo.get("name");

          var user = userService.handleOAuth2User(email, username);

          String jwtToken = jwtProvider.generateToken(user.getUserId());

          Map<String, Object> response = new HashMap<>();
          response.put("access_token", jwtToken);

          return ResponseEntity.ok(response);
        }
      }
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Failed to exchange authorization code or fetch user info");
  }
}
