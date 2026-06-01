import LoginForm from "../../features/auth/components/LoginForm";
import styles from './Login.module.css';

export default function Login()
{
    return(
        <div className={styles.loginContainer}>
            <h1 className={styles.title}>
                Iniciar sesión
            </h1>
            <LoginForm/>
        </div>
    );
}