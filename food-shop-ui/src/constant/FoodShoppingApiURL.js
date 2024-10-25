

export const ServerURL = import.meta.env.VITE_API_URL;

// authorization url
export const AuthorizationUserUrl = `${ServerURL}/auth/account`;
export const AuthorizationAdminUrl = `${ServerURL}/admin/account`;

export const AuthorizationUrl =  `${ServerURL}/auth/admin`;
// account service
const AccountService = `${ServerURL}/account`;
// admin
const AdminUrl = `${AccountService}/admin`;
export const IsAdminUrl = `${AdminUrl}/isAdmin`;
export const AccountAdminEmployee = `${AdminUrl}/employees`;
export const AccountAdminCustomer = `${AdminUrl}/customers`;
export const AccountAdminUserEmployee = `${AccountAdminEmployee}/user`;
export const AccountAdminUserCustomer = `${AccountAdminCustomer}/user`;
export const CustomersAdminUrl = `${AccountAdminCustomer}/users`;
export const EmployeesAdminUrl = `${AccountAdminEmployee}/users`
export const RegisterEmployeeUrl = `${AdminUrl}/registerEmployee`;
// reset password
export const AdminResetUserPassword = `${AdminUrl}/user`;
// customer
export const CustomerUrl = `${AccountService}/customer`;
export const CustomerCreditCardUrl = `${CustomerUrl}/credit`;
export const IsCustomerUrl = `${CustomerUrl}/isCustomer`;
export const CustomerInfoUrl = `${CustomerUrl}/info`;
export const UploadCustomerImageUrl= `${CustomerUrl}/image/upload`;
export const SendCustomerEmailVerificationUrl = `${CustomerUrl}/sendEmailVerification`;
// employee
export const EmployeeUrl = `${AccountService}/employee`;
export const IsEmployeeUrl = `${EmployeeUrl}/isEmployee`;
export const EmployeeInfoUrl = `${EmployeeUrl}/info`;
export const UploadEmployeeImageUrl= `${EmployeeUrl}/image/upload`;
// user
export const UserUrl = `${AccountService}/user`;
export const UserInfoUrl = `${UserUrl}/userInfo`;
export const UpdateUserPasswordUrl = `${UserUrl}/changePassword`;
// phone
const UserInfoPhoneUrl = `${UserUrl}/phone`;
export const GenerateOTPPhoneUrl = `${UserInfoPhoneUrl}/generateOTP`;
export const VerifyOTPPhoneUrl = `${UserInfoPhoneUrl}/verifyOTP`;

// address
const PaymentUrl = `${ServerURL}/payment`
const AddressUrl = `${PaymentUrl}/address`;
export const GetWardsUrl = `${AddressUrl}/wards`;
export const GetProvincesUrl = `${AddressUrl}/provinces`;
export const GetDistrictsUrl = `${AddressUrl}/districts`;
export const StoreLocationUrl = `${AddressUrl}/store`;
// shipping
const ShippingUrl = `${PaymentUrl}/shipping`;
export const GetShippingServicesUrl = `${ShippingUrl}/services`;
export const ShippingCostUrl = `${ShippingUrl}/cost`;

// payment service
const PaymentService = `${ServerURL}/payment`;
export const PaymentMethodsUrl = `${PaymentService}/methods`;
export const PaymentPayUrl = `${PaymentService}/pay`;
// cod
const PaymentCODUrl = `${PaymentService}/cod`;
export const PaymentCODSuccessful = `${PaymentCODUrl}/pay/successful`;
export const PaymentCODSCancel = `${PaymentCODUrl}/pay/cancel`;
// admin
const PaymentAdminUrl = `${PaymentService}/admin`;
export const GetRevenuePerMonth = `${PaymentAdminUrl}/revenue`;
export const GetTopSellingProductByYear = `${PaymentAdminUrl}/sellingProduct`;
export const GetPaymentStatusPercentageByYear = `${PaymentAdminUrl}/percentage`;
export const GetPaymentYears = `${PaymentAdminUrl}/years`;
// shop service
const ShopService = `${ServerURL}/shop`;
// home
// product
const HomeUrl = `${ShopService}/home`;
export const ProductsUrl = `${HomeUrl}/products`;
export const ProductsByNameUrl = `${ProductsUrl}/name`;
export const ProductsByIdUrl = `${ProductsUrl}/id`;
export const RecommendedProductsUrl = `${ProductsUrl}/recommended`;
export function ProductsByCategory(categoryName){
    return `${CategoriesUrl}/${categoryName}/products`;
}
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
export const ProductSizeAdminUrl = `${ProductAdminUrl}/size`;
export const ProductSizesAdminUrl = `${ProductAdminUrl}/sizes`;
export const UploadProductImageUrl = `${ProductUrl}/image/upload`;
// category admin
const CategoryUrl = `${ShopService}/category`;
export const CategoryAdminUrl = `${CategoryUrl}`;
export const UploadCategoryImageUrl = `${CategoryUrl}/image/upload`;
// discount admin
export const DiscountAdminUrl = `${ShopService}/discount`
export const DiscountsByProductAdminUrl = `${DiscountAdminUrl}/product`
export const DiscountTypeAdminUrl = `${DiscountAdminUrl}/type`
// cart
export const CartUrl = `${ShopService}/cart`;
export const CartProductUrl = `${CartUrl}/product`;
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
export const RegisterCustomerUrl = `${ServerURL}/register`;
export const ForgotUrl = `${ServerURL}/forgot`;
export const VerifyResetPasswordUrl = `${ServerURL}/verifyReset`;
export const ResetPasswordUrl = `${ServerURL}/reset`;
