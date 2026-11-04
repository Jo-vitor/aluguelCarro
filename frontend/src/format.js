export const brl = (v) => Number(v).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

export const data = (iso) => {
  if (!iso) return '';
  const [a, m, d] = iso.slice(0, 10).split('-');
  return `${d}/${m}/${a}`;
};

export const STATUS = {
  EM_ANALISE: 'Em análise',
  PARECER_POSITIVO: 'Aprovado na análise',
  PARECER_NEGATIVO: 'Reprovado na análise',
  CONTRATO_EXECUTADO: 'Contrato executado',
  CANCELADO: 'Cancelado',
};

export const PAPEL = { CLIENTE: 'Cliente', EMPRESA: 'Empresa', BANCO: 'Banco' };
