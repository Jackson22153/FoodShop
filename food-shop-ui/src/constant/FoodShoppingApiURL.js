
export const ServerURL = "http://localhost:8060";
// service
const ShopService = `${ServerURL}/shop`;
const AccountService = `${ServerURL}/account`;
const OrderService = `${ServerURL}/order`;
// account service
const AdminUrl = `${AccountService}/admin`;
// user
export const UserUrl = `${AccountService}/user`;
export const UserInfoUrl = `${UserUrl}/userInfo`;
export const UploadUserImageUrl = `${UserUrl}/upload`;

// shop service
const HomeUrl = `${ShopService}/home`;
// product admin
const ProductUrl = `${ShopService}/product`;
export const ProductAdminUrl = `${ProductUrl}`;
// category admin
const CategoryUrl = `${ShopService}/category`;
export const CategoryAdminUrl = `${CategoryUrl}`;
const SearchUrl = `${ShopService}/search`;
// discount admin
export const DiscountAdminUrl = `${ShopService}/discount`
export const DiscountsByProductAdminUrl = `${DiscountAdminUrl}/product`

// notification service
const NotificationService = `${ServerURL}/notification`;
export const UserNotificationUrl = `${NotificationService}/notification`;
// customer
export const CustomerNotificationsUrl = `${NotificationService}/customer`;
export const MarkAsReadCustomerNotificationUrl = `${CustomerNotificationsUrl}/mark-as-read`
export const CustomerNotificationSummaryUrl = `${CustomerNotificationsUrl}/summary`
// employee
export const EmployeeNotificationsUrl = `${NotificationService}/employee`;
export const MarkAsReadEmployeeNotificationUrl = `${EmployeeNotificationsUrl}/mark-as-read`
export const EmployeeNotificationSummaryUrl = `${EmployeeNotificationsUrl}/summary`

// ws prefix destination
const AppPrefixDestinationUrl = `/app`;
// ws user destination prefix
const UserDestinationPrefix = `/user`;

const TopicWsPrefix = '/topic';
// ws queue 
const QueueWs = `/queue`

// gateway
export const LoginUrl = `${ServerURL}/login`;
export const LogoutUrl = `${ServerURL}/logout`;
export const UsernameUrl = `${ServerURL}/user`;
export const IsAuthenticatedUrl = `${ServerURL}/isAuthenticated`;
// product
export const SearchProductsUrl = `${SearchUrl}/products`;
export const RecommendedProductsByCategoryUrl = `${SearchUrl}/recommended`;
export const ProductsUrl = `${HomeUrl}/products`;
export const ProductsByNameUrl = `${ProductsUrl}/name`;
export const ProductsByIdUrl = `${ProductsUrl}/id`;
export const RecommendedProductsUrl = `${ProductsUrl}/recommended`;
export function ProductsByCategories(categoryName){
    return `${CategoriesUrl}/${categoryName}/products`;
}
export const UploadProductImageUrl = `${HomeUrl}/upload`;
// categories
export const CategoriesUrl = `${HomeUrl}/categories`;
export const CategoriesIDUrl = `${CategoriesUrl}/id`;
export const CategoriesNameUrl = `${CategoriesUrl}/name`;
// shippers
export const ShippersUrl = `${HomeUrl}/shippers`;
// adminurl
export const IsAdminUrl = `${AdminUrl}/isAdmin`;


// export const CategoryAdminUrl = `${AdminUrl}/category`
export const DiscountTypeAdminUrl = `${AdminUrl}/discountTypes`
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
export const CustomerOrdersUrl = `${CustomerUrl}/orders`;

// employee
export const EmployeeUrl = `${AccountService}/employee`;
export const IsEmployeeUrl = `${EmployeeUrl}/isEmployee`;
export const EmployeeInfoUrl = `${EmployeeUrl}/info`;
export const EmployeeOrdersUrl = `${EmployeeUrl}/orders`;
export const EmployeePendingOrdersUrl = `${EmployeeOrdersUrl}/pending`;


// cart
export const CartUrl = `${ShopService}/cart`;
export const CartProductsUrl = `${CartUrl}/products`;
export const NumberOfCartProductsUrl = `${CartProductsUrl}/number`;
export const CartOrderUrl = `${CartUrl}/order`;
// websocket endpoint
export const AccountWSUrl = `${AccountService}/chat`;
export const OrderServiceWSUrl = `${OrderService}/chat`;
// customer
const CustomerWsUrl = `${AppPrefixDestinationUrl}/customer`;
const CustomerOrderWsUrl = `${CustomerWsUrl}/order`;
export const PlaceOrderWsUrl = `${CustomerOrderWsUrl}/placeOrder`;
export const ReceiveOrderWsUrl = `${CustomerOrderWsUrl}/receive`;
// employee
const EmployeeWsUrl = `${AppPrefixDestinationUrl}/employee`;
const EmployeeOrderWsUrl = `${EmployeeWsUrl}/order`;
export const ConfirmOrderWsUrl = `${EmployeeOrderWsUrl}/confirm`;
export const CancelOrderWsUrl = `${EmployeeOrderWsUrl}/cancel`;
export const FulfillOrderWsUrl = `${EmployeeOrderWsUrl}/fulfill`;
// shop service ws
export const ShopWSUrl = `${ShopService}/chat`;
export const AddItemTOCartWsUrl = `${AppPrefixDestinationUrl}/cart.addItem`;
// topic ws
export const OrderWsUrl = `${TopicWsPrefix}/order`;
const NotificationWsUrl = `${TopicWsPrefix}/notification`;
export const EmployeeNotificationOrderWsUrl = `${TopicWsPrefix}/employee.notification.order`;
// user queue cart
export const QUEUE_CART = `${UserDestinationPrefix}/queue/cart`;
export const QUEUE_MESSAGES = `${UserDestinationPrefix}/queue/messages`

