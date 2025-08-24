package br.com.grooworks.crestline.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth/docusign")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class CallbackController {
    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        return ResponseEntity.ok("Code recebido: " + code);
    }
}
