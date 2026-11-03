package br.pucminas.aluguel.service;

import br.pucminas.aluguel.dto.Dtos.*;
import br.pucminas.aluguel.model.*;

public final class Mapeador {
    private Mapeador() {}

    public static ClienteResponse cliente(Cliente c) {
        return new ClienteResponse(c.getId(), c.getUsuario().getUsername(), c.getNome(), c.getRg(), c.getCpf(),
                c.getEndereco(), c.getProfissao(),
                c.getRendimentos().stream().map(r -> new RendimentoDto(r.getEntidadeEmpregadora(), r.getValor())).toList());
    }

    public static AutomovelResponse automovel(Automovel a) {
        return new AutomovelResponse(a.getId(), a.getMatricula(), a.getAno(), a.getMarca(), a.getModelo(),
                a.getPlaca(), a.getTipoProprietario(), a.getProprietarioId());
    }

    public static PedidoResponse pedido(PedidoAluguel p) {
        Contrato ct = p.getContrato();
        ContratoResponse contrato = ct == null ? null : new ContratoResponse(ct.getId(), ct.getTipo(),
                ct.getDataAssinatura(), ct.getAgente().getNome(),
                ct.getCredito() == null ? null : new CreditoResponse(ct.getCredito().getBanco().getNome(),
                        ct.getCredito().getValor(), ct.getCredito().getTaxaJurosMensal(), ct.getCredito().getParcelas()));
        return new PedidoResponse(p.getId(), p.getCliente().getId(), p.getCliente().getNome(),
                automovel(p.getAutomovel()), p.getDataInicio(), p.getDataFim(), p.getValorMensal(),
                p.isSolicitaCredito(), p.getStatus(), p.getParecer(),
                p.getAgenteAvaliador() == null ? null : p.getAgenteAvaliador().getNome(),
                p.getCriadoEm(), contrato);
    }
}
