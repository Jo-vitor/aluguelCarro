import { useEffect, useState } from 'react';
import { useAuth } from '../auth.jsx';
import { Erro } from '../components/Ui.jsx';
import ClienteCampos, { clienteVazio, paraApi } from '../components/ClienteCampos.jsx';

export default function Perfil() {
  const { chamar, sair } = useAuth();
  const [c, setC] = useState(null);
  const [erro, setErro] = useState(null);
  const [ok, setOk] = useState(false);

  useEffect(() => {
    chamar('/api/clientes/me')
      .then((r) => setC({ ...clienteVazio, ...r, rendimentos: r.rendimentos.map((x) => ({ ...x, valor: String(x.valor) })) }))
      .catch(setErro);
  }, [chamar]);

  async function salvar(e) {
    e.preventDefault();
    setErro(null); setOk(false);
    try {
      await chamar('/api/clientes/me', { method: 'PUT', body: paraApi(c) });
      setOk(true);
    } catch (err) {
      setErro(err);
    }
  }

  async function excluir() {
    if (!window.confirm('Excluir sua conta e todos os seus pedidos? Esta ação não pode ser desfeita.')) return;
    try {
      await chamar('/api/clientes/me', { method: 'DELETE' });
      sair();
    } catch (err) {
      setErro(err);
    }
  }

  if (!c) return erro ? <Erro erro={erro} /> : <p className="vazio">Carregando cadastro…</p>;

  return (
    <div className="estreita">
      <h2>Meu cadastro</h2>
      <form className="cartao" onSubmit={salvar}>
        <ClienteCampos valor={c} onChange={(v) => { setC(v); setOk(false); }} />
        <Erro erro={erro} />
        {ok && <p className="sucesso">Cadastro atualizado.</p>}
        <div className="acoes">
          <button className="btn">Salvar alterações</button>
          <button type="button" className="btn-perigo" onClick={excluir}>Excluir conta</button>
        </div>
      </form>
    </div>
  );
}
