import { STATUS } from '../format.js';

export function Erro({ erro }) {
  if (!erro) return null;
  const campos = erro.campos ? Object.entries(erro.campos) : [];
  return (
    <div className="erro" role="alert">
      <strong>{erro.message}</strong>
      {campos.length > 0 && <ul>{campos.map(([c, m]) => <li key={c}>{c}: {m}</li>)}</ul>}
    </div>
  );
}

export function StatusBadge({ status }) {
  return <span className={`badge ${status}`}>{STATUS[status]}</span>;
}

export function Campo({ rotulo, children }) {
  return <label className="campo"><span>{rotulo}</span>{children}</label>;
}
