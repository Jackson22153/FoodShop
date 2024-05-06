import './Cart.css';
import CartCard from '../../../shared/functions/cartCard/CartCard';
import { useEffect, useState } from 'react';
import { CartProduct, OrderInfo, ProductWithDiscount } from '../../../../model/Type';
import { addProductToCart, deleteProductToCart, getProductsFromCart } from '../../../../api/CartApi';
import { ceilRound } from '../../../../service/convert';
import { orderPath } from '../../../../constant/FoodShoppingURL';

export default function CartComponent(){
    const [order, setOrder] = useState<OrderInfo>();
    const [totalItems, setTotalItems] = useState(0);
    const [totalPrice, setTotalPrice] = useState(0);

    useEffect(()=>{
        initial();
    }, []);

    // click to place order
    const onClickPlaceOrder = ()=>{
        window.location.href=orderPath;
    }

    // calculate totalprice
    async function calculateTotalPrice(products: ProductWithDiscount[]){
        var totalPrice = 0;
        products.forEach(product =>{
            var totalDiscount = 0;
            product.discounts.forEach((discount) => {
                totalDiscount+=discount.discountPercent;
            });
            totalPrice += product.unitPrice*product.quantity*(1-totalDiscount/100);
        })
        setTotalPrice(totalPrice)
    }

    function onClickIncrementQuantity(index: number){
        if(order){
            const products = order.products;
            const quantity = products[index].quantity;
            const unitsInStock = products[index].unitsInStock
            // const newValue = quantity+1<=unitsInStock?quantity+1:unitsInStock;        
            if(quantity+1<=unitsInStock){
                
                products[index].quantity = quantity+1;
                setOrder({...order, products: products})
                const cartProduct = {
                    productID: products[index].productID,
                    quantity: 1
                }
                onClickAddToCart(cartProduct);

                calculateTotalPrice(products)
            }
        }
    }
    
    function onClickDecrementQuantity(index: number){
        if(order){
            const products = order.products;
            const quantity = products[index].quantity;
            // const newValue = quantity-1>1?quantity-1:1;
            if(quantity-1>=1){
                products[index].quantity = quantity-1;
                setOrder({...order, products: products})
                const cartProduct = {
                    productID: products[index].productID,
                    quantity: -1
                }
                onClickAddToCart(cartProduct);
                calculateTotalPrice(products)
            }
        }
    }
    async function onClickRemoveCartProduct(index: number){
        if(order){
            const products = order.products;
            const res = await deleteProductToCart(products[index].productID);
            if(res){
              window.location.reload()  
            }
        }
    }

    async function onClickAddToCart(cartProduct: CartProduct){
        const res = await addProductToCart(cartProduct);
        if(res.status){
            // console.log(res.data)
        }
    }

    const onChangeQuantityProduct = (event: any, index: number)=>{
        if(order){
            const quantity = event.target.value;
            const products = order.products;
            products[index] = {
                ...products[index], quantity: quantity
            }
            setOrder({...order, products: products})
        }
    }
    
    function initial(){
        fetchProductsInCart();
    }

    async function fetchProductsInCart(){
        const res = await getProductsFromCart();
        if(res.status){
            const data = res.data;
            console.log(data);
            setOrder(data);
            getTotalItems(data);
            setTotalPrice(data.totalPrice);
        }
    }

    function getTotalItems(order: OrderInfo){
        var totalItems = 0;
        if(order){
            order.products.forEach((_product) =>{
                totalItems+=1;
            })
            setTotalItems(totalItems)
        }
    }

    return(
        <section className="h-100 h-custom cart-section">
            <div className="container py-5 h-100">
                <div className="row d-flex justify-content-center align-items-center h-100">
                    <div className="col-12">
                        <div className="card card-registration card-registration-2">
                            <div className="card-body p-0">
                                <div className="row g-0">
                                    {order &&
                                        <>
                                            <div className="col-lg-8">
                                                <div className="p-5">
                                                    <div className="d-flex justify-content-between align-items-center mb-5">
                                                        <h1 className="fw-bold mb-0 text-black">Shopping Cart</h1>
                                                        <h6 className="mb-0 text-muted">{totalItems} items</h6>
                                                    </div>
                                                    <hr className="my-4"/>
                                                    {order.products.map((product, index)=>(
                                                        <div key={product.productID}>
                                                            <div  className="row mb-4 d-flex justify-content-between align-items-center">
                                                                <CartCard product={product} number={index}
                                                                    onChangeQuantity={onChangeQuantityProduct}
                                                                    onClickDecrementQuantity={onClickDecrementQuantity}
                                                                    onClickIncrementQuantity={onClickIncrementQuantity}
                                                                    onClickRemoveProduct={onClickRemoveCartProduct}
                                                                />
                                                            </div>
        
                                                            <hr className="my-4"/>
                                                        </div>
                                                    ))}
                                                    {/* <div className="pt-5">
                                                        <h6 className="mb-0"><a href="#!" className="text-body">
                                                            <FontAwesomeIcon icon={faLongArrowAltLeft}/> Back to shop</a>
                                                        </h6>
                                                    </div> */}
                                                </div>
                                            </div>
                                            <div className="col-lg-4 bg-grey">
                                                <div className="p-5">
                                                    <h3 className="fw-bold mb-5 mt-2 pt-1">Summary</h3>
                                                    <hr className="my-4"/>

                                                    <div className="d-flex justify-content-between mb-4">
                                                        <h5 className="text-uppercase">items {totalItems}</h5>
                                                        <h5>$ {ceilRound(totalPrice)}</h5>
                                                    </div>

                                                    {/* <h5 className="text-uppercase mb-3">Shipping</h5> */}

                                                    {/* <div className="mb-4 pb-2">
                                                        <select className="select">
                                                            <option value="1">Standard-Delivery- $5.00</option>
                                                            <option value="2">Two</option>
                                                            <option value="3">Three</option>
                                                            <option value="4">Four</option>
                                                        </select>
                                                    </div> */}

                                                    {/* <h5 className="text-uppercase mb-3">Give code</h5>

                                                    <div className="mb-5">
                                                        <div className="form-outline">
                                                            <input type="text" id="form3Examplea2" className="form-control form-control-lg"/>
                                                            <label className="form-label" htmlFor="form3Examplea2">Enter your code</label>
                                                        </div>
                                                    </div> */}

                                                    <hr className="my-4"/>

                                                    <div className="d-flex justify-content-between mb-5">
                                                        <h5 className="text-uppercase">Total price</h5>
                                                        <h5>$ {ceilRound(totalPrice + order.freight)}</h5>
                                                    </div>
                                                    <button type="button" className="btn btn-dark btn-block btn-lg"
                                                        onClick={onClickPlaceOrder} disabled={order.products.length>0?false:true}
                                                        data-mdb-ripple-color="dark">
                                                            Order
                                                    </button>
                                                </div>
                                            </div>
                                        </>
                                    }
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}