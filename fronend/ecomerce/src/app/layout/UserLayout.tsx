import { Outlet } from "react-router-dom";
import styles from './UserLayout.module.css';
import { useAuth } from "../../features/auth/hooks/useAuth.ts";

export default function UserLayout() {
    const { logout } = useAuth();

    return (
        <div className={styles.userContainer}>
            <header className={styles.header}>
                <nav>
                    <button onClick={logout} className={styles.logoutButton}>Cerrar Sesión</button>
                </nav>
            </header>
            <main className={styles.content}>
                <Outlet />
            </main>
        </div>
    );
}
