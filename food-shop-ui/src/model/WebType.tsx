import { UserInfo } from "./Type";

export type Alert = {
    message: string,
    type: string,
    isShowed: boolean
}
export type Modal = {
    title: string,
    message: string,
    isShowed: boolean
}
export type CartContextType = {
    numberOfCartProducts: number;
    setNumberOfCartProducts: (value: number) => void;
}
export type UserInfoContext = {
    userInfo: UserInfo,
    setUserInfo: (userInfo: UserInfo) => void;
}