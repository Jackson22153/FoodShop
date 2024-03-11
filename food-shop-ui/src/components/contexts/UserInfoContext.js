import { createContext } from "react";
import { userInfoContext } from "../ui/homePath/home/Home";

const UserInfoContext = createContext<UserInfo>({
    username: "",
    isAuthenticated: false
})

export const UserInfoProvider = userInfoContext.Provider;
export const UserInfoConsumer = userInfoContext.Consumer;

export default UserInfoContext;