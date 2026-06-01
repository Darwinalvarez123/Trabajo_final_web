export class ApiError extends Error
{
    status: number;
    violations?: { field: string; message: string }[];
    constructor(status:number, menssaje: string, violations?: { field: string; message: string }[]
    ) {
        super(menssaje);
        this.name = "ApiError";
        this.status = status;
        this.violations = violations;

        Object.setPrototypeOf(
            this,
            ApiError.prototype
        )
    }
}