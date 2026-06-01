import {useProductDelete} from "../hooks/useProductDelete.ts";

interface ProductDeleteProps {
    productId: number;
    productName: string;
    onDeleteSuccess: () => void;
    className?: string;
}

export default function ProductDelete({productId, productName, onDeleteSuccess, className}: ProductDeleteProps) {
    const {deleteProduct, loading: isDeleting, error: deleteError} = useProductDelete();

    const handleDelete = async () => {
        const confirmed = window.confirm(`¿Estás seguro de que deseas eliminar el producto "${productName}"?`);

        if (confirmed) {
            const success = await deleteProduct(productId);
            if (success) {
                onDeleteSuccess();
            }
        }
    };

    return (
        <div>
            <button
                onClick={handleDelete}
                disabled={isDeleting}
                className={className}
            >
                {isDeleting ? "Eliminando..." : "Eliminar"}
            </button>

            {deleteError && (
                <p>
                    {deleteError}
                </p>
            )}
        </div>
    );
}