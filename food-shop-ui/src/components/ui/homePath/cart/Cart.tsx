import './Cart.css';
import CartCard from '../../../shared/functions/cartCard/CartCard';
import { useEffect, useState } from 'react';
import { CartInfo, CartProduct, CartProductInfo } from '../../../../model/Type';
import { deleteAllCartProducts, deleteCartProduct, getProductsFromCart, updateProductInCart } from '../../../../api/CartApi';
import { ceilRound } from '../../../../service/Convert';
import { ORDER_PATH } from '../../../../constant/FoodShoppingURL';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Modal } from '../../../../model/WebType';
import ModalComponent from '../../../shared/functions/modal/Modal';
import { useNavigate } from 'react-router-dom';

export default function CartComponent(){
    const navigate = useNavigate()
    const [cartOrder, setCartOrder] = useState<CartInfo>();
    const [totalItems, setTotalItems] = useState(0);
    const [totalPrice, setTotalPrice] = useState(0);
    const [isSelectedAllItems, setIsSelectedAllItems] = useState(false);
    const [removeAllModal, setRemoveAllModal] = useState<Modal>({
        title: "Remove all products",
        message: "Do you want to remove all products ?",
        isShowed: false
    })
    useEffect(()=>{
        initial();
    }, []);

    function initial(){
        fetchProductsInCart();
    }

    // click to place order
    const onClickPlaceOrder = ()=>{
        navigate(ORDER_PATH);
    }

    // calculate totalprice
    async function calculateTotalPrice(products: CartProductInfo[]){
        var totalPrice = 0;
        products.forEach(product =>{
            if(product.isSelected){
                var totalDiscount = 0;
                product.discounts.forEach((discount) => {
                    totalDiscount+=discount.discountPercent;
                });
                totalPrice += product.unitPrice*product.quantity*(1-totalDiscount/100);
            }
        })
        setTotalPrice(totalPrice)
    }

    // increase product's quantity
    function onClickIncrementQuantity(index: number){
        if(cartOrder){
            const products = cartOrder.products;
            const quantity = products[index].quantity;
            const unitsInStock = products[index].unitsInStock   
            if(quantity+1<=unitsInStock){
                
                products[index].quantity = quantity+1;
                setCartOrder({...cartOrder, products: products})
                const cartProduct = {
                    productID: products[index].productID,
                    quantity: quantity+1,
                    isSelected: products[index].isSelected
                }
                updateCart([cartProduct]);

                calculateTotalPrice(products)
            }
        }
    }
    
    // decrease product's quantity
    function onClickDecrementQuantity(index: number){
        if(cartOrder){
            const products = cartOrder.products;
            const quantity = products[index].quantity;
            // const newValue = quantity-1>1?quantity-1:1;
            if(quantity-1>=1){
                products[index].quantity = quantity-1;
                setCartOrder({...cartOrder, products: products})
                const cartProduct = {
                    productID: products[index].productID,
                    quantity: quantity-1,
                    isSelected: products[index].isSelected
                }
                updateCart([cartProduct]);
                calculateTotalPrice(products)
            }
        }
    }

    // remove product from cart
    async function onClickRemoveCartProduct(index: number){
        if(cartOrder){
            const products = cartOrder.products;
            const productID = products[index].productID
            const updatedProducts = products.filter(p => p.productID!=productID);
            calculateTotalPrice(updatedProducts)
            getTotalItems(updatedProducts)
            removeCartProduct(productID);
        }
    }
    const removeCartProduct = async (productID: number)=>{
        const res = await deleteCartProduct(productID);
        if(200<=res.status&&res.status<300){
            const data = res.data;
            setCartOrder(data)
        }
    }
    // remove all products
    function onClickRemoveAllCartProducts(){
        if(cartOrder){
            setRemoveAllModal(modal => ({...modal, isShowed: true}))
        }
    }

    const removeAllCartProducts = async ()=>{
        const res = await deleteAllCartProducts();
        if(200<=res.status&&res.status<300){
            setCartOrder(res.data)
        }
    }
    // add product to cart
    async function updateCart(cartProducts: CartProduct[]){
        const res = await updateProductInCart(cartProducts);
        if(200<=res.status && res.status<300){
            console.log(res.data)
            const data = res.data;
            setCartOrder(data)
        }
    }

    // change product's quantity
    const onChangeQuantityProduct = (event: any, index: number)=>{
        if(cartOrder){
            const quantity = event.target.value;
            const products = cartOrder.products;
            products[index] = {
                ...products[index], quantity: quantity
            }
            setCartOrder({...cartOrder, products: products})
        }
    }

    // get products in cart
    async function fetchProductsInCart(){
        const res = await getProductsFromCart();
        if(200<=res.status&&res.status<300){
            const data = res.data;
            setCartOrder(data);
            getTotalItems(data.products);
            setTotalPrice(data.totalPrice);
        }
    }
    // get total products in cart
    function getTotalItems(products: CartProductInfo[]){
        var totalItems = 0;
        Array.from(products).forEach((product) =>{
            if(product.isSelected)
                totalItems+=1;
        })
        // check selected items
        if(totalItems===products.length && totalItems>0){
            setIsSelectedAllItems(true)
        }else if(totalItems < products.length){
            setIsSelectedAllItems(false)
        }
        setTotalItems(totalItems)
    }

    // change selected product
    const onChangeCheckedProduct = (index: number) => {
        // event.preventDefault();
        // Create a new array with updated product
        const updatedProducts = cartOrder.products.map((product, i) =>
            i === index ? { ...product, isSelected: !product.isSelected } : product
        );

        // Set the new array to state
        setCartOrder(prevCartOrder => ({
            ...prevCartOrder,
            products: updatedProducts
        }));
        // update total items 
        getTotalItems(updatedProducts)
        // calculate order price
        calculateTotalPrice(updatedProducts)
        // update cart
        const updatedCartProduct = {
            productID: updatedProducts[index].productID,
            quantity: updatedProducts[index].quantity,
            isSelected: updatedProducts[index].isSelected
        }
        updateCart([updatedCartProduct])
    }

    const onChangeCheckedProducts = () => {
        if(cartOrder.products.length>0){
            const updatedProducts = cartOrder.products.map((product) =>{
               const updatedProduct = {
                productID: product.productID,
                quantity: product.quantity,
                isSelected: !isSelectedAllItems
               }
               return updatedProduct
            });
            const updatedProductInfos = cartOrder.products.map((product) =>{
                const updatedProduct = {
                    ...product, isSelected: !isSelectedAllItems
                }
                return updatedProduct
             });
            setCartOrder({...cartOrder, products: updatedProductInfos})
            getTotalItems(updatedProductInfos)
            updateCart(updatedProducts)
            calculateTotalPrice(updatedProductInfos)
            setIsSelectedAllItems(!isSelectedAllItems)
        }
    }

    // remove all button
    const onClickCloseModal = ()=>{
        setRemoveAllModal(modal => ({...modal, isShowed: false}))
    }
    const onClickConfirmModal = ()=>{
        if(cartOrder){
            removeAllCartProducts();
            setTotalPrice(0)
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
                                    {cartOrder &&
                                        <>
                                            <div className="col-lg-8">
                                                <div className="p-5">
                                                    <div className="d-flex justify-content-between align-items-center mb-5">
                                                        <h1 className="fw-bold mb-0 text-black">Shopping Cart</h1>
                                                        <h6 className="mb-0 text-muted">{totalItems} {totalItems!=1?'items':'item'}</h6>
                                                    </div>
                                                    <div className="row d-flex justify-content-between align-items-center">
                                                        <input className="form-check-input cart-item-check-input" type="checkbox" 
                                                            checked={isSelectedAllItems} onChange={onChangeCheckedProducts}/>
                                                        <div className="col-md-2 col-lg-2 col-xl-2">
                                                            <span>All</span>
                                                        </div>
                                                        <div className="col-md-3 col-lg-3 col-xl-3">
                                                            <span>Product</span>
                                                        </div>
                                                        <div className="col-md-3 col-lg-3 col-xl-2 d-flex p-0 justify-content-center">
                                                            <span>Quantity</span>
                                                        </div>
                                                        <div className="col-md-3 col-lg-2 col-xl-2 offset-lg-1">
                                                            <span>Price</span>
                                                        </div>
                                                        <div className="col-md-1 col-lg-1 col-xl-1 text-end">
                                                            <button className="text-muted btn btn-light" onClick={onClickRemoveAllCartProducts}>
                                                                <FontAwesomeIcon icon={faTrash}/>
                                                            </button>
                                                        </div>
                                                    </div>
                                                    <hr className="my-4"/>
                                                    {cartOrder.products.map((product, index)=>(
                                                        <div className='cart-item' key={index}>
                                                            <div className="row mb-4 d-flex justify-content-between align-items-center">
                                                                <input className="form-check-input cart-item-check-input" type="checkbox" 
                                                                    checked={product.isSelected} onChange={(_e)=>onChangeCheckedProduct(index)}/>
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
                                                        <h5>$ {ceilRound(totalPrice + cartOrder.freight)}</h5>
                                                    </div>
                                                    <button type="button" className="btn btn-dark btn-block btn-lg"
                                                        onClick={onClickPlaceOrder} disabled={cartOrder.products.length>0?false:true}
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
            <ModalComponent modal={removeAllModal} handleConfirmButton={onClickConfirmModal} 
                handleCloseButton={onClickCloseModal}/>
        </section>
    );
}