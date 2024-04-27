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