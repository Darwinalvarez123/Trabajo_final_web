import { Outlet, NavLink } from "react-router-dom";
import styles from './AdminLayout.module.css';
import { useAuth } from "../../features/auth/hooks/useAuth.ts";

export default function AdminLayout() {
    const { logout } = useAuth();

    return (
        <div className={styles.adminContainer}>
            <aside className={styles.sidebar}>
                <nav>
                    <ul>
                        <li>
                            <NavLink to="/admin" end>Dashboard</NavLink>
                        </li>
                        <li>
                            <NavLink to={"/admin/categories"} end>Categorias</NavLink>
                        </li>
                        <li>
                            <NavLink to="/admin/products">Productos</NavLink>
                        </li>
                        <li>
                            <NavLink to="/admin/customer">Clientes</NavLink>
                        </li>
                        <li>
                            <NavLink to="/admin/orders">Órdenes</NavLink>
                        </li>
                        <li>
                            <button onClick={logout} className={styles.logoutButton}>Cerrar Sesión</button>
                        </li>
                    </ul>
                </nav>
            </aside>

            <main className={styles.content}>
                <Outlet />
            </main>
        </div>
    );
}