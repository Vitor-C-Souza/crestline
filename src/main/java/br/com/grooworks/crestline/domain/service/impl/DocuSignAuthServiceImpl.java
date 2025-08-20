package br.com.grooworks.crestline.domain.service.impl;

import br.com.grooworks.crestline.domain.service.DocuSignAuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class DocuSignAuthServiceImpl implements DocuSignAuthService {

    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;

    @Value("${docusign.client-id}")
    private String clientId;

    @Value("${docusign.client-secret}")
    private String clientSecret;

    @Value("${docusign.token-url}")
    private String tokenUrl;

    @Value("${docusign.refresh-token}")
    public void setRefreshToken(String token) {
        this.refreshToken = token;
    }

    @Override
    public synchronized String getAccessToken() {
        if (accessToken == null || Instant.now().isAfter(expiresAt)) {
            refreshAccessToken();
        }
        return accessToken;
    }

    private void refreshAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Basic Auth
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        Map body = response.getBody();

        this.accessToken = (String) body.get("access_token");
        this.refreshToken = (String) body.get("refresh_token");
        int expiresIn = (Integer) body.get("expires_in");
        this.expiresAt = Instant.now().plusSeconds(expiresIn - 60);
    }
}
