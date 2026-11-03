package br.pucminas.aluguel.controller;

import br.pucminas.aluguel.dto.Dtos.*;
import br.pucminas.aluguel.security.CurrentUser;
import br.pucminas.aluguel.service.AutomovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/automoveis") @RequiredArgsConstructor
public class AutomovelController {
    private final AutomovelService service;

    @GetMapping public List<AutomovelResponse> listar() { return service.listar(); }

    @GetMapping("/{id}") public AutomovelResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public AutomovelResponse criar(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody AutomovelRequest req) {
        return service.criar(CurrentUser.from(jwt), req);
    }
}
