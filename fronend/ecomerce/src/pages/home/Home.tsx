import React from 'react';
import { Link } from 'react-router-dom';
import styles from './Home.module.css';

const Home: React.FC = () => {
  return (
    <div className={styles.homeContainer}>
      <h1>¡Bienvenido a nuestra Tienda Online!</h1>
      <p>Tu tienda de confianza para todo lo que necesitas.</p>
      <div className={styles.buttonContainer}>
        <Link to="/register" className={styles.button}>Registrarse</Link>
        <Link to="/login" className={styles.button}>Iniciar sesión</Link>
      </div>
    </div>
  );
};

export default Home;
