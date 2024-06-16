
export const ServerURL = "http://localhost:8060";

// account service
const AccountService = `${ServerURL}/account`;
// admin
const AdminUrl = `${AccountService}/admin`;
export const IsAdminUrl = `${AdminUrl}/isAdmin`;
export const CustomersAdminUrl = `${AdminUrl}/customers`
export const EmployeesAdminUrl = `${AdminUrl}/employees`
export const RolesAdminUrl = `${AdminUrl}/roles`
export const RolesEmployeeAdminUrl = `${RolesAdminUrl}/employee`
export const UsersAdminUrl = `${AdminUrl}/users`
export const UsersRolesAdminUrl = `${UsersAdminUrl}/roles`
export const ResetPasswordAdminUrl = (userID)=>{
    return `${UsersAdminUrl}/${userID}/password`
};
// customer
export const CustomerUrl = `${AccountService}/customer`;
export const IsCustomerUrl = `${CustomerUrl}/isCustomer`;
export const CustomerInfoUrl = `${CustomerUrl}/info`;

// employee
export const EmployeeUrl = `${AccountService}/employee`;
export const IsEmployeeUrl = `${EmployeeUrl}/isEmployee`;
export const EmployeeInfoUrl = `${EmployeeUrl}/info`;
// user
export const UserUrl = `${AccountService}/user`;
export const UserInfoUrl = `${UserUrl}/userInfo`;
export const UploadUserImageUrl = `${UserUrl}/upload`;


// shop service
const ShopService = `${ServerURL}/shop`;
// home
// product
const HomeUrl = `${ShopService}/home`;
export const ProductsUrl = `${HomeUrl}/products`;
export const ProductsByNameUrl = `${ProductsUrl}/name`;
export const ProductsByIdUrl = `${ProductsUrl}/id`;
export const RecommendedProductsUrl = `${ProductsUrl}/recommended`;
export function ProductsByCategories(categoryName){
    return `${CategoriesUrl}/${categoryName}/products`;
}
// image
const ShopImageURl = `${ShopService}/image`;
export const UploadProductImageUrl = `${ShopImageURl}/product/upload`;
export const UploadCategoryImageUrl = `${ShopImageURl}/category/upload`;
// categories
export const CategoriesUrl = `${HomeUrl}/categories`;
export const CategoriesIDUrl = `${CategoriesUrl}/id`;
export const CategoriesNameUrl = `${CategoriesUrl}/name`;
// search
const SearchUrl = `${ShopService}/search`;
export const SearchProductsUrl = `${SearchUrl}/products`;
export const RecommendedProductsByCategoryUrl = `${SearchUrl}/recommended`;
// product admin
const ProductUrl = `${ShopService}/product`;
export const ProductAdminUrl = `${ProductUrl}`;
// category admin
const CategoryUrl = `${ShopService}/category`;
export const CategoryAdminUrl = `${CategoryUrl}`;
// discount admin
export const DiscountAdminUrl = `${ShopService}/discount`
export const DiscountsByProductAdminUrl = `${DiscountAdminUrl}/product`
export const DiscountTypeAdminUrl = `${DiscountAdminUrl}/type`
// cart
export const CartUrl = `${ShopService}/cart`;
export const CartProductsUrl = `${CartUrl}/products`;
export const NumberOfCartProductsUrl = `${CartProductsUrl}/number`;
export const CartOrderUrl = `${CartUrl}/order`;


// notification service
const NotificationService = `${ServerURL}/notification`;
// customer
const NotificationCustomerUrl = `${NotificationService}/customer`;
export const CustomerNotificationsUrl = `${NotificationCustomerUrl}/notification`
export const MarkAsReadCustomerNotificationUrl = `${CustomerNotificationsUrl}/mark`
export const CustomerSummaryNotificationUrl = `${NotificationCustomerUrl}/summary`
// employee
const NotificationEmployeeUrl = `${NotificationService}/employee`;
export const EmployeeNotificationsUrl = `${NotificationEmployeeUrl}/notification`;
export const MarkAsReadEmployeeNotificationUrl = `${EmployeeNotificationsUrl}/mark`
export const EmployeeSummaryNotificationUrl = `${NotificationEmployeeUrl}/summary`
// websocket
export const NotificationServiceWsUrl = `${NotificationService}/chat`;

// order service
const OrderService = `${ServerURL}/order`;
// customer
const CustomerOrderPathUrl = `${OrderService}/customer`;
// process order
const CustomerOrderUrl = `${CustomerOrderPathUrl}/order`;
export const PlaceOrderUrl =  `${CustomerOrderUrl}/place`;
export const ReceiveOrderUrl =  `${CustomerOrderUrl}/receive`;
// get orders
export const CustomerOrdersUrl = `${CustomerOrderPathUrl}/orders`;
// employee
const EmployeeOrderPathUrl = `${OrderService}/employee`;
// process order
const EmployeeOrderUrl = `${EmployeeOrderPathUrl}/order`;
export const ConfirmOrderUrl = `${EmployeeOrderUrl}/confirm`;
export const CancelOrderUrl = `${EmployeeOrderUrl}/cancel`;
export const FulfillOrderUrl = `${EmployeeOrderUrl}/fulfill`;
// get orders
export const EmployeeOrdersUrl = `${EmployeeOrderPathUrl}/orders`;
export const EmployeeOrderSummarysUrl = `${EmployeeOrderPathUrl}/summary`;


// ws 
// topic ws
const TopicWsPrefix = '/topic';
export const OrderWsUrl = `${TopicWsPrefix}/order`;
export const EmployeeNotificationOrderWsUrl = `${TopicWsPrefix}/employee.notification.order`;
export const EmployeeNotificationAccountWsUrl = `${TopicWsPrefix}/employee.notification.account`;
export const CustomerNotificationAccountWsUrl = `${TopicWsPrefix}/customer.notification.account`;

// ws user destination prefix
const UserDestinationPrefix = `/user`;
// user queue cart
export const QUEUE_MESSAGES = `${UserDestinationPrefix}/queue/messages`

// gateway service
export const LoginUrl = `${ServerURL}/login`;
export const LogoutUrl = `${ServerURL}/logout`;
