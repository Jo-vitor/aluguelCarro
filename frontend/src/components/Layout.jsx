import { NavLink } from 'react-router-dom';
import { useAuth } from '../auth.jsx';
import { PAPEL } from '../format.js';

export default function Layout({ children }) {
  const { user, sair } = useAuth();
  return (
    <>
      <header className="topo">
        <div className="topo-in">
          <span className="marca">Aluguel de Carros</span>
          <nav>
            <NavLink to="/" end>Pedidos</NavLink>
            <NavLink to="/automoveis">Automóveis</NavLink>
            {user.papel === 'CLIENTE' && <NavLink to="/perfil">Meu cadastro</NavLink>}
          </nav>
          <div className="usuario">
            <span>{user.nome} <small>({PAPEL[user.papel]})</small></span>
            <button className="btn-link" onClick={sair}>Sair</button>
          </div>
        </div>
      </header>
      <main className="pagina">{children}</main>
    </>
  );
}
