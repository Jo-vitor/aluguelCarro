import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../api.js';
import { useAuth } from '../auth.jsx';
import { Campo, Erro } from '../components/Ui.jsx';
import ClienteCampos, { clienteVazio, paraApi } from '../components/ClienteCampos.jsx';

export default function Registro() {
  const { entrar } = useAuth();
  const nav = useNavigate();
  const [tipo, setTipo] = useState('CLIENTE');
  const [cred, setCred] = useState({ username: '', senha: '' });
  const [cliente, setCliente] = useState(clienteVazio);
  const [agente, setAgente] = useState({ nome: '', cnpj: '' });
  const [erro, setErro] = useState(null);
  const [enviando, setEnviando] = useState(false);

  async function enviar(e) {
    e.preventDefault();
    setErro(null);
    setEnviando(true);
    try {
      const resp = tipo === 'CLIENTE'
        ? await api('/api/auth/registro/cliente', { method: 'POST', body: { ...cred, dados: paraApi(cliente) } })
        : await api('/api/auth/registro/agente', {
            method: 'POST',
            body: { ...cred, tipo, nome: agente.nome, cnpj: agente.cnpj.replace(/\D/g, '') },
          });
      entrar(resp);
      nav('/');
    } catch (err) {
      setErro(err);
    } finally {
      setEnviando(false);
    }
  }

  return (
    <div className="entrada larga">
      <h1>Criar conta</h1>
      <div className="abas" role="tablist">
        {[['CLIENTE', 'Cliente'], ['EMPRESA', 'Empresa'], ['BANCO', 'Banco']].map(([v, r]) => (
          <button key={v} type="button" role="tab" aria-selected={tipo === v} className={tipo === v ? 'ativa' : ''} onClick={() => setTipo(v)}>{r}</button>
        ))}
      </div>
      <form className="cartao" onSubmit={enviar}>
        <div className="duas">
          <Campo rotulo="Usuário"><input required minLength={3} value={cred.username} onChange={(e) => setCred({ ...cred, username: e.target.value })} /></Campo>
          <Campo rotulo="Senha (mín. 6 caracteres)"><input required minLength={6} type="password" value={cred.senha} onChange={(e) => setCred({ ...cred, senha: e.target.value })} /></Campo>
        </div>
        {tipo === 'CLIENTE' ? (
          <ClienteCampos valor={cliente} onChange={setCliente} />
        ) : (
          <>
            <Campo rotulo={tipo === 'BANCO' ? 'Nome do banco' : 'Nome da empresa'}><input required value={agente.nome} onChange={(e) => setAgente({ ...agente, nome: e.target.value })} /></Campo>
            <Campo rotulo="CNPJ"><input required inputMode="numeric" placeholder="somente números" value={agente.cnpj} onChange={(e) => setAgente({ ...agente, cnpj: e.target.value })} /></Campo>
          </>
        )}
        <Erro erro={erro} />
        <button className="btn" disabled={enviando}>{enviando ? 'Criando…' : 'Criar conta'}</button>
      </form>
      <p className="rodape-form">Já tem conta? <Link to="/login">Entrar</Link></p>
    </div>
  );
}
