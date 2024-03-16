import { createContext } from "react";

const userInfoContext = createContext({
    username: "",
    isAuthenticated: false
})

export const UserInfoProvider = userInfoContext.Provider;
export const UserInfoConsumer = userInfoContext.Consumer;

export default userInfoContext;