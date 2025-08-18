package br.com.grooworks.crestline.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/docusign")
public class CallbackController {
    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        return ResponseEntity.ok("Code recebido: " + code);
    }
}
