import { searchProducts } from "../api/SearchApi";

let debounceTimeOut;
function sendToBackend(userInput, handleSearchResultCallback){
    searchProducts(userInput).then(res =>{
        if(res.status===200){
            const data = res.data;
            // console.log(data);
            handleSearchResultCallback(data);
            // return data;
        }
    })
}

export function onUserInput(userInput, handleSearchResultCallback){
    clearTimeout(debounceTimeOut);
    debounceTimeOut = setTimeout(()=>{
        if(userInput.length>2){
            sendToBackend(userInput, handleSearchResultCallback);
        }
    }, 300);
}

