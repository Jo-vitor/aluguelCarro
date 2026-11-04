import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../auth.jsx';
import { brl, data } from '../format.js';
import { Erro, StatusBadge } from '../components/Ui.jsx';

export default function Pedidos() {
  const { chamar, ehAgente } = useAuth();
  const [lista, setLista] = useState(null);
  const [erro, setErro] = useState(null);

  useEffect(() => {
    chamar('/api/pedidos').then(setLista).catch(setErro);
  }, [chamar]);

  return (
    <>
      <div className="cabeca">
        <h2>{ehAgente ? 'Pedidos para análise' : 'Meus pedidos de aluguel'}</h2>
        {!ehAgente && <Link className="btn" to="/pedidos/novo">Novo pedido</Link>}
      </div>
      <Erro erro={erro} />
      {lista === null && !erro && <p className="vazio">Carregando pedidos…</p>}
      {lista?.length === 0 && (
        <p className="vazio">
          {ehAgente ? 'Nenhum pedido foi enviado ainda.' : 'Você ainda não tem pedidos. Crie o primeiro escolhendo um automóvel.'}
        </p>
      )}
      {lista?.length > 0 && (
        <div className="tabela-wrap">
          <table>
            <thead>
              <tr>
                <th>Pedido</th>{ehAgente && <th>Cliente</th>}<th>Automóvel</th><th>Período</th><th>Valor mensal</th><th>Status</th>
              </tr>
            </thead>
            <tbody>
              {lista.map((p) => (
                <tr key={p.id}>
                  <td><Link to={`/pedidos/${p.id}`}>#{p.id}</Link></td>
                  {ehAgente && <td>{p.clienteNome}</td>}
                  <td>{p.automovel.marca} {p.automovel.modelo} <small>{p.automovel.placa}</small></td>
                  <td>{data(p.dataInicio)} a {data(p.dataFim)}</td>
                  <td>{brl(p.valorMensal)}</td>
                  <td><StatusBadge status={p.status} /></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
}
