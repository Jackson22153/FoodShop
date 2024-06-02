import axios from "axios";
import { UploadProductImageUrl } from "../constant/FoodShoppingApiURL";

export function uploadProductImage(file){
    const formData = new FormData();
    formData.append('file', file);
    
    return axios.post(UploadProductImageUrl, formData, {
        headers:{
            "Content-Type": "multipart/form-data",
        }
    });
}