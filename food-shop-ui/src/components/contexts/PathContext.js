import { createContext } from "react";

const pathContext = createContext('');

export const PathProvider = pathContext.Provider;
export const PathConsumer = pathContext.Consumer;

export default pathContext;