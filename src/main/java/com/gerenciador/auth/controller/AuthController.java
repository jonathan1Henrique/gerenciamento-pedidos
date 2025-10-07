package com.gerenciador.auth.controller;


import com.gerenciador.auth.record.LoginResponseRecord;
import com.gerenciador.auth.record.LoginResquetRecord;
import com.gerenciador.auth.record.RegisterRequestRecord;
import com.gerenciador.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestRecord request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseRecord> login(@RequestBody LoginResquetRecord request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
