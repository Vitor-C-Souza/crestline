package br.com.grooworks.crestline.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth/docusign")
@CrossOrigin(origins = "*")
public class CallbackController {
    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        return ResponseEntity.ok("Code recebido: " + code);
    }
}
