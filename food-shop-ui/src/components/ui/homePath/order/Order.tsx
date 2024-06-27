import { ChangeEventHandler, useContext, useEffect, useState } from "react"
import { OrderInfo } from "../../../../model/Type"
import { displayProductImage, getError } from "../../../../service/Image";
import { getOrder } from "../../../../api/CartApi";
import { faLongArrowAltLeft } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { CART_PATH } from "../../../../constant/FoodShoppingURL";
import { Notification } from "../../../../model/Type";
import { NOTIFICATION_TYPE } from "../../../../constant/WebConstant";
import userInfoContext from "../../../contexts/UserInfoContext";
import { placeOrder } from "../../../../api/OrderApi";
import { Link } from "react-router-dom";
import ScrollToTop from "../../../shared/functions/scroll-to-top/ScrollToTop";

export default function OrderComponent(){
    const [orderInfo, setOrderInfo] = useState<OrderInfo>();
    const userInfo = useContext(userInfoContext);
    const [notification, setNotification] = useState<Notification>({
        notificationID: '',
        title: '',
        message: '',
        senderID: '',
        receiverID: '',
        topic: '',
        status: '',
        isRead: false,
        time: '',
        isShowed: false,
    })

    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        checkAuthenticationCustomer();
        fetchCartOrder();
    }
    // check customer
    async function checkAuthenticationCustomer(){
        // try {
        //     const res = await isCustomer();
        //     if(200<=res.status&&res.status<300){
        //         const data = res.data;
        //         const status = data.status;
        //         if(!status) window.location.href="/"
        //     }
        // } catch (error) {
        //     window.location.href="/"
        // }
    }

    // cart order
    async function fetchCartOrder(){
        try {
            const res = await getOrder();
            if(200<=res.status&&res.status<300){
                const data = res.data;
                setOrderInfo(data);
            }
        } catch (error) {
            var errorMessage = "An error has occurred";
            if (error.response) {
                // Retrieve the error message from the response body
                errorMessage = error.response.data.error;
            } else {
                errorMessage = error.message;
            }
            setNotification({...notification,
                message: errorMessage,
                status: NOTIFICATION_TYPE.ERROR,
                isShowed: true
            })
        }
    }

    const onChangeOrderInfo:ChangeEventHandler<any> = (event)=>{
        if(orderInfo){
            const name = event.target.name;
            const value = event.target.value;

            setOrderInfo({...orderInfo, [name]: value})
        }
    }
    // place an order
    const onClickOrder = ()=>{
        if(orderInfo){
            const products =orderInfo.products.map(product =>{
                const orderProduct = {
                    productID: product.productID,
                    quantity: product.quantity,
                    discounts: product.discounts
                };
                return orderProduct;
            })
            const data = {
                products: products,
                freight:10,
                shipName: orderInfo.shipName,
                shipAddress: orderInfo.shipAddress,
                shipCity: orderInfo.shipCity,
                phone: orderInfo.phone,
                shipVia: 1
            }
            userPlaceOrder(data);
        }
    }

    // place order
    async function userPlaceOrder(order: any){
        try {
            const res = await placeOrder(order);
            if(200<=res.status&&res.status<300){
                const data = res.data
                setNotification({...notification,
                    message: `Order has been placed successfully`,
                    status: NOTIFICATION_TYPE.SUCCESSFUL,
                    isShowed: true
                })
            }
        } catch (error) {
            setNotification({...notification,
                message: `Order can not be placed`,
                status: NOTIFICATION_TYPE.ERROR,
                isShowed: true
            })
        }
    }

    return(
        <div className="container my-5" id="checkout-order">
            <ScrollToTop/>
            {orderInfo &&
                <div className="box-shadow-default rounded-5 col-sm-12 col-md-10 mx-auto col-lg-7">
                    <div className="d-flex flex-column justify-content-center align-items-center position-relative pt-3 rounded-top-5" id="order-heading">
                        <div className="text-uppercase py-4">
                            <h4>Order detail</h4>
                        </div>
                    </div>
                    <div className="bg-white rounded-bottom-5 px-3 px-md-5 pb-4">
                        <div className="table-responsive">
                            <table className="table table-borderless">
                                <thead>
                                    <tr className="text-uppercase text-muted">
                                        <th scope="col">product</th>
                                        <th scope="col" className="text-right">total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <th scope="row">Food</th>
                                        <td className="text-right"><b>${orderInfo.totalPrice}</b></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        {orderInfo.products.map(product =>(
                            <div key={product.productID} className="d-flex justify-content-start align-items-center list py-1 px-2">
                                <div><b>{product.quantity}px</b></div>
                                <div className="mx-3">
                                    <img src={displayProductImage(product.picture)}
                                        alt="product" className="rounded-circle" width="30" height="30"/>
                                </div>
                                <div className=""><p className="m-0">{product.productName}</p></div>
                                
                                {product.totalDiscount>0 &&
                                    <div className="text-white d-flex justify-content-center align-items-center small-ele ms-4">
                                        -{product.totalDiscount}%
                                    </div>
                                }

                                <div className="text-right ml-auto">
                                    ${product.extendedPrice}
                                </div>
                            </div>
                        ))}

                        <div className="pt-2 border-bottom mb-3"></div>
                        <div className="d-flex justify-content-start align-items-center pl-3">
                            <div className="text-muted">Payment Method</div>
                            <div className="ml-auto">
                                <div>COD</div>
                                {/* <img src="https://www.freepnglogos.com/uploads/mastercard-png/mastercard-logo-logok-15.png" alt=""
                                    width="30" height="30"/>
                                <label>Mastercard ******5342</label> */}
                            </div>
                        </div>
                        <div className="d-flex justify-content-start align-items-center py-1 pl-3">
                            <div className="text-muted">Shipping</div>
                            <div className="ml-auto">
                                <label>${orderInfo.freight}</label>
                            </div>
                        </div>
                        <div className="d-flex justify-content-start align-items-center pb-4 pl-3 border-bottom">
                            {/* <div className="text-muted">
                                <button className="text-white btn">50% Discount</button>
                            </div>
                            <div className="ml-auto price">
                                -$34.94
                            </div> */}
                        </div>
                        <div className="d-flex justify-content-start align-items-center pl-3 py-3 mb-4 border-bottom">
                            <div className="text-muted">
                                Today's Total
                            </div>
                            <div className="ml-auto h5 font-weight-bold">
                                ${orderInfo.totalPrice + orderInfo.freight}
                            </div>
                        </div>
                        <div className="row border rounded p-1 my-3 d-flex">
                            <div className="col-md-12 py-3">
                                <div className="d-flex flex-column align-items start">
                                    <b>Shipping Address</b>
                                    <div className="row">
                                        <div className="col-md-5">
                                            <div className="form-group">
                                                <label htmlFor="ship-name-orderinfo">Contact Name</label>
                                                <input type="text" className="form-control" id="ship-name-orderinfo" 
                                                    placeholder="Enter your contact name" value={orderInfo.shipName} 
                                                    onChange={onChangeOrderInfo} name="shipName" required/>
                                            </div>
                                        </div>
                                        <div className="col-md-5">
                                            <div className="form-group">
                                                <label htmlFor="phone-orderinfo">Phone</label>
                                                <input type="text" className="form-control" id="phone-orderinfo" 
                                                    placeholder="Enter your city" value={orderInfo.phone} required
                                                    onChange={onChangeOrderInfo} name="phone"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="row">
                                        <div className="col-md-10">
                                            <div className="form-group">
                                                <label htmlFor="ship-address-orderinfo">Address</label>
                                                <input type="text" className="form-control" id="ship-address-orderinfo" 
                                                    placeholder="Enter your address" value={orderInfo.shipAddress} required
                                                    onChange={onChangeOrderInfo} name="shipAddress"/>
                                            </div>
                                        </div>
                                        <div className="col-md-4">
                                            <div className="form-group">
                                                <label htmlFor="ship-city-orderinfo">City</label>
                                                <input type="text" className="form-control" id="ship-city-orderinfo" 
                                                    placeholder="Enter your city" value={orderInfo.shipCity} required
                                                    onChange={onChangeOrderInfo} name="shipCity"/>
                                            </div>
                                        </div>
                                    </div> 
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-3">
                                <div className="pt-2">
                                    <h6 className="mb-0"> 
                                        <Link to={CART_PATH}>
                                            <div className="text-body">
                                                <FontAwesomeIcon icon={faLongArrowAltLeft}/> Back to shop
                                            </div>
                                        </Link>
                                    </h6>
                                </div>
                            </div>
                            <div className="col-6">
                                <button onClick={onClickOrder} className="btn btn-primary">Order</button>
                            </div>
                        </div>
                    </div>
                </div>
            }
            {notification.isShowed &&
                <div className="screen-center vh-100 d-flex justify-content-center align-items-center">
                    <div className="card col-md-4 bg-white shadow-md p-5">
                        <div className="mb-4 text-center">
                            {notification.status.toLowerCase()===NOTIFICATION_TYPE.SUCCESSFUL.toLowerCase() ?
                                <svg xmlns="http://www.w3.org/2000/svg" className="text-success bi bi-check-circle" width="75" height="75"
                                    fill="currentColor" viewBox="0 0 16 16">
                                    <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z" />
                                    <path
                                        d="M10.97 4.97a.235.235 0 0 0-.02.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-1.071-1.05z" />
                                </svg>:
                                <img width={"200px"} src={getError()} alt="Error" />
                            }
                        </div>
                        <div className="text-center">
                            <h1>{notification.status.toLowerCase()===NOTIFICATION_TYPE.SUCCESSFUL.toLowerCase()?'Thank You !': 'Error'}</h1>
                            <p>{notification.message}</p>
                            <Link to={"/"}>
                                <span className="btn btn-outline-success">Back Home</span>
                            </Link>
                        </div>
                    </div>
                </div>
            }    
        </div>
    )
}