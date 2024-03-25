
const URL = "http://localhost:8060";
// service
const ShopService = `${URL}/shop`;
const AccountService = `${URL}/account`;
// account service
const AdminUrl = `${AccountService}/admin`;
// shop service
const HomeUrl = `${ShopService}/home`;
const SearchUrl = `${ShopService}/search`;

// public url
// gateway
export const LoginUrl = `${URL}/loginBE`;
export const UsernameUrl = `${URL}/user`;
export const IsAuthenticatedUrl = `${URL}/isAuthenticated`;
// product
export const SearchProductsUrl = `${SearchUrl}/products`;
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

