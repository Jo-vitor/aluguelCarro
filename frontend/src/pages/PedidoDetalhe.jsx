import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useParams } from 'react-router-dom';
import { useAuth } from '../auth.jsx';
import { brl, data, PAPEL } from '../format.js';
import { Campo, Erro, StatusBadge } from '../components/Ui.jsx';

function Trilha({ status }) {
  const etapas = ['Pedido enviado', 'Análise financeira', 'Contrato'];
  const atual = { EM_ANALISE: 1, PARECER_POSITIVO: 2, PARECER_NEGATIVO: 1, CONTRATO_EXECUTADO: 3, CANCELADO: -1 }[status];
  if (status === 'CANCELADO') return <p className="aviso">Este pedido foi cancelado.</p>;
  return (
    <ol className="trilha">
      {etapas.map((e, i) => {
        const estado = i < atual ? 'feita' : i === atual ? (status === 'PARECER_NEGATIVO' ? 'falhou' : 'atual') : '';
        return <li key={e} className={estado}><span>{e}</span></li>;
      })}
    </ol>
  );
}

export default function PedidoDetalhe() {
  const { id } = useParams();
  const { chamar, user, ehAgente } = useAuth();
  const [p, setP] = useState(null);
  const [erro, setErro] = useState(null);
  const [parecer, setParecer] = useState('');
  const [tipo, setTipo] = useState('CLIENTE');
  const [usaCredito, setUsaCredito] = useState(false);
  const [cred, setCred] = useState({ valor: '', taxaJurosMensal: '', parcelas: '' });

  const carregar = useCallback(() => chamar(`/api/pedidos/${id}`).then(setP).catch(setErro), [chamar, id]);
  useEffect(() => { carregar(); }, [carregar]);

  async function acao(path, body) {
    setErro(null);
    try {
      setP(await chamar(`/api/pedidos/${id}/${path}`, { method: 'POST', body }));
    } catch (err) {
      setErro(err);
    }
  }

  if (!p) return erro ? <Erro erro={erro} /> : <p className="vazio">Carregando pedido…</p>;

  const encerrado = p.status === 'CANCELADO' || p.status === 'CONTRATO_EXECUTADO';
  const podeEditar = ehAgente ? !encerrado : p.status === 'EM_ANALISE';

  return (
    <>
      <div className="cabeca">
        <h2>Pedido #{p.id} <StatusBadge status={p.status} /></h2>
        <Link className="btn-sec" to="/">Voltar à lista</Link>
      </div>
      <Trilha status={p.status} />
      <Erro erro={erro} />

      <section className="cartao dados">
        <dl>
          <dt>Cliente</dt><dd>{p.clienteNome}</dd>
          <dt>Automóvel</dt><dd>{p.automovel.marca} {p.automovel.modelo} {p.automovel.ano} — placa {p.automovel.placa}</dd>
          <dt>Período</dt><dd>{data(p.dataInicio)} a {data(p.dataFim)}</dd>
          <dt>Valor mensal</dt><dd>{brl(p.valorMensal)}</dd>
          <dt>Crédito</dt><dd>{p.solicitaCredito ? 'Solicitado' : 'Não solicitado'}</dd>
          {p.parecer && <><dt>Parecer de {p.agenteAvaliador}</dt><dd>{p.parecer}</dd></>}
        </dl>
      </section>

      {p.contrato && (
        <section className="cartao dados">
          <h3>Contrato</h3>
          <dl>
            <dt>Assinado em</dt><dd>{data(p.contrato.dataAssinatura)} por {p.contrato.agente}</dd>
            <dt>Propriedade do automóvel</dt><dd>{PAPEL[p.contrato.tipo]}</dd>
            {p.contrato.credito && (
              <>
                <dt>Crédito</dt>
                <dd>{brl(p.contrato.credito.valor)} em {p.contrato.credito.parcelas}x, juros de {p.contrato.credito.taxaJurosMensal}% a.m. ({p.contrato.credito.banco})</dd>
              </>
            )}
          </dl>
        </section>
      )}

      <div className="acoes">
        {podeEditar && <Link className="btn-sec" to={`/pedidos/${p.id}/editar`}>Modificar pedido</Link>}
        {!ehAgente && !encerrado && (
          <button className="btn-perigo" onClick={() => window.confirm('Cancelar este pedido?') && acao('cancelar')}>Cancelar pedido</button>
        )}
      </div>

      {ehAgente && p.status === 'EM_ANALISE' && (
        <section className="cartao">
          <h3>Análise financeira</h3>
          <Campo rotulo="Parecer"><textarea rows="3" value={parecer} onChange={(e) => setParecer(e.target.value)} /></Campo>
          <div className="acoes">
            <button className="btn" disabled={!parecer.trim()} onClick={() => acao('avaliar', { positivo: true, parecer })}>Parecer positivo</button>
            <button className="btn-perigo" disabled={!parecer.trim()} onClick={() => acao('avaliar', { positivo: false, parecer })}>Parecer negativo</button>
          </div>
        </section>
      )}

      {ehAgente && p.status === 'PARECER_POSITIVO' && (
        <section className="cartao">
          <h3>Executar contrato</h3>
          <Campo rotulo="Propriedade do automóvel">
            <select value={tipo} onChange={(e) => setTipo(e.target.value)}>
              <option value="CLIENTE">Do cliente</option>
              <option value={user.papel}>{user.papel === 'BANCO' ? 'Do banco' : 'Da empresa'}</option>
            </select>
          </Campo>
          {p.solicitaCredito && user.papel === 'BANCO' && (
            <>
              <label className="check">
                <input type="checkbox" checked={usaCredito} onChange={(e) => setUsaCredito(e.target.checked)} />
                Conceder contrato de crédito
              </label>
              {usaCredito && (
                <div className="tres">
                  <Campo rotulo="Valor (R$)"><input type="number" min="0.01" step="0.01" value={cred.valor} onChange={(e) => setCred({ ...cred, valor: e.target.value })} /></Campo>
                  <Campo rotulo="Juros (% a.m.)"><input type="number" min="0" step="0.001" value={cred.taxaJurosMensal} onChange={(e) => setCred({ ...cred, taxaJurosMensal: e.target.value })} /></Campo>
                  <Campo rotulo="Parcelas"><input type="number" min="1" step="1" value={cred.parcelas} onChange={(e) => setCred({ ...cred, parcelas: e.target.value })} /></Campo>
                </div>
              )}
            </>
          )}
          {p.solicitaCredito && user.papel !== 'BANCO' && <p className="aviso">O cliente pediu crédito: somente um banco pode conceder.</p>}
          <button className="btn" onClick={() => acao('contrato', {
            tipo,
            credito: usaCredito ? { valor: Number(cred.valor), taxaJurosMensal: Number(cred.taxaJurosMensal), parcelas: Number(cred.parcelas) } : null,
          })}>Executar contrato</button>
        </section>
      )}
    </>
  );
}
