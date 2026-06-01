import styles from './DashboardUser.module.css';
import UserCatalog from "../../features/user/components/UserCatalog.tsx";

export default function DashboardUser()
{
    return(
        <div className={styles.dashboardUserContainer}>
            <h1 className={styles.title}>Dashboard de Usuario</h1>
            <UserCatalog/>
        </div>
    );
}