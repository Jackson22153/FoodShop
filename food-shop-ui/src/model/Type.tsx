// category
export type Category={
    categoryID: number,
    categoryName: string,
    description: string,
    picture: string
}
// product
export type CurrentProduct = {
    productID: number,
    productName: string, 
    picture: string, 
    unitPrice: number, 
    unitsInStock: string,
    discountID: string,
    discountPercent: number,
    categoryName: string
}
export type ExistedProduct = {
    productID: number,
    productName: string, 
    picture: string, 
    unitPrice: number, 
    unitsInStock: string,
    discountID: string,
    discountPercent: number,
    categoryName: string,
    discontinued: boolean
}
export type CurrentProductDetail = {
    productID: number,
    productName: string, 
    picture: string, 
    unitPrice: number, 
    unitsInStock: number,
    quantityPerUnit: string,
    description: string,

    discountID: string,
    discountPercent: number,

    categoryID: number,
    categoryName: string,
}
export type Product={
    productID: number,
    productName: string,
    quantityPerUnit: number,
    unitPrice: number,
    unitsInStock: number,
    discontinued: boolean,
    picture: string,
    description: string
}
export type ProductSize={
    id: string,
    height: number,
    width: number,
    length: number,
    weight: number,
    productid: number,
}
export type ProductDetails={
    productID: number,
    productName: string,
    quantityPerUnit: number,
    unitPrice: number,
    unitsInStock: number,
    discontinued: boolean,
    picture: string,
    description: string,

    discountPercent: number,
    startDate: string,
    endDate: string,

    categoryID: number,
    categoryName: string,
}
// discount
export type DiscountType={
    discountTypeID: number,
    discountType: string,
    description: string
}
export type Discount = {
    discountID: string,
    discountPercent: number,
    discountCode: string,
    startDate: string,
    endDate: string,
    active: boolean
}
export type DiscountDetail = {
    discountID: string,
    discountPercent: number,
    discountCode: string,
    startDate: string,
    endDate: string,
    description: string,
    discountType: string,
    active: boolean
}
// payment method
export type PaymentMethod = {
    methodID: string,
    methodName: string,
    details: string
}
// cart
export type CartInfo = {
    products: CartProductInfo[],
    freight: number,
    totalPrice: number
}
export type CartProductInfo = {
    productID: number,
    productName: string,
    categoryName: string,
    quantity: number,
    totalDiscount: number,
    unitPrice: number,
    unitsInStock: number,
    picture: string,
    extendedPrice: number,
    discounts: DiscountBriefInfo[]
    isSelected: boolean
}

export type SellingProduct = {
    productID: number,
    productName: string,
    quantityPerUnit: number,
    unitPrice: number,
    unitsInStock: number,
    discontinued: boolean,
    picture: string,
    description: string,

    discountPercent: number,
    startDate: string,
    endDate: string,

    categoryID: number,
    categoryName: string,
    quantity:number
} 


export type CartProduct = {
    productID: number,
    quantity: number,
    isSelected: boolean
}

export type UsernamePasswordAuthentication = {
    username: string,
    password: string
}
export type RegisterInfo = {
    username: string,
    password: string,
    confirmPassword: string,
    email: string,
    firstname: string,
    lastname: string
}
// customer
export type Customer={
    customerID: string,
    contactName: string,
    address: string,
    city: string,
    phone: string,
    picture: string,
    email: string,
    username: string,
    userID: string
}
export type CustomerDetail={
    customerID: string,
    userID: string,
    username: string,
    email: string,
    firstName: string,
    lastName: string,
    contactName: string,
    address: string,
    ward: string,
    district: string,
    city: string,
    phone: string,
    picture: string
}
export type CustomerAdminDetail={
    customerID: string,
    userID: string,
    username: string,
    email: string,
    firstName: string,
    lastName: string,
    contactName: string,
    address: string,
    city: string,
    district: string,
    ward: string,
    phone: string,
    picture: string,
    emailVerified: boolean,
    enabled: boolean
}
export type CustomerUserInfo={
    customerID: string,
    contactName: string,
    picture: string,
    userInfo:UserInfo
}

// employee
export type Employee = {
    employeeID: string,
    firstName: string,
    lastName: string,
    birthDate: string,
    hireDate: string,
    phone: string,
    address: string,
    city: string,
    picture: string,
    email: string,
    username: string,
    userID: string
}
export type EmployeeDetail = {
    employeeID: string,
    userID: string,
    username: string,
    email: string,
    firstName: string,
    lastName: string,
    birthDate: string,
    hireDate: string,
    address: string,
    city: string,
    district: string,
    ward: string,
    phone: string,
    picture: string,
    title: string,
    notes: string,
}
export type EmployeeAdminDetails = {
    employeeID: string,
    userID: string,
    username: string,
    email: string,
    firstName: string,
    lastName: string,
    birthDate: string,
    hireDate: string,
    address: string,
    city: string,
    district: string,
    ward: string,
    phone: string,
    picture: string,
    title: string,
    notes: string,
    enabled: boolean,
    emailVerified: boolean
}
// pageable
export type Pageable = {
    first: boolean,
    last: boolean,
    number: number,
    totalPages: number

}
// notification
export type Notification = {
    notificationID: string,
    title: string,
    message: string,
    picture: string,
    senderID: string,
    receiverID: string,
    topic: string,
    status: string,
    isRead: boolean,
    time: string,
    isShowed: boolean,
}
export type NotificationSummary = {
    totalOfUnreadNotifications: number
}
// order
export type Order = {
    orderID: string,
    products: OrderProduct[],
    totalPrice: number,
    status: string
}
export type OrderProduct = {
    productID: number,
    productName: string,
    unitPrice: number,
    quantity: number,
    discount: number,
    extendedPrice: number,
    picture: string
}
export type OrderInfo = {
    orderID: number,
    orderDate: string,
    requiredDate: string,
    shippedDate: string,
    customerID: string,
    freight: number,
    salesPerson: string,
    shipAddress: string,
    shipCity: string,
    shipDistrict: string,
    shipWard: string,
    shipName: string,
    shipperName: String,
    phone: string,
    status: string,
    totalPrice: number,
    method: string,
    products: ProductWithDiscount[],
    paymentMethod: string
}

export type OrderWithProduct = {
    orderID: number,
    orderDate: string,
    requiredDate: string,
    shippedDate: string,
    customerID: string,
    contactName: string,
    employeeID: string,
    salesPerson: string,
    freight: number,
    shipName: string,
    shipAddress: string,
    shipCity: string,
    shipDistrict: string,
    shipWard: string,
    shipVia: number,
    shipperName: string,
    shipperPhone: string,
    phone: string,
    status: string,
    totalPrice: number
    products: ProductWithDiscount[]
    method: string
}
export type OrderItem = {
    productID: number,
    productName: string,
    quantity: number,
    picture: string,
    unitPrice: number,
    extendedPrice: number,
    discounts: DiscountInfo[]
}
export type OrderDetail = {
    orderID: number,
    customerID: string,
    contactName: string,
    picture: string,
    status: string,
    totalPrice: number,
    products: ProductWithDiscount[]
    freight: number,
}
export type OrderSummary = {
    totalPendingOrders: number
}
// discount
export type DiscountInfo = {
    discountID: string,
    discountPercent: number,
    discountCode: string,
    discountType: string,
    appliedDate: string
}
export type ProductWithDiscount = {
    productID: number,
    productName: string,
    categoryName: string,
    quantity: number,
    totalDiscount: number,
    unitPrice: number,
    unitsInStock: number,
    picture: string,
    extendedPrice: number,
    discounts: DiscountBriefInfo[]
}
export type DiscountBriefInfo = {
    discountID: string,
    discountPercent: number,
    discountType: string
}

// user
export type UserAccount = {
    userID: string,
    username: string,
    email: string,
    firstName: string,
    lastName: string,
    picture: string,
    enabled: boolean,
    emailVerified: boolean
}
export type UserInfo = {
    user: User,
    roles: string[]
}
export type User = {
    userID: string,
    username: string,
    email: string,
}
export type VerificationInfo = {
    phoneVerified: Boolean,
    profileVerified: Boolean,
    emailVerified: Boolean
}
// role
export type Role = {
    roleID: number,
    roleName: string
}
// response
export type PaymentResponse = {
    message: string,
    status: boolean,
    redirect_url: string
}
// province
export type Province = {
    ProvinceID: string,
    ProvinceName: string,
    NameExtension: string[],
    CreatedAt: string,
    UpdatedAt: string,
    CanUpdateCOD: boolean,
    Status: string,
}
// district
export type District = {
    DistrictID: string,
    ProvinceID: string,
    DistrictName: string,
    SupportType: number,
    NameExtension: string[],
    CanUpdateCOD: boolean,
    Status: boolean,
    CreatedDate: string,
    UpdatedDate: string,
}
// ward
export type Ward = {
    WardCode: string,
    DistrictID: string,
    WardName: string,
    NameExtension: string[],
    CanUpdateCOD: boolean,
    SupportType: number,
    Status: number,
    CreatedDate: string,
    UpdatedDate: string,
}
export type Location = {
    address: string,
    district: string,
    ward: string,
    city: string
    districtID: string,
    wardCode: string,
    cityID: string
}
// revenue
export type Revenue = {
    month: number,
    total: number
}
// payment status percentage
export type PaymentStatusPercentage = {
    status: string,
    percentage: number
}
// credit card
export type CreditCard = {
    name: string,
    number: string,
    expirationDate: string,
    securityCode: string,
    userID: string
}