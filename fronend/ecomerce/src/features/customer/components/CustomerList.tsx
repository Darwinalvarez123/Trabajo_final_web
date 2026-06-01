import {useGetCustomer} from "../hooks/useGetCustomer.ts";
import CustomerCard from "./CustomerCard.tsx";
import styles from "../styles/customerList.module.css"
export default function CustomerList()
{
    const {data,loading,error} = useGetCustomer();

    if(loading) return (<p>cargando</p>)
    if(error) return (<p>error: {error}</p>)
    return (
        <div className={styles.container}>
            <h2 className={styles.title}>Lista de Clientes</h2>

            <div className={styles.grid}>
                {data.map((customer) => (
                    <CustomerCard
                        key={customer.id}
                        customer={customer}
                    />
                ))}
            </div>
        </div>
    );
}