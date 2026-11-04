import { useCallback, useEffect, useState } from 'react';
import { useAuth } from '../auth.jsx';
import { PAPEL } from '../format.js';
import { Campo, Erro } from '../components/Ui.jsx';

const vazio = { matricula: '', ano: '', marca: '', modelo: '', placa: '' };

export default function Automoveis() {
  const { chamar } = useAuth();
  const [lista, setLista] = useState([]);
  const [f, setF] = useState(vazio);
  const [erro, setErro] = useState(null);

  const carregar = useCallback(() => chamar('/api/automoveis').then(setLista).catch(setErro), [chamar]);
  useEffect(() => { carregar(); }, [carregar]);

  async function enviar(e) {
    e.preventDefault();
    setErro(null);
    try {
      await chamar('/api/automoveis', { method: 'POST', body: { ...f, ano: Number(f.ano), placa: f.placa.toUpperCase() } });
      setF(vazio);
      carregar();
    } catch (err) {
      setErro(err);
    }
  }

  const set = (k) => (e) => setF({ ...f, [k]: e.target.value });

  return (
    <>
      <h2>Automóveis</h2>
      <div className="tabela-wrap">
        <table>
          <thead><tr><th>Matrícula</th><th>Veículo</th><th>Ano</th><th>Placa</th><th>Propriedade</th></tr></thead>
          <tbody>
            {lista.map((a) => (
              <tr key={a.id}>
                <td>{a.matricula}</td><td>{a.marca} {a.modelo}</td><td>{a.ano}</td><td>{a.placa}</td><td>{PAPEL[a.tipoProprietario]}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <h3>Cadastrar automóvel</h3>
      <form className="cartao" onSubmit={enviar}>
        <div className="duas">
          <Campo rotulo="Matrícula"><input required value={f.matricula} onChange={set('matricula')} /></Campo>
          <Campo rotulo="Placa"><input required value={f.placa} onChange={set('placa')} /></Campo>
        </div>
        <div className="tres">
          <Campo rotulo="Marca"><input required value={f.marca} onChange={set('marca')} /></Campo>
          <Campo rotulo="Modelo"><input required value={f.modelo} onChange={set('modelo')} /></Campo>
          <Campo rotulo="Ano"><input required type="number" min="1980" value={f.ano} onChange={set('ano')} /></Campo>
        </div>
        <Erro erro={erro} />
        <button className="btn">Cadastrar automóvel</button>
      </form>
    </>
  );
}
