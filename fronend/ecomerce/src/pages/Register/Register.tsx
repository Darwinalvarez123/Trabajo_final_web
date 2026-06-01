import RegisterForm from "../../features/auth/components/RegisterForm.tsx";
import styles from './Register.module.css';

export default function Register()
{
    return (
        <div className={styles.registerContainer}>
            <p className={styles.title}>Registrar</p>
            <RegisterForm/>
        </div>
    );
}
