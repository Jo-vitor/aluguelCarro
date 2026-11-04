import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../api.js';
import { useAuth } from '../auth.jsx';
import { Campo, Erro } from '../components/Ui.jsx';

export default function Login() {
  const { entrar } = useAuth();
  const nav = useNavigate();
  const [f, setF] = useState({ username: '', senha: '' });
  const [erro, setErro] = useState(null);
  const [enviando, setEnviando] = useState(false);

  async function enviar(e) {
    e.preventDefault();
    setErro(null);
    setEnviando(true);
    try {
      entrar(await api('/api/auth/login', { method: 'POST', body: f }));
      nav('/');
    } catch (err) {
      setErro(err);
    } finally {
      setEnviando(false);
    }
  }

  return (
    <div className="entrada">
      <h1>Aluguel de Carros</h1>
      <p className="sub">Entre para criar pedidos de aluguel e acompanhar a análise.</p>
      <form className="cartao" onSubmit={enviar}>
        <Campo rotulo="Usuário"><input required autoFocus value={f.username} onChange={(e) => setF({ ...f, username: e.target.value })} /></Campo>
        <Campo rotulo="Senha"><input required type="password" value={f.senha} onChange={(e) => setF({ ...f, senha: e.target.value })} /></Campo>
        <Erro erro={erro} />
        <button className="btn" disabled={enviando}>{enviando ? 'Entrando…' : 'Entrar'}</button>
      </form>
      <p className="rodape-form">Ainda não tem conta? <Link to="/registro">Cadastre-se</Link></p>
    </div>
  );
}
