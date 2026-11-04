import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { api } from './api';

const AuthCtx = createContext(null);
const KEY = 'aluguel.sessao';

function ler() {
  try { return JSON.parse(localStorage.getItem(KEY)); } catch { return null; }
}

export function AuthProvider({ children }) {
  const [sessao, setSessao] = useState(ler);

  const entrar = useCallback((resp) => {
    const s = { token: resp.token, user: { username: resp.username, papel: resp.papel, nome: resp.nome } };
    try { localStorage.setItem(KEY, JSON.stringify(s)); } catch {}
    setSessao(s);
  }, []);

  const sair = useCallback(() => {
    try { localStorage.removeItem(KEY); } catch {}
    setSessao(null);
  }, []);

  useEffect(() => {
    window.addEventListener('auth:expirada', sair);
    return () => window.removeEventListener('auth:expirada', sair);
  }, [sair]);

  const chamar = useCallback((path, opts = {}) => api(path, { ...opts, token: sessao?.token }), [sessao]);

  const value = useMemo(() => ({
    user: sessao?.user ?? null,
    ehAgente: !!sessao && sessao.user.papel !== 'CLIENTE',
    entrar, sair, chamar,
  }), [sessao, entrar, sair, chamar]);

  return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>;
}

export const useAuth = () => useContext(AuthCtx);
