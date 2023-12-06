package ru.nsu.enrollease;

import org.springframework.web.client.RestTemplate;


import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class GoogleAuthService {

    private final RestTemplate restTemplate;

    public GoogleAuthService() {
        this.restTemplate = new RestTemplate();
    }

    public String getEmailFromGoogleToken(String accessToken) {
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v2/userinfo";
//        Map<String, String> response = restTemplate.getForObject(userInfoEndpoint + "?access_token=" + accessToken, Map.class);
//        return response.get("email");
        return "Stub";
    }
}
