package br.com.grooworks.crestline.domain.service;

import br.com.grooworks.crestline.domain.service.impl.DocuSignAuthServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenScheduler {

    private final DocuSignAuthServiceImpl authService;

    public TokenScheduler(DocuSignAuthServiceImpl authService) {
        this.authService = authService;
    }

    @Scheduled(fixedRate = 25 * 60 * 1000)
    public void refreshToken() {
        authService.getAccessToken();
    }
}
