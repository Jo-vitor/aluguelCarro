package br.pucminas.aluguel.dto;

import br.pucminas.aluguel.model.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public final class Dtos {
    private Dtos() {}

    public record LoginRequest(@NotBlank String username, @NotBlank String senha) {}
    public record AuthResponse(String token, String username, Papel papel, String nome) {}

    public record RendimentoDto(@NotBlank String entidadeEmpregadora,
                                @NotNull @DecimalMin("0.0") BigDecimal valor) {}

    public record ClienteDados(
            @NotBlank String nome,
            @NotBlank String rg,
            @NotBlank @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos") String cpf,
            @NotBlank String endereco,
            @NotBlank String profissao,
            @Valid @Size(max = 3, message = "No máximo 3 rendimentos") List<RendimentoDto> rendimentos) {}

    public record RegistroCliente(
            @NotBlank @Size(min = 3, message = "Usuário deve ter ao menos 3 caracteres") String username,
            @NotBlank @Size(min = 6, message = "Senha deve ter ao menos 6 caracteres") String senha,
            @NotNull @Valid ClienteDados dados) {}

    public record RegistroAgente(
            @NotBlank @Size(min = 3) String username,
            @NotBlank @Size(min = 6, message = "Senha deve ter ao menos 6 caracteres") String senha,
            @NotNull Papel tipo,
            @NotBlank String nome,
            @NotBlank @Pattern(regexp = "\\d{14}", message = "CNPJ deve ter 14 dígitos") String cnpj) {}

    public record ClienteResponse(Long id, String username, String nome, String rg, String cpf,
                                  String endereco, String profissao, List<RendimentoDto> rendimentos) {}

    public record AutomovelRequest(
            @NotBlank String matricula,
            @NotNull @Min(1980) @Max(2100) Integer ano,
            @NotBlank String marca,
            @NotBlank String modelo,
            @NotBlank String placa) {}

    public record AutomovelResponse(Long id, String matricula, Integer ano, String marca, String modelo,
                                    String placa, TipoProprietario tipoProprietario, Long proprietarioId) {}

    public record PedidoRequest(
            @NotNull Long automovelId,
            @NotNull LocalDate dataInicio,
            @NotNull LocalDate dataFim,
            @NotNull @Positive BigDecimal valorMensal,
            boolean solicitaCredito) {}

    public record AvaliacaoRequest(boolean positivo, @NotBlank String parecer) {}

    public record CreditoRequest(
            @NotNull @Positive BigDecimal valor,
            @NotNull @PositiveOrZero BigDecimal taxaJurosMensal,
            @NotNull @Min(1) Integer parcelas) {}

    public record ContratoRequest(@NotNull TipoProprietario tipo, @Valid CreditoRequest credito) {}

    public record CreditoResponse(String banco, BigDecimal valor, BigDecimal taxaJurosMensal, Integer parcelas) {}

    public record ContratoResponse(Long id, TipoProprietario tipo, LocalDate dataAssinatura,
                                   String agente, CreditoResponse credito) {}

    public record PedidoResponse(Long id, Long clienteId, String clienteNome, AutomovelResponse automovel,
                                 LocalDate dataInicio, LocalDate dataFim, BigDecimal valorMensal,
                                 boolean solicitaCredito, StatusPedido status, String parecer,
                                 String agenteAvaliador, Instant criadoEm, ContratoResponse contrato) {}
}
