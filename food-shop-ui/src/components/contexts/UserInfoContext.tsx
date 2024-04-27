import { createContext } from "react";
import { UserInfo } from "../../model/Type";

const userInfoContext = createContext<UserInfo>({
    userID: '',
    username: "",
    isAuthenticated: false
})

export const UserInfoProvider = userInfoContext.Provider;
export const UserInfoConsumer = userInfoContext.Consumer;

export default userInfoContext;