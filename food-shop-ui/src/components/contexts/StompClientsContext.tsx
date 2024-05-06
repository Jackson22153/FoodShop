import { createContext } from "react"

type StompClients = {
    stompClientShop:any,
    stompClientAccount:any
}
const stompClientsContext = createContext<StompClients>({
    stompClientShop: null,
    stompClientAccount: null
})

export const StompClientsProvider = stompClientsContext.Provider;
export const StompClientsConsumer = stompClientsContext.Consumer;

export default stompClientsContext;