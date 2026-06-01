import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useLogin } from "../hooks/useLogin";
import * as React from "react";
import styles from "../styles/AuthForm.module.css"; // Importar los estilos

export default function LoginForm() {
    const { submit, loading, error } = useLogin();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();

    async function handleSubmit(e: React.SubmitEvent<HTMLFormElement>) {
        e.preventDefault();

        const response = await submit(email, password);

        if (!response) return;

        if (response.roles.includes("ROLE_ADMIN")) {
            navigate("/admin");
        } else {
            navigate("/user");
        }
    }

    return (
        <div className={styles.container}> {/* Contenedor principal */}
            <form onSubmit={handleSubmit} noValidate className={styles.formCard}> {/* Aplicar estilo al formulario */}
                <h2 className={styles.title}>Iniciar Sesión</h2> {/* Título */}
                <div className={styles.formGroup}> {/* Grupo para email */}
                    <label htmlFor="email">Correo Electrónico</label>
                    <input
                        id="email"
                        type="email"
                        placeholder="correo"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className={styles.input} // Aplicar estilo al input
                    />
                </div>

                <div className={styles.formGroup}> {/* Grupo para contraseña */}
                    <label htmlFor="password">Contraseña</label>
                    <input
                        id="password"
                        type="password"
                        placeholder="contraseña"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className={styles.input} // Aplicar estilo al input
                    />
                </div>

                {error && <p className={styles.error}>{error}</p>} {/* Aplicar estilo al error */}
                {loading && <p>Cargando...</p>}

                <button type="submit" disabled={loading} className={styles.button}> {/* Aplicar estilo al botón */}
                    iniciar sesión
                </button>
            </form>
        </div>
    );
}
