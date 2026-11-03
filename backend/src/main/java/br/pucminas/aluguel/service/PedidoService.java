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
public class PedidoService {
    private final PedidoAluguelRepository pedidos;
    private final ClienteRepository clientes;
    private final AgenteRepository agentes;
    private final AutomovelRepository automoveis;

    public PedidoResponse criar(CurrentUser u, PedidoRequest r) {
        PedidoAluguel p = new PedidoAluguel();
        p.setCliente(clienteDe(u));
        aplicar(p, r);
        return Mapeador.pedido(pedidos.save(p));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar(CurrentUser u) {
        List<PedidoAluguel> lista = u.papel().isAgente()
                ? pedidos.findAllByOrderByCriadoEmDesc()
                : pedidos.findByClienteIdOrderByCriadoEmDesc(clienteDe(u).getId());
        return lista.stream().map(Mapeador::pedido).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscar(CurrentUser u, Long id) { return Mapeador.pedido(carregar(u, id)); }

    public PedidoResponse modificar(CurrentUser u, Long id, PedidoRequest r) {
        PedidoAluguel p = carregar(u, id);
        if (p.getStatus() == StatusPedido.CONTRATO_EXECUTADO || p.getStatus() == StatusPedido.CANCELADO)
            throw new ApiException(HttpStatus.CONFLICT, "Pedido encerrado não pode ser modificado");
        if (!u.papel().isAgente() && p.getStatus() != StatusPedido.EM_ANALISE)
            throw new ApiException(HttpStatus.CONFLICT, "O cliente só pode modificar pedidos em análise");
        aplicar(p, r);
        return Mapeador.pedido(p);
    }

    public PedidoResponse cancelar(CurrentUser u, Long id) {
        PedidoAluguel p = carregar(u, id);
        if (p.getStatus() == StatusPedido.CONTRATO_EXECUTADO || p.getStatus() == StatusPedido.CANCELADO)
            throw new ApiException(HttpStatus.CONFLICT, "Este pedido não pode mais ser cancelado");
        p.setStatus(StatusPedido.CANCELADO);
        return Mapeador.pedido(p);
    }

    /** Análise financeira feita por um agente. */
    public PedidoResponse avaliar(CurrentUser u, Long id, AvaliacaoRequest r) {
        Agente agente = agenteDe(u);
        PedidoAluguel p = carregar(u, id);
        if (p.getStatus() != StatusPedido.EM_ANALISE)
            throw new ApiException(HttpStatus.CONFLICT, "Somente pedidos em análise podem ser avaliados");
        p.setStatus(r.positivo() ? StatusPedido.PARECER_POSITIVO : StatusPedido.PARECER_NEGATIVO);
        p.setParecer(r.parecer());
        p.setAgenteAvaliador(agente);
        return Mapeador.pedido(p);
    }

    public PedidoResponse executarContrato(CurrentUser u, Long id, ContratoRequest r) {
        Agente agente = agenteDe(u);
        PedidoAluguel p = carregar(u, id);
        if (p.getStatus() != StatusPedido.PARECER_POSITIVO)
            throw new ApiException(HttpStatus.CONFLICT, "Só é possível executar contrato com parecer positivo");
        if (r.tipo() != TipoProprietario.CLIENTE && !r.tipo().name().equals(agente.getUsuario().getPapel().name()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Tipo de contrato incompatível com o agente");

        Contrato ct = new Contrato();
        ct.setPedido(p); ct.setAgente(agente); ct.setTipo(r.tipo());

        if (r.credito() != null) {
            if (!p.isSolicitaCredito())
                throw new ApiException(HttpStatus.BAD_REQUEST, "O pedido não solicitou crédito");
            if (agente.getUsuario().getPapel() != Papel.BANCO)
                throw new ApiException(HttpStatus.FORBIDDEN, "Somente bancos concedem crédito");
            ContratoCredito cc = new ContratoCredito();
            cc.setBanco(agente); cc.setValor(r.credito().valor());
            cc.setTaxaJurosMensal(r.credito().taxaJurosMensal()); cc.setParcelas(r.credito().parcelas());
            ct.setCredito(cc);
        }

        Automovel a = p.getAutomovel();
        a.setTipoProprietario(r.tipo());
        a.setProprietarioId(r.tipo() == TipoProprietario.CLIENTE ? p.getCliente().getId() : agente.getId());

        p.setContrato(ct);
        p.setStatus(StatusPedido.CONTRATO_EXECUTADO);
        return Mapeador.pedido(p);
    }

    private void aplicar(PedidoAluguel p, PedidoRequest r) {
        if (!r.dataFim().isAfter(r.dataInicio()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "A data final deve ser posterior à data inicial");
        p.setAutomovel(automoveis.findById(r.automovelId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Automóvel não encontrado")));
        p.setDataInicio(r.dataInicio()); p.setDataFim(r.dataFim());
        p.setValorMensal(r.valorMensal()); p.setSolicitaCredito(r.solicitaCredito());
    }

    private PedidoAluguel carregar(CurrentUser u, Long id) {
        PedidoAluguel p = pedidos.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        if (!u.papel().isAgente() && !p.getCliente().getUsuario().getId().equals(u.id()))
            throw new ApiException(HttpStatus.FORBIDDEN, "Este pedido pertence a outro cliente");
        return p;
    }

    private Cliente clienteDe(CurrentUser u) {
        return clientes.findByUsuarioId(u.id())
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN, "Apenas clientes possuem pedidos próprios"));
    }

    private Agente agenteDe(CurrentUser u) {
        return agentes.findByUsuarioId(u.id())
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN, "Apenas agentes executam esta ação"));
    }
}
