package br.pucminas.aluguel.controller;

import br.pucminas.aluguel.dto.Dtos.*;
import br.pucminas.aluguel.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) { return auth.login(req); }

    @PostMapping("/registro/cliente") @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registrarCliente(@Valid @RequestBody RegistroCliente req) { return auth.registrarCliente(req); }

    @PostMapping("/registro/agente") @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registrarAgente(@Valid @RequestBody RegistroAgente req) { return auth.registrarAgente(req); }
}
