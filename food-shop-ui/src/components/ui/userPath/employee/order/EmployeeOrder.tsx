import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom"
import { OrderWithProduct } from "../../../../../model/Type";
import { displayProductImage } from "../../../../../service/Image";
import dayjs from "dayjs";
import { getOrderDetail } from "../../../../../api/OrderApi";
import { ModalContextType } from "../../../../../model/WebType";
import modalContext from "../../../../contexts/ModalContext";
import ScrollToTop from "../../../../shared/functions/scroll-to-top/ScrollToTop";
import { ceilRound } from "../../../../../service/Convert";

export default function EmployeeOrderComponent(){
    const { orderId } = useParams();
    const [orderInfo, setOrderInfo] = useState<OrderWithProduct>();
    const {setModal: setErrorModal} = useContext<ModalContextType>(modalContext);

    useEffect(()=>{
        fetchOrder()
    }, [])

    const fetchOrder = async ()=>{
        try {
            const res = await getOrderDetail(orderId);
            if(res.status){
                const data = res.data;
                // console.log(data);
                setOrderInfo(data);
            }
        } catch (error) {
            setErrorModal({
                title: "Error", 
                isShowed: true, 
                message: error.response?error.response.data.error:error.message
            })
        }
    }

    return(
        <div id="order-bill-container">
            <ScrollToTop/>
            {orderInfo &&
                <div className="box-shadow-default rounded-5 col-sm-12 col-md-10 mx-auto col-lg-7">
                    <div className="d-flex flex-column justify-content-center align-items-center position-relative pt-3 rounded-top-5" id="order-heading">
                        <div>
                            <h3>Order detail</h3>
                        </div>
                        <div className="h5">{dayjs(orderInfo.orderDate).toString()}</div>
                        <div className="pt-1">
                            {orderInfo.shippedDate?
                                <p>Order <b>#{orderInfo.orderID}</b> was delivered on <b className="text-dark"> {dayjs(orderInfo.shippedDate).toString()}</b></p>:
                                <p>Order <b>#{orderInfo.orderID}</b> is <b className="text-dark"> {orderInfo.status}</b></p>
                            }
                        </div>
                        {/* <div className="btn close text-white">
                            &times;
                        </div> */}
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
                                <div>{orderInfo.method.toUpperCase()}</div>
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
                        <div className="d-flex justify-content-start align-items-center pl-3 py-3 mb-4 border-bottom">
                            <div className="text-muted">
                                Today's Total
                            </div>
                            <div className="ml-auto h5 font-weight-bold">
                                ${ceilRound(orderInfo.totalPrice + orderInfo.freight)}
                            </div>
                        </div>
                        <div className="row border rounded p-1 my-3 d-flex">
                            <div className="col-md-6 py-3">
                                <div className="d-flex flex-column align-items start">
                                    <div>
                                        <b>Employee</b>
                                        <p className="text-justify pt-2">{orderInfo.salesPerson}</p>
                                    </div>
                                    <div className="d-flex justify-content-start align-items-center pb-4 pl-3 border-bottom"/>
                                    <div className= 'py-3'>
                                        <b>Ship</b>
                                        <p className="text-justify pt-2">{orderInfo.shipperName}</p>
                                        <p className='text-justify'>{orderInfo.shipperPhone}</p>
                                    </div>
                                </div>
                            </div>
                            <div className="col-md-6 py-3">
                                <div className="d-flex flex-column align-items start">
                                    <b>Shipping Address</b>
                                    <p className="text-justify pt-2">{orderInfo.shipName}, {orderInfo.shipAddress}</p>
                                    <p className="text-justify">{`${orderInfo.shipWard}, ${orderInfo.shipDistrict}, ${orderInfo.shipCity}`}</p>
                                    <p className="text-justify"><b>Phone:</b> {orderInfo.phone}</p>
                                </div>
                            </div>
                        </div>
                        {/* <div className="pl-3 font-weight-bold">Related Subsriptions</div> */}
                        <div className="d-sm-flex justify-content-between rounded my-3 subscriptions">
                            <div>{orderInfo.orderDate}</div>
                            <div>Status: <b>{orderInfo.status}</b></div>
                            <div>
                                Total: <b> ${ceilRound(orderInfo.totalPrice + orderInfo.freight)}</b>
                            </div>
                        </div>
                    </div>
                </div>
            }
        </div>
    )
}