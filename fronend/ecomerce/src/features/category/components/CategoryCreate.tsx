import {useNavigate} from "react-router-dom";
import {useCreateCategory} from "../hooks/useCreateCartegory.ts";
import  type {CreateCategoryRequest} from "../type/category.ts";
import {CategoryForm} from "./CategoryForm.tsx";

export default function CategoryCreate()
{
    const {createCategory, loading: isCreating, error: createError} = useCreateCategory();
    const navigate = useNavigate();
    const dataForm: CreateCategoryRequest = {
        name: "",
        description: "",
    };

    const handleCrate = async (data: CreateCategoryRequest): Promise<void> =>
    {
        console.log("📦 BODY ENVIADO AL HOOK:", JSON.stringify(data, null, 2));
        const newCategory = await createCategory(data);
        if (newCategory)
        {
            alert("¡Categoría creada con éxito!");
            navigate("/admin");
        }
    }
    return(
        <div>
            <h2>Crear Categoría</h2>

            {createError && <p>Error al crear: {createError}</p>}
            <CategoryForm
                initialData={dataForm}
                onSubmit={handleCrate}
                isSubmitting={isCreating}
                submitButtonText={"Crear Categoría"}
            >
            </CategoryForm>

        </div>
    )


}