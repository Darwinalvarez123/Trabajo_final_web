import {useAdminUsers} from "../hooks/useAdminUsers";
import styles from '../styles/AdminUsers.module.css'; // Ruta corregida

export default function AdminUsers() {
    const {
        data,
        loading,
        roles,
        error,
        selectedRoles,
        setSelectedRoles,
        handleUpdateRole,
        handleToggle
    } = useAdminUsers();

    if (loading) return <p>Cargando...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div className={styles.adminUsersContainer}>
            <h2 className={styles.title}>Administrar usuarios</h2>

            <table className={styles.usersTable}>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Email</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {data?.map((user) => (
                        <tr key={user.id}>
                            <td data-label="ID">{user.id}</td>
                            <td data-label="Email">{user.email}</td>
                            <td data-label="Rol">
                                <select
                                    value={selectedRoles[user.id] || user.roles[0]}
                                    onChange={(e) =>
                                        setSelectedRoles({
                                            ...selectedRoles,
                                            [user.id]: e.target.value,
                                        })
                                    }
                                >
                                    {roles.map((role) => (
                                        <option key={role} value={role}>
                                            {role}
                                        </option>
                                    ))}
                                </select>
                            </td>
                            <td data-label="Estado">{user.enabled ? "Activo" : "Inactivo"}</td>
                            <td data-label="Acciones">
                                <button
                                    className={`${styles.actionButton} ${styles.editButton}`}
                                    onClick={() => handleUpdateRole(user.id)}
                                >
                                    Actualizar Rol
                                </button>
                                <button
                                    className={`${styles.actionButton} ${styles.deleteButton}`}
                                    onClick={() => handleToggle(user.id)}
                                >
                                    {user.enabled ? "Desactivar" : "Activar"}
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}