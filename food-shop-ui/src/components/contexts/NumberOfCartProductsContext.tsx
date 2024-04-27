import { createContext } from "react";
import { CartContextType } from "../../model/WebType";

const numberOfCartProductsContext = createContext<CartContextType>({
    numberOfCartProducts: 0,
    setNumberOfCartProducts: ()=> {}
});

export const NumberOfCartProductsProvider = numberOfCartProductsContext.Provider;
export const NumberOfCartProductsConsumer = numberOfCartProductsContext.Consumer;

export default numberOfCartProductsContext;