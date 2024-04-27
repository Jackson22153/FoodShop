
const URL = "http://localhost:8060";
// service
const ShopService = `${URL}/shop`;
const AccountService = `${URL}/account`;
// account service
const AdminUrl = `${AccountService}/admin`;
// shop service
const HomeUrl = `${ShopService}/home`;
const SearchUrl = `${ShopService}/search`;
// ws prefix destination
const AppPrefixDestinationUrl = `/app`;

// gateway
export const LoginUrl = `${URL}/loginBE`;
export const LogoutUrl = `${URL}/logout`;
export const UsernameUrl = `${URL}/user`;
export const IsAuthenticatedUrl = `${URL}/isAuthenticated`;
// product
export const SearchProductsUrl = `${SearchUrl}/products`;
export const RecommendedProductsByCategoryUrl = `${SearchUrl}/recommended`;
export const ProductsUrl = `${HomeUrl}/products`;
export const ProductsByNameUrl = `${ProductsUrl}/name`
export const ProductsByIdUrl = `${ProductsUrl}/id`
export const RecommendedProductsUrl = `${ProductsUrl}/recommended`;
export function ProductsByCategories(categoryName){
    return `${CategoriesUrl}/${categoryName}/products`;
}
// categories
export const CategoriesUrl = `${HomeUrl}/categories`;
// shippers
export const ShippersUrl = `${HomeUrl}/shippers`;
// adminurl
export const ProductAdminUrl = `${AdminUrl}/product`;
export const DiscountAdminUrl = `${AdminUrl}/discount`
// customer
export const CustomerUrl = `${AccountService}/customer`;
export const CustomerInfoUrl = `${CustomerUrl}/info`;
export const CustomerOrdersUrl = `${CustomerUrl}/orders`;
// employee
export const EmployeeUrl = `${AccountService}/employee`;
export const EmployeeInfoUrl = `${EmployeeUrl}/info`;
export const EmployeeOrdersUrl = `${EmployeeUrl}/orders`;
export const EmployeePendingOrdersUrl = `${EmployeeOrdersUrl}/pending`;
// cart
export const CartUrl = `${ShopService}/cart`;
export const CartProductsUrl = `${CartUrl}/products`;
export const NumberOfCartProductsUrl = `${CartProductsUrl}/number`;
// websocket
export const AccountWSUrl = `${AccountService}/chat`;
// shop service ws
export const ShopWSUrl = `${ShopService}/chat`;
export const AddItemTOCartWsUrl = `${AppPrefixDestinationUrl}/cart.addItem`;
// user queue cart
export const QUEUE_CART = '/user/queue/cart';

