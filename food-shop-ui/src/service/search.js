import { searchProducts } from "../api/SearchApi";
const SEARCH_TIMEOUT = 300;
let debounceTimeOut;

export function onUserInput(userInput, searchCallBack){
    clearTimeout(debounceTimeOut);
    debounceTimeOut = setTimeout(()=>{
        if(userInput.length>2){
            searchCallBack(userInput);
        }
    }, SEARCH_TIMEOUT);
}

