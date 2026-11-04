import { Navigate, Outlet, Route, Routes } from 'react-router-dom';
import { useAuth } from './auth.jsx';
import Layout from './components/Layout.jsx';
import Login from './pages/Login.jsx';
import Registro from './pages/Registro.jsx';
import Pedidos from './pages/Pedidos.jsx';
import PedidoForm from './pages/PedidoForm.jsx';
import PedidoDetalhe from './pages/PedidoDetalhe.jsx';
import Perfil from './pages/Perfil.jsx';
import Automoveis from './pages/Automoveis.jsx';

function Protegida({ papel }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (papel === 'CLIENTE' && user.papel !== 'CLIENTE') return <Navigate to="/" replace />;
  return <Layout><Outlet /></Layout>;
}

export default function App() {
  const { user } = useAuth();
  return (
    <Routes>
      <Route path="/login" element={user ? <Navigate to="/" replace /> : <Login />} />
      <Route path="/registro" element={user ? <Navigate to="/" replace /> : <Registro />} />
      <Route element={<Protegida />}>
        <Route path="/" element={<Pedidos />} />
        <Route path="/pedidos/:id" element={<PedidoDetalhe />} />
        <Route path="/pedidos/:id/editar" element={<PedidoForm />} />
        <Route path="/automoveis" element={<Automoveis />} />
      </Route>
      <Route element={<Protegida papel="CLIENTE" />}>
        <Route path="/pedidos/novo" element={<PedidoForm />} />
        <Route path="/perfil" element={<Perfil />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
