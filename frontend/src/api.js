const BASE = import.meta.env.VITE_API_URL ?? 'http://localhost:8080';

export class ApiError extends Error {
  constructor(mensagem, campos) {
    super(mensagem);
    this.campos = campos;
  }
}

export async function api(path, { method = 'GET', body, token } = {}) {
  const res = await fetch(BASE + path, {
    method,
    headers: {
      ...(body ? { 'Content-Type': 'application/json' } : {}),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  });
  if (res.status === 204) return null;
  let dados = null;
  try { dados = await res.json(); } catch {}
  if (!res.ok) {
    if (res.status === 401 && token) window.dispatchEvent(new Event('auth:expirada'));
    throw new ApiError(dados?.erro ?? 'Não foi possível concluir a operação.', dados?.campos);
  }
  return dados;
}
