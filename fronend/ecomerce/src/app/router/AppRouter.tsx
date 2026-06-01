import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from "../../pages/Login/Login";
import PrivateRoute from "./PrivateRoute";
import DashboardAdminView from "../../pages/admin/DashboardAdminView.tsx";
import DashboardUser from "../../pages/user/DashboardUser";
import Register from "../../pages/Register/Register";
import AdminLayout from "../layout/AdminLayout.tsx";
import UserLayout from "../layout/UserLayout.tsx"; // Importar UserLayout
import ProductUpdate from "../../features/product/components/ProductUpdate.tsx";
import ProductCreate from "../../features/product/components/ProductCreate.tsx";
import ProductsGetAll from "../../features/product/components/ProductsGetAll.tsx";
import CategoryList from "../../features/category/components/CategoryList.tsx";
import CategoryCreate from "../../features/category/components/CategoryCreate.tsx";
import CategoryUpdate from "../../features/category/components/CategoryUpdate.tsx";
import InventarioList from "../../features/inventory/components/InventarioList.tsx";
import CustomerList from "../../features/customer/components/CustomerList.tsx";
import CustomerAddressList from "../../features/address/components/CustomerAddressList.tsx";
import OrdersGetAll from "../../features/order/components/OrdersGetAll.tsx";
import OrderDetails from "../../features/order/components/OrderDetails.tsx";
import Home from "../../pages/home/Home";

export default function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home/>}/> {/* Nueva ruta para la página de inicio */}
                <Route path="/login" element={<Login/>}/>
                <Route path="/register" element={<Register/>}/>

                <Route
                    path="/admin"
                    element={
                        <PrivateRoute requiredRole="ROLE_ADMIN">
                            <AdminLayout/>
                        </PrivateRoute>
                    }
                >

                    <Route index element={<DashboardAdminView/>}/>
                    <Route path="products" element={<ProductsGetAll/>}/>
                    <Route path="categories" element={<CategoryList/>}/>
                    <Route path="categories/new" element={<CategoryCreate/>}/>
                    <Route path="inventory/:productId" element={<InventarioList/>}/>
                    <Route path="categories/:id" element={<CategoryUpdate/>}/>
                    <Route path="products/:id" element={<ProductUpdate/>}/>
                    <Route path="products/new" element={<ProductCreate/>}/>
                    <Route path={"customer"} element={<CustomerList/>}/>
                    <Route path="customers/:customerId/addresses" element={<CustomerAddressList/>}/>
                    <Route path="orders" element={<OrdersGetAll />} />
                    <Route path="orders/:orderId" element={<OrderDetails />} />
                </Route>

                <Route
                    path="/user"
                    element={
                        <PrivateRoute>
                            <UserLayout/> 
                        </PrivateRoute>
                    }
                >
                    <Route index element={<DashboardUser/>}/> {/* DashboardUser como ruta index dentro de UserLayout */}
                </Route>

            </Routes>

        </BrowserRouter>
    );
}
