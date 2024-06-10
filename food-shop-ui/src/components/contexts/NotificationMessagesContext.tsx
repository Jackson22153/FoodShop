import { createContext } from "react";
import { Notification } from "../../model/Type";

const notificationMessagesContext = createContext<Notification|undefined>(undefined);

export const NotificationMessagesProvider = notificationMessagesContext.Provider
export const NotificationMessagesConsumer = notificationMessagesContext.Consumer

export default notificationMessagesContext; 