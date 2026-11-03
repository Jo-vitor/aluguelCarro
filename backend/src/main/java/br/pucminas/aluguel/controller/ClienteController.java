package br.pucminas.aluguel.controller;

import br.pucminas.aluguel.dto.Dtos.*;
import br.pucminas.aluguel.security.CurrentUser;
import br.pucminas.aluguel.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/clientes") @RequiredArgsConstructor
public class ClienteController {
    private final ClienteService service;

    @GetMapping("/me") @PreAuthorize("hasRole('CLIENTE')")
    public ClienteResponse meuPerfil(@AuthenticationPrincipal Jwt jwt) {
        return service.meuPerfil(CurrentUser.from(jwt).id());
    }

    @PutMapping("/me") @PreAuthorize("hasRole('CLIENTE')")
    public ClienteResponse atualizar(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody ClienteDados dados) {
        return service.atualizar(CurrentUser.from(jwt).id(), dados);
    }

    @DeleteMapping("/me") @PreAuthorize("hasRole('CLIENTE')") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@AuthenticationPrincipal Jwt jwt) { service.excluir(CurrentUser.from(jwt).id()); }

    @GetMapping @PreAuthorize("hasAnyRole('EMPRESA','BANCO')")
    public List<ClienteResponse> listar() { return service.listar(); }

    @GetMapping("/{id}") @PreAuthorize("hasAnyRole('EMPRESA','BANCO')")
    public ClienteResponse buscar(@PathVariable Long id) { return service.buscar(id); }
}
