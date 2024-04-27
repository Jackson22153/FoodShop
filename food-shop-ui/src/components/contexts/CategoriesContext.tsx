import { createContext } from "react";
import { Category } from "../../model/Type";

const CategoriesContext = createContext<Category[]>([]);

export const CategoriesProvider = CategoriesContext.Provider;
export const CategoriesConsumer = CategoriesContext.Consumer;


export default CategoriesContext;