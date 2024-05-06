import { createContext } from "react";
import { NotificationContext } from "../../model/WebType";

const notificationMessagesContext = createContext<NotificationContext>({
    notifications: [],
    setNotifications: ()=>{}
});

export const NotificationMessagesProvider = notificationMessagesContext.Provider
export const NotificationMessagesConsumer = notificationMessagesContext.Consumer

export default notificationMessagesContext; 