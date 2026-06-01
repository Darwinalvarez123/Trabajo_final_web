import type { CategoryResponse } from "../type/category";
import styles from "../styles/CategoryCard.module.css";

interface CategoryCardProps {
    category: CategoryResponse;
    onEdit: (categoryId: number) => void;
    onToggleStatus: (categoryId: number, currentActiveStatus: boolean, categoryName: string) => void;
}

export default function CategoryCard({ category, onEdit, onToggleStatus }: CategoryCardProps) {
    return (
        <div className={`${styles.card} ${!category.active ? styles.disabledCard : ""}`}>

            <div className={styles.productName}>
                {category.name}
                {!category.active && <span className={styles.inactiveBadge}> (Inactiva)</span>}
            </div>

            <div className={styles.productDescription}>
                <p title={category.description}>
                    {category.description || "Sin descripción disponible."}
                </p>

                <div className={styles.productMeta}>
                    <span
                        onClick={() => onToggleStatus(category.id, category.active, category.name)}
                        className={`${styles.statusToggle} ${category.active ? styles.active : styles.inactive}`}
                        style={{ cursor: 'pointer' }}
                    >
                        <strong>Estado:</strong> {category.active ? "Activo" : "Inactivo"}
                    </span>
                </div>
            </div>

            <div className={styles.actions}>
                <button
                    onClick={() => onEdit(category.id)}
                    className={`${styles.editButton} ${styles.actionButton}`}
                >
                    Editar
                </button>

                <button
                    onClick={() => onToggleStatus(category.id, category.active, category.name)}
                    className={`${styles.actionButton} ${category.active ? styles.deactivateButton : styles.activateButton}`}
                >
                    {category.active ? "Desactivar" : "Activar"}
                </button>
            </div>
        </div>
    );
}