
const URL = "http://localhost:8060";
// service
const ShopService = `${URL}/shop`;
const AccountService = `${URL}/account`;
// account service
const AdminUrl = `${AccountService}/admin`;
// shop service
const HomeUrl = `${ShopService}/home`;
const SearchUrl = `${ShopService}/search`;
// ws prefix destination
const AppPrefixDestinationUrl = `/app`;
// ws user destination prefix
const UserDestinationPrefix = `/user`;

const TopicWsPrefix = '/topic';
// ws queue 
const QueueWs = `/queue`

// gateway
export const LoginUrl = `${URL}/loginBE`;
export const LogoutUrl = `${URL}/logout`;
export const UsernameUrl = `${URL}/user`;
export const IsAuthenticatedUrl = `${URL}/isAuthenticated`;
// product
export const SearchProductsUrl = `${SearchUrl}/products`;
export const RecommendedProductsByCategoryUrl = `${SearchUrl}/recommended`;
export const ProductsUrl = `${HomeUrl}/products`;
export const ProductsByNameUrl = `${ProductsUrl}/name`
export const ProductsByIdUrl = `${ProductsUrl}/id`
export const RecommendedProductsUrl = `${ProductsUrl}/recommended`;
export function ProductsByCategories(categoryName){
    return `${CategoriesUrl}/${categoryName}/products`;
}
// categories
export const CategoriesUrl = `${HomeUrl}/categories`;
export const CategoriesIDUrl = `${CategoriesUrl}/id`;
export const CategoriesNameUrl = `${CategoriesUrl}/name`;
// shippers
export const ShippersUrl = `${HomeUrl}/shippers`;
// adminurl
export const ProductAdminUrl = `${AdminUrl}/product`;
export const DiscountAdminUrl = `${AdminUrl}/discount`
export const DiscountsByProductAdminUrl = `${DiscountAdminUrl}/product`
export const CategoryAdminUrl = `${AdminUrl}/category`
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
export const CustomerInfoUrl = `${CustomerUrl}/info`;
export const CustomerOrdersUrl = `${CustomerUrl}/orders`;
export const CustomerNotificationsUrl = `${CustomerUrl}/notifications`;
// employee
export const EmployeeUrl = `${AccountService}/employee`;
export const EmployeeInfoUrl = `${EmployeeUrl}/info`;
export const EmployeeOrdersUrl = `${EmployeeUrl}/orders`;
export const EmployeePendingOrdersUrl = `${EmployeeOrdersUrl}/pending`;
export const EmployeeNotificationsUrl = `${EmployeeUrl}/notifications`;
// user
export const UserUrl = `${AccountService}/user`;
export const UserInfoUrl = `${UserUrl}/userInfo`;
// cart
export const CartUrl = `${ShopService}/cart`;
export const CartProductsUrl = `${CartUrl}/products`;
export const NumberOfCartProductsUrl = `${CartProductsUrl}/number`;
// websocket
// account service ws
export const AccountWSUrl = `${AccountService}/chat`;
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

