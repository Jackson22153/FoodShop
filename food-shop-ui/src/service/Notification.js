import { getEmployee } from "../api/AdminApi";
import { customerInfo, customerOrder, employeeInfo, employeeOrder 
} from "../constant/FoodShoppingURL";
import { NOTIFICATION_TOPIC, ROLE } from "../constant/WebConstant";

// get url for any roles
export function getUrlFromNotification(notification, roles){
    if(roles.includes(ROLE.CUSTOMER.toLowerCase())){
        return getCustomerUrlFromNotification(notification);
    }else if(roles.includes(ROLE.EMPLOYEE.toLowerCase())){
        return getEmployeeUrlFromNotification(notification);
    }
    return '/';
}
// employee
export function getEmployeeUrlFromNotification(notification){
    if(notification.topic.toLowerCase()==NOTIFICATION_TOPIC.ORDER.toLowerCase()){
        return employeeOrder;
    }else if(notification.topic.toLowerCase()==NOTIFICATION_TOPIC.ACCOUNT.toLowerCase()){
        return employeeInfo;
    }
    return '/'
}
// customer
export function getCustomerUrlFromNotification(notification){
    if(notification.topic.toLowerCase()==NOTIFICATION_TOPIC.ORDER.toLowerCase()){
        return customerOrder;
    }else if(notification.topic.toLowerCase()==NOTIFICATION_TOPIC.ACCOUNT.toLowerCase()){
        return customerInfo;
    }
    return '/'
}