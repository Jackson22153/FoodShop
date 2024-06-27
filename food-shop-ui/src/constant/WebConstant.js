// ALERT TYPE
export const ALERT_TYPE = {
    SUCCESS: 'success',
    DANGER: 'danger',
    PRIMARY: 'primary',
    INFO: 'info'
}
// NOTIFICATION TYPE
export const NOTIFICATION_TYPE={
    SUCCESSFUL: 'successful',
    FAILED: 'failed',
    ERROR: 'error',
    CANCEL: 'cancel'
}
// ORDER NOTIFICATION
export const ORDER_NOTIFICATION = {
    PLACE_ORDER: 'place_order', 
    CONFIRM_ORDER: 'confirm_order',
    RECEIVE_ORDER: 'receive_order',
    FULFILL_ORDER: 'fulfill_order',
    CANCEL_ORDER: 'cancel_order'
}
// ORDER STATUS
export const ORDER_STATUS = {
    PENDING: 'pending',
    CONFIRMED: 'confirmed',
    SHIPPING: 'shipping',
    SUCCESSFUL: 'successful',
    CANCELED: 'canceled',
    ALL: 'all'
}
// DISCOUNT TYPE
export const DISCOUNT_TYPE = {
    CODE: 'Code',
    PERCENTAGE_BASED: "Percentage-based"
}

// CART COOKIE
export const CART_COOKIE = 'cart';
// ALERT TIMEOUT
export const ALERT_TIMEOUT = 3000;
export const SLIDE_TIMEOUT = 5000;
// ROLE
export const ROLE = {
    CUSTOMER: "CUSTOMER",
    EMPLOYEE: "EMPLOYEE",
    ADMIN: "ADMIN"
}
// NOTIFICATION
export const NOTIFICATION_TOPIC = {
    ORDER: 'Order',
    ACCOUNT: 'Account'
}
// MARK NOTIFICATION TYPE
export const MARK_NOTIFICATION_TYPE = {
    ALL: 'All',
    NOTIFICATION: 'Notification',
    BROADCAST: 'Broadcast'
}