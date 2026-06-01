import { useState, useMemo } from "react";
import { useParams } from "react-router-dom";
import { useAddressGetAll } from "../hooks/useAddressGetAll";
import styles from "../styles/AddressList.module.css";

export default function CustomerAddressList() {
    const { customerId } = useParams<{ customerId: string }>();
    const customerIdNum = customerId ? parseInt(customerId, 10) : 0;

    const { data: allAddresses, loading, error } = useAddressGetAll();

    // 1. Memoizar el filtrado para evitar recalcular en cada render
    const addresses = useMemo(() => {
        return allAddresses?.filter(addr => addr.customerId === customerIdNum) ?? [];
    }, [allAddresses, customerIdNum]);

    const [currentAddressIndex, setCurrentAddressIndex] = useState(0);



    if (loading) return <p>Cargando direcciones...</p>;
    if (error) return <p>Error al cargar direcciones: {error}</p>;
    if (addresses.length === 0) return <p>No hay direcciones registradas para este cliente.</p>;

    const currentAddress = addresses[currentAddressIndex];

    const handleViewNextAddress = () => {
        setCurrentAddressIndex((prev) => (prev + 1) % addresses.length);
    };

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>Dirección del Cliente</h2>
            <div className={styles.addressCard}>
                <p><strong>Calle:</strong> {currentAddress.street}</p>
                <p><strong>Ciudad:</strong> {currentAddress.city}</p>
                <p><strong>Estado:</strong> {currentAddress.state}</p>
                <p><strong>Código Postal:</strong> {currentAddress.postalCode}</p>
                <p><strong>País:</strong> {currentAddress.country}</p>
            </div>

            {addresses.length > 1 && (
                <button onClick={handleViewNextAddress} className={styles.viewOtherButton}>
                    Ver otra dirección ({currentAddressIndex + 1} de {addresses.length})
                </button>
            )}
        </div>
    );
}