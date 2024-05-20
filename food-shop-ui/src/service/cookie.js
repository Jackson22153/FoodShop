function getCookie(name) {
    let cookieArr = document.cookie.split(";");

    for(let i = 0; i < cookieArr.length; i++) {
        let cookiePair = cookieArr[i].split("=");

        if(name == cookiePair[0].trim()) {
            return cookiePair[1];
        }
    }
    return null;
}
function decodeCartCookie(encodedCartJson){
    const jsonStr = atob(encodedCartJson);
    return JSON.parse(jsonStr);
}


export function getNumberOfCartProducts(encodedCartJson){
    if(encodedCartJson != undefined && encodedCartJson != null){
        const cartProducts = decodeCartCookie(encodedCartJson);
        // console.log(cartProducts)
        return cartProducts.length;
    }
    return 0;
}
export const getAccessToken = ()=>{
    const accessToken = getCookie("accesstoken");
    return accessToken;
}
export const getIdToken = ()=>{
    const accessToken = getCookie("idtoken");
    return accessToken;
}