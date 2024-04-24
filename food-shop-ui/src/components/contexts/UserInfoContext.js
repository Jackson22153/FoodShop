import { createContext } from "react";

const userInfoContext = createContext({
    userID: '',
    username: "",
    isAuthenticated: false
})

export const UserInfoProvider = userInfoContext.Provider;
export const UserInfoConsumer = userInfoContext.Consumer;

export default userInfoContext;