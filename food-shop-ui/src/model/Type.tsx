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
    companyName: string,
    contactName: string,
    contactTitle: string,
    address: string,
    city: string,
    region: string,
    postalCode: string,
    country: string,
    phone: string,
    fax: string
}
export type Pageable = {
    first: boolean,
    last: boolean,
    number: number,
    totalPages: number

}
export type UserInfo = {
    username: string,
    isAuthenticated: boolean
}