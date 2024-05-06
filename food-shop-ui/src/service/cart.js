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
export const numberOfProductsInCart = ()=>{
    const cartCookie = getCookie("cart");
    const cartJson = atob(cartCookie);
    const cart =JSON.parse(cartJson);
    return cart.length
}