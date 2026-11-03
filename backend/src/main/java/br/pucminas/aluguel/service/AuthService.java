package br.pucminas.aluguel.service;

import br.pucminas.aluguel.dto.Dtos.*;
import br.pucminas.aluguel.exception.ApiException;
import br.pucminas.aluguel.model.*;
import br.pucminas.aluguel.repository.*;
import br.pucminas.aluguel.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional
public class AuthService {
    private final UsuarioRepository usuarios;
    private final ClienteRepository clientes;
    private final AgenteRepository agentes;
    private final ClienteService clienteService;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthResponse registrarCliente(RegistroCliente req) {
        Usuario u = novoUsuario(req.username(), req.senha(), Papel.CLIENTE);
        Cliente c = clienteService.criar(u, req.dados());
        return new AuthResponse(jwt.gerar(u), u.getUsername(), u.getPapel(), c.getNome());
    }

    public AuthResponse registrarAgente(RegistroAgente req) {
        if (req.tipo() == Papel.CLIENTE)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Agente deve ser EMPRESA ou BANCO");
        if (agentes.existsByCnpj(req.cnpj())) throw new ApiException(HttpStatus.CONFLICT, "CNPJ já cadastrado");
        Usuario u = novoUsuario(req.username(), req.senha(), req.tipo());
        Agente a = new Agente();
        a.setUsuario(u); a.setNome(req.nome()); a.setCnpj(req.cnpj());
        agentes.save(a);
        return new AuthResponse(jwt.gerar(u), u.getUsername(), u.getPapel(), a.getNome());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        Usuario u = usuarios.findByUsername(req.username())
                .filter(x -> encoder.matches(req.senha(), x.getSenhaHash()))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos"));
        String nome = u.getPapel() == Papel.CLIENTE
                ? clientes.findByUsuarioId(u.getId()).map(Cliente::getNome).orElse(u.getUsername())
                : agentes.findByUsuarioId(u.getId()).map(Agente::getNome).orElse(u.getUsername());
        return new AuthResponse(jwt.gerar(u), u.getUsername(), u.getPapel(), nome);
    }

    private Usuario novoUsuario(String username, String senha, Papel papel) {
        if (usuarios.findByUsername(username).isPresent())
            throw new ApiException(HttpStatus.CONFLICT, "Nome de usuário já existe");
        Usuario u = new Usuario();
        u.setUsername(username); u.setSenhaHash(encoder.encode(senha)); u.setPapel(papel);
        return usuarios.save(u);
    }
}
