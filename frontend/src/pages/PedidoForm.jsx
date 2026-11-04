import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../auth.jsx';
import { Campo, Erro } from '../components/Ui.jsx';

const vazio = { automovelId: '', dataInicio: '', dataFim: '', valorMensal: '', solicitaCredito: false };

export default function PedidoForm() {
  const { id } = useParams();
  const { chamar } = useAuth();
  const nav = useNavigate();
  const [f, setF] = useState(vazio);
  const [carros, setCarros] = useState([]);
  const [erro, setErro] = useState(null);
  const [enviando, setEnviando] = useState(false);

  useEffect(() => {
    chamar('/api/automoveis').then(setCarros).catch(setErro);
    if (id) {
      chamar(`/api/pedidos/${id}`).then((p) => setF({
        automovelId: String(p.automovel.id), dataInicio: p.dataInicio, dataFim: p.dataFim,
        valorMensal: String(p.valorMensal), solicitaCredito: p.solicitaCredito,
      })).catch(setErro);
    }
  }, [chamar, id]);

  async function enviar(e) {
    e.preventDefault();
    setErro(null);
    setEnviando(true);
    try {
      const body = { ...f, automovelId: Number(f.automovelId), valorMensal: Number(f.valorMensal) };
      const p = await chamar(id ? `/api/pedidos/${id}` : '/api/pedidos', { method: id ? 'PUT' : 'POST', body });
      nav(`/pedidos/${p.id}`);
    } catch (err) {
      setErro(err);
    } finally {
      setEnviando(false);
    }
  }

  return (
    <div className="estreita">
      <h2>{id ? `Modificar pedido #${id}` : 'Novo pedido de aluguel'}</h2>
      <form className="cartao" onSubmit={enviar}>
        <Campo rotulo="Automóvel">
          <select required value={f.automovelId} onChange={(e) => setF({ ...f, automovelId: e.target.value })}>
            <option value="">Selecione…</option>
            {carros.map((c) => <option key={c.id} value={c.id}>{c.marca} {c.modelo} {c.ano} — {c.placa}</option>)}
          </select>
        </Campo>
        <div className="duas">
          <Campo rotulo="Início"><input required type="date" value={f.dataInicio} onChange={(e) => setF({ ...f, dataInicio: e.target.value })} /></Campo>
          <Campo rotulo="Fim"><input required type="date" value={f.dataFim} onChange={(e) => setF({ ...f, dataFim: e.target.value })} /></Campo>
        </div>
        <Campo rotulo="Valor mensal (R$)"><input required type="number" min="0.01" step="0.01" value={f.valorMensal} onChange={(e) => setF({ ...f, valorMensal: e.target.value })} /></Campo>
        <label className="check">
          <input type="checkbox" checked={f.solicitaCredito} onChange={(e) => setF({ ...f, solicitaCredito: e.target.checked })} />
          Solicitar contrato de crédito a um banco
        </label>
        <Erro erro={erro} />
        <div className="acoes">
          <button className="btn" disabled={enviando}>{id ? 'Salvar alterações' : 'Enviar pedido'}</button>
          <Link className="btn-sec" to={id ? `/pedidos/${id}` : '/'}>Voltar</Link>
        </div>
      </form>
    </div>
  );
}
