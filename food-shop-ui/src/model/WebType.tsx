import { UserInfo } from "./Type";

export type Alert = {
    message: string,
    type: string,
    isShowed: boolean
}
export type Modal = {
    message: string,
    title: string,
    isShowed: boolean,
    confirmAction: ()=> void,
}
export type CartContextType = {
    numberOfCartProducts: number;
    setNumberOfCartProducts: (value: number) => void;
}
export type UserInfoContext = {
    userInfo: UserInfo,
    setUserInfo: (userInfo: UserInfo) => void;
}