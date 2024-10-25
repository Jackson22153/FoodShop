import axios from "axios";
import { 
    GetDistrictsUrl, GetProvincesUrl, GetWardsUrl, 
    ShippingCostUrl,
    StoreLocationUrl
} from "../constant/FoodShoppingApiURL";

// get provinces
export function getProvinces(){
    return axios.get(GetProvincesUrl, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// get districts
export function getDistricts(provinceId){
    return axios.get(`${GetDistrictsUrl}?provinceId=${provinceId}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// get wards
export function getWards(districtId){
    return axios.get(`${GetWardsUrl}?districtId=${districtId}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// get shipping cost
export function getShippingCost(wardCode, districtId, cityId){
    return axios.get(`${ShippingCostUrl}?wardCode=${wardCode}&districtId=${districtId}&cityId=${cityId}`, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}
// get store location
export function getStoreLocation(){
    return axios.get(StoreLocationUrl, {
        withCredentials: true,
        headers:{
            "Content-Type": 'application/json',
        }
    })
}