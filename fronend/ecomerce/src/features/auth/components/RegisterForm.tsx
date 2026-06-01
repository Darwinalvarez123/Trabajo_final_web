import { useState } from "react";
import { useRegister } from "../hooks/useRegister";
import { useLogin } from "../hooks/useLogin";
import { useCustomerCreate } from "../../customer/hooks/useCustomerCreate";
import { useAddressCreate } from "../../address/hooks/useAddressCreate";
import styles from "../styles/AuthForm.module.css";
import {useNavigate} from "react-router-dom";

export default function RegisterForm() {
    const { submit: registerUser, loading: regLoading, error: regError } = useRegister();
    const { submit: submitLogin, loading: loginLoading } = useLogin();
    const { create: createCustomer, loading: custLoading, error: custError } = useCustomerCreate();
    const { create: createAddress, loading: addrLoading, error: addrError } = useAddressCreate();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        email: "", password: "", firstName: "", lastName: "",
        street: "", city: "", state: "", postalCode: "", country: ""
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const authRes = await registerUser(formData.email, formData.password);
        if (!authRes) {
            alert(regError || "Error en el registro de usuario.");
            return;
        }

        await new Promise((resolve) => setTimeout(resolve, 1000));

        const custRes = await createCustomer({
            firstName: formData.firstName,
            lastName: formData.lastName,
            email: formData.email
        });

        if (!custRes) {
            alert(custError || "Error al crear el perfil del cliente.");
            return;
        }

        const addrRes = await createAddress({
            street: formData.street,
            city: formData.city,
            state: formData.state,
            postalCode: formData.postalCode,
            country: formData.country,
            customerId: custRes.id
        });

        if (addrRes) {
            const loginSuccess = await submitLogin(formData.email, formData.password);
            if (loginSuccess) {
                alert("¡Registro exitoso! Bienvenido.");
                navigate("/user");
            } else {
                alert("Registro exitoso. Por favor inicia sesión.");
                window.location.href = "/login";
            }
        } else {
            alert(addrError || "Error al crear la dirección.");
        }
    };

    return (
        <div className={styles.container}>
            <form onSubmit={handleSubmit} className={styles.formCard}>
                <h2 className={styles.title}>Registro</h2>
                <div className={styles.formGrid}>
                    <h3 className={styles.sectionTitle}>Datos Personales</h3>
                    <div className={styles.formGroup}>
                        <label htmlFor="firstName">Nombre</label>
                        <input id="firstName" name="firstName" required onChange={handleChange} value={formData.firstName} className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="lastName">Apellido</label>
                        <input id="lastName" name="lastName" required onChange={handleChange} value={formData.lastName} className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="email">Correo</label>
                        <input id="email" name="email" type="email" required onChange={handleChange} value={formData.email} className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="password">Contraseña</label>
                        <input id="password" name="password" type="password" required onChange={handleChange} value={formData.password} className={styles.input} />
                    </div>

                    <h3 className={styles.sectionTitle}>Dirección</h3>
                    <div className={styles.formGroup}>
                        <label htmlFor="street">Calle</label>
                        <input id="street" name="street" required onChange={handleChange} value={formData.street} className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="city">Ciudad</label>
                        <input id="city" name="city" required onChange={handleChange} value={formData.city} className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="state">Estado</label>
                        <input id="state" name="state" required onChange={handleChange} value={formData.state} className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="postalCode">Código Postal</label>
                        <input id="postalCode" name="postalCode" required onChange={handleChange} value={formData.postalCode} className={styles.input} />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="country">País</label>
                        <input id="country" name="country" required onChange={handleChange} value={formData.country} className={styles.input} />
                    </div>

                    <button type="submit" disabled={regLoading || custLoading || addrLoading || loginLoading} className={styles.button}>
                        {regLoading || custLoading || addrLoading || loginLoading ? "Procesando..." : "Registrar"}
                    </button>
                </div>
            </form>
        </div>
    );
}