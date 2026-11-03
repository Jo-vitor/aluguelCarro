package br.pucminas.aluguel.service;

import br.pucminas.aluguel.dto.Dtos.*;
import br.pucminas.aluguel.exception.ApiException;
import br.pucminas.aluguel.model.*;
import br.pucminas.aluguel.repository.*;
import br.pucminas.aluguel.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class AutomovelService {
    private final AutomovelRepository automoveis;
    private final ClienteRepository clientes;
    private final AgenteRepository agentes;

    @Transactional(readOnly = true)
    public List<AutomovelResponse> listar() { return automoveis.findAll().stream().map(Mapeador::automovel).toList(); }

    @Transactional(readOnly = true)
    public AutomovelResponse buscar(Long id) {
        return Mapeador.automovel(automoveis.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Automóvel não encontrado")));
    }

    public AutomovelResponse criar(CurrentUser u, AutomovelRequest r) {
        if (automoveis.existsByPlaca(r.placa())) throw new ApiException(HttpStatus.CONFLICT, "Placa já cadastrada");
        if (automoveis.existsByMatricula(r.matricula()))
            throw new ApiException(HttpStatus.CONFLICT, "Matrícula já cadastrada");
        Automovel a = new Automovel();
        a.setMatricula(r.matricula()); a.setAno(r.ano()); a.setMarca(r.marca());
        a.setModelo(r.modelo()); a.setPlaca(r.placa());
        a.setTipoProprietario(TipoProprietario.valueOf(u.papel().name()));
        a.setProprietarioId(u.papel() == Papel.CLIENTE
                ? clientes.findByUsuarioId(u.id()).map(Cliente::getId).orElse(null)
                : agentes.findByUsuarioId(u.id()).map(Agente::getId).orElse(null));
        return Mapeador.automovel(automoveis.save(a));
    }
}
