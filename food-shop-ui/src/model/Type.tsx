export type Category={
    categoryID: number,
    categoryName: string,
    description: string,
    picture: string
}
export type Product={
    productID: number,
    productName: string,
    quantityPerUnit: number,
    unitPrice: number,
    unitsInStock: number,
    unitsOnOrder: number,
    reorderLevel: number,
    discontinued: boolean,
    picture: string
}
export type ProductDetails={
    productID: number,
    productName: string,
    quantityPerUnit: number,
    unitPrice: number,
    unitsInStock: number,
    unitsOnOrder: number,
    reorderLevel: number,
    discountAmount: number,
    startDate: string,
    endDate: string,
    discontinued: boolean,
    picture: string
    categoryID: number,
    categoryName: string,
    supplierID: number,
    companyName: string
}
export type Discount = {
    discountID: number,
    discountAmount: number,
    startDate: string,
    endDate: string,
    product: Product
}
export type CartProduct = {
    productID: number,
    productName: string,
    discount: number,
    quantity: number
}
export type Supplier = {
    supplierID: number,
    companyName: string,
    contactName: string,
    contactTitle: string,
    address: string,
    city: string,
    region: string,
    postalCode: string,
    country: string,
    phone: string,
    fax: string,
    homePage: string
}
export type Customer={
    customerID: string,
    contactName: string,
    address: string,
    city: string,
    phone: string,
    picture: string,
    email: string,
    username: string
}
export type Pageable = {
    first: boolean,
    last: boolean,
    number: number,
    totalPages: number

}
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
    quantity: number,
    totalDiscount: number,
    unitPrice: number,
    picture: string,
    extendedPrice: number,
    discounts: DiscountBriefInfo[]
}
export type DiscountBriefInfo = {
    discountID: string,
    discountPercent: number,
    discountType: string
}
export type Employee = {
    employeeID: string,
    firstName: string,
    lastName: string,
    birthDate: string,
    hireDate: string,
    homePhone: string,
    address: string,
    city: string,
    photo: string,
    reportsTo: string,
    title: string,
    email: string,
    username: string
}
export type UserInfo = {
    userID: string,
    username: string,
    isAuthenticated: boolean
}