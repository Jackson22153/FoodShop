import axios from "axios";
import { 
    CategoriesUrl, ProductsByCategories, ProductsByIdUrl, 
    ProductsByNameUrl, ProductsUrl, RecommendedProductsUrl, 
    SearchProductsUrl, 
    ShippersUrl
} 
from '../constant/FoodShoppingApiURL';

// const BASE_URL = `${UrlService.clientService}/search`

// categories
// get
export async function getCategories(pageNumber){
    return axios.get(`${CategoriesUrl}?page=${pageNumber}`, {
        'Content-Type': 'application/json'
    });
}
export async function getCategory(categoryName){
    return axios.get(`${CategoriesUrl}/${categoryName}`, {
        'Content-Type': 'application/json'
    });
}

// products
// get
export async function searchProducts(productName){
    const url = SearchProductsUrl;
    return axios.get(`${url}?l=${productName}`,{
        'Content-Type': 'application/json'
    });
}
export async function getProductsByCategory(categoryName, pageNumber){
    const url = ProductsByCategories(categoryName);
    return axios.get(`${url}?page=${pageNumber}`,{
        'Content-Type': 'application/json'
    });
}
export async function getProducts(page){
    return axios.get(`${ProductsUrl}?page=${page}`, {
        'Content-Type': 'application/json'
    })
}
export async function getProductsByProductName(productName, page){
    return axios.get(`${ProductsByNameUrl}?l=${productName}&page=${page}`,{
        'Content-Type': 'application/json'
    })
}
export async function getProductByID(productID){
    return axios.get(`${ProductsByIdUrl}/${productID}`,{
        'Content-Type': 'application/json'
    })
}
export async function getRecommendedProduct(){
    return axios.get(`${RecommendedProductsUrl}`,{
        'Content-Type': 'application/json',
    })
}

// shippers
export async function getShippers(page){
    return axios.get(`${ShippersUrl}?page=${page}`,{
        'Content-Type': 'application/json'
    })
}


// export async function getRecommendedProducts(search){
//     return axios.get(`${BASE_URL}/products/recommend?s=${search}`)
// }
// export async function searchProducts(searchValue, page){
//     return axios.get(`${BASE_URL}/products/search?s=${searchValue}&page=${page}`,{
//         withCredentials: true
//     })
// }

// // shipper
// export async function getShippers(page){
//     return axios.get(`${BASE_URL}/shippers?page=${page}`,{
//         withCredentials: true,
//         headers:{
//           'Content-Type': 'application/json'
//         }
//     })
// }
