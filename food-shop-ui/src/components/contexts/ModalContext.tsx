import { createContext } from "react";
import { ModalContextType } from "../../model/WebType";

const modalContext = createContext<ModalContextType>({
    modal: {
        title: "",
        message: "",
        isShowed: false
    },
    setModal: ()=>{}
});

export const ModalProvider = modalContext.Provider;
export const ModalConsumer = modalContext.Consumer;


export default modalContext;