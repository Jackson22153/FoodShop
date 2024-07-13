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



export type CartProduct = {
    productID: number,
    quantity: number,
    isSelected: boolean
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
    city: string,
    phone: string,
    picture: string
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
    phone: string,
    picture: string,
    title: string,
    notes: string,
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
    shipName: string,
    shipperName: String,
    phone: string,
    status: string,
    totalPrice: number
    products: ProductWithDiscount[]
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
    shipVia: number,
    shipperName: string,
    shipperPhone: string,
    phone: string,
    status: string,
    totalPrice: number
    products: ProductWithDiscount[]
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
    totalPrice: number
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
    picture: string
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
// role
export type Role = {
    roleID: number,
    roleName: string
}