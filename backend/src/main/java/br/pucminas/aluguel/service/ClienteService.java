package br.pucminas.aluguel.service;

import br.pucminas.aluguel.dto.Dtos.*;
import br.pucminas.aluguel.exception.ApiException;
import br.pucminas.aluguel.model.*;
import br.pucminas.aluguel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class ClienteService {
    private final ClienteRepository clientes;
    private final UsuarioRepository usuarios;
    private final PedidoAluguelRepository pedidos;

    public Cliente criar(Usuario usuario, ClienteDados d) {
        if (clientes.existsByCpf(d.cpf())) throw new ApiException(HttpStatus.CONFLICT, "CPF já cadastrado");
        Cliente c = new Cliente();
        c.setUsuario(usuario);
        preencher(c, d);
        return clientes.save(c);
    }

    @Transactional(readOnly = true)
    public ClienteResponse meuPerfil(Long usuarioId) { return Mapeador.cliente(porUsuario(usuarioId)); }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() { return clientes.findAll().stream().map(Mapeador::cliente).toList(); }

    @Transactional(readOnly = true)
    public ClienteResponse buscar(Long id) {
        return Mapeador.cliente(clientes.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Cliente não encontrado")));
    }

    public ClienteResponse atualizar(Long usuarioId, ClienteDados d) {
        Cliente c = porUsuario(usuarioId);
        if (!c.getCpf().equals(d.cpf()) && clientes.existsByCpf(d.cpf()))
            throw new ApiException(HttpStatus.CONFLICT, "CPF já cadastrado");
        preencher(c, d);
        return Mapeador.cliente(c);
    }

    public void excluir(Long usuarioId) {
        Cliente c = porUsuario(usuarioId);
        pedidos.deleteAll(pedidos.findByClienteIdOrderByCriadoEmDesc(c.getId()));
        clientes.delete(c);
        usuarios.delete(c.getUsuario());
    }

    private Cliente porUsuario(Long usuarioId) {
        return clientes.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    private void preencher(Cliente c, ClienteDados d) {
        c.setNome(d.nome()); c.setRg(d.rg()); c.setCpf(d.cpf());
        c.setEndereco(d.endereco()); c.setProfissao(d.profissao());
        c.getRendimentos().clear();
        if (d.rendimentos() != null)
            d.rendimentos().forEach(r -> c.getRendimentos().add(new Rendimento(r.entidadeEmpregadora(), r.valor())));
    }
}
