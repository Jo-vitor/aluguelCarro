import { Campo } from './Ui.jsx';

export const clienteVazio = {
  nome: '', rg: '', cpf: '', endereco: '', profissao: '',
  rendimentos: [{ entidadeEmpregadora: '', valor: '' }],
};

export function paraApi(c) {
  return {
    ...c,
    cpf: c.cpf.replace(/\D/g, ''),
    rendimentos: c.rendimentos
      .filter((r) => r.entidadeEmpregadora.trim() !== '')
      .map((r) => ({ entidadeEmpregadora: r.entidadeEmpregadora, valor: Number(r.valor || 0) })),
  };
}

export default function ClienteCampos({ valor, onChange }) {
  const set = (k) => (e) => onChange({ ...valor, [k]: e.target.value });
  const setRend = (i, k) => (e) =>
    onChange({ ...valor, rendimentos: valor.rendimentos.map((r, j) => (j === i ? { ...r, [k]: e.target.value } : r)) });
  const add = () => onChange({ ...valor, rendimentos: [...valor.rendimentos, { entidadeEmpregadora: '', valor: '' }] });
  const del = (i) => onChange({ ...valor, rendimentos: valor.rendimentos.filter((_, j) => j !== i) });

  return (
    <>
      <Campo rotulo="Nome completo"><input required value={valor.nome} onChange={set('nome')} /></Campo>
      <div className="duas">
        <Campo rotulo="RG"><input required value={valor.rg} onChange={set('rg')} /></Campo>
        <Campo rotulo="CPF"><input required inputMode="numeric" placeholder="somente números" value={valor.cpf} onChange={set('cpf')} /></Campo>
      </div>
      <Campo rotulo="Endereço"><input required value={valor.endereco} onChange={set('endereco')} /></Campo>
      <Campo rotulo="Profissão"><input required value={valor.profissao} onChange={set('profissao')} /></Campo>

      <fieldset className="rendimentos">
        <legend>Rendimentos (até 3)</legend>
        {valor.rendimentos.map((r, i) => (
          <div className="linha-rend" key={i}>
            <input placeholder="Entidade empregadora" value={r.entidadeEmpregadora} onChange={setRend(i, 'entidadeEmpregadora')} />
            <input type="number" min="0" step="0.01" placeholder="Rendimento (R$)" value={r.valor} onChange={setRend(i, 'valor')} />
            <button type="button" className="btn-link" onClick={() => del(i)}>Remover</button>
          </div>
        ))}
        {valor.rendimentos.length < 3 && <button type="button" className="btn-sec" onClick={add}>Adicionar rendimento</button>}
      </fieldset>
    </>
  );
}
