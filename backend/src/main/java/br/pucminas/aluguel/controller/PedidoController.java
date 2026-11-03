package br.pucminas.aluguel.controller;

import br.pucminas.aluguel.dto.Dtos.*;
import br.pucminas.aluguel.security.CurrentUser;
import br.pucminas.aluguel.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/pedidos") @RequiredArgsConstructor
public class PedidoController {
    private final PedidoService service;

    @PostMapping @PreAuthorize("hasRole('CLIENTE')") @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criar(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody PedidoRequest req) {
        return service.criar(CurrentUser.from(jwt), req);
    }

    @GetMapping
    public List<PedidoResponse> listar(@AuthenticationPrincipal Jwt jwt) { return service.listar(CurrentUser.from(jwt)); }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return service.buscar(CurrentUser.from(jwt), id);
    }

    @PutMapping("/{id}")
    public PedidoResponse modificar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id,
                                    @Valid @RequestBody PedidoRequest req) {
        return service.modificar(CurrentUser.from(jwt), id, req);
    }

    @PostMapping("/{id}/cancelar") @PreAuthorize("hasRole('CLIENTE')")
    public PedidoResponse cancelar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return service.cancelar(CurrentUser.from(jwt), id);
    }

    @PostMapping("/{id}/avaliar") @PreAuthorize("hasAnyRole('EMPRESA','BANCO')")
    public PedidoResponse avaliar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id,
                                  @Valid @RequestBody AvaliacaoRequest req) {
        return service.avaliar(CurrentUser.from(jwt), id, req);
    }

    @PostMapping("/{id}/contrato") @PreAuthorize("hasAnyRole('EMPRESA','BANCO')")
    public PedidoResponse executarContrato(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id,
                                           @Valid @RequestBody ContratoRequest req) {
        return service.executarContrato(CurrentUser.from(jwt), id, req);
    }
}
