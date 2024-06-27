import { useContext, useEffect, useState } from "react";
import { OrderDetail, Pageable } from "../../../../../model/Type";
import { ORDER_STATUS } from "../../../../../constant/WebConstant";
import PaginationSection from "../../../../shared/website/sections/paginationSection/PaginationSection";
import { getPageNumber } from "../../../../../service/Pageable";
import { CUSTOMER_ORDER } from "../../../../../constant/FoodShoppingURL";
import { displayProductImage } from "../../../../../service/Image";
import { getCustomerOrders, receiveOrder } from "../../../../../api/OrderApi";
import notificationMessagesContext from "../../../../contexts/NotificationMessagesContext";
import { ModalContextType } from "../../../../../model/WebType";
import modalContext from "../../../../contexts/ModalContext";
import { Link, useSearchParams } from "react-router-dom";

export default function UserOrdersComponent(){
    const [listOrders, setListOrders] = useState<OrderDetail[]>([])
    const notificationMessage = useContext(notificationMessagesContext)
    const {setModal: setErrorModal} = useContext<ModalContextType>(modalContext);
    const [searchParams] = useSearchParams()
    const orderParam = searchParams.get("order")!=null? searchParams.get("order").toLowerCase():ORDER_STATUS.PENDING
    const [page, setPage] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    })

    useEffect(()=>{
        initial();
    }, [notificationMessage, orderParam])

    function initial(){
        const pageNumber = getPageNumber();
        fetchOrdersBasedOnType(pageNumber);
    }

    function fetchOrdersBasedOnType(pageNumber: number){
        switch (orderParam){
            // pending orders
            case ORDER_STATUS.PENDING:{
                fetchOrders(pageNumber, ORDER_STATUS.PENDING);
                break;
            }
            // confirmed orders
            case ORDER_STATUS.CONFIRMED:{
                fetchOrders(pageNumber, ORDER_STATUS.CONFIRMED);
                break;
            }
            // shipping orders
            case ORDER_STATUS.SHIPPING:{
                fetchOrders(pageNumber, ORDER_STATUS.SHIPPING);
                break;
            }
            // successful orders
            case ORDER_STATUS.SUCCESSFUL:{
                fetchOrders(pageNumber, ORDER_STATUS.SUCCESSFUL);
                break;
            }
            // canceled orders
            case ORDER_STATUS.CANCELED:{
                fetchOrders(pageNumber, ORDER_STATUS.CANCELED);
                break;
            }
            // all orders
            case ORDER_STATUS.ALL:{
                fetchOrders(pageNumber, ORDER_STATUS.ALL);
                break;
            }
        }
    }
    
    // get orders
    async function fetchOrders(pageNumber: number, type:string){
        try {
            const res = await getCustomerOrders(pageNumber, type)
            if(200<=res.status&&res.status<300){
                const data = res.data;
                setListOrders(data.content)
                setPage({
                    first: data.first,
                    last: data.last,
                    number: data.number,
                    totalPages: data.totalPages
                });
            }
        } catch (error) {
            setErrorModal({
                title: "Error", 
                isShowed: true, 
                message: error.response?error.response.data.error:error.message
            })
        }
    }

    // shipping order
    const onClickReceive = (order: OrderDetail)=>{
        customerReceiveOrder(order)
    }
    // cancel order
    const customerReceiveOrder = async (order: OrderDetail)=>{
        try {
            const data = {
                orderID: order.orderID,
                customerID: order.customerID
            }
            const res = await receiveOrder(data);
            if(200<=res.status&&res.status<300){
    
            }
        } catch (error) {
            
        } finally{
            window.location.reload();
        }
    }

    return(
        <>
            <ul className="nav nav-fill nav-tabs emp-profile p-0 mb-3 cursor-pointer box-shadow-default" 
                role="tablist">
                <li className="nav-item" role="presentation">
                    <Link to={`${CUSTOMER_ORDER}?order=${ORDER_STATUS.PENDING}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.PENDING ?'active':''}`}
                            id="pending-order-tab" role="tab">Pending Orders</span>
                    </Link>
                </li>
                <li className="nav-item" role="presentation">
                    <Link to={`${CUSTOMER_ORDER}?order=${ORDER_STATUS.CONFIRMED}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.CONFIRMED ?'active':''}`}
                            id="all-order-tab" role="tab">Confirmed Orders</span>
                    </Link>
                </li>
                <li className="nav-item" role="presentation">
                    <Link to={`${CUSTOMER_ORDER}?order=${ORDER_STATUS.SHIPPING}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.SHIPPING ?'active':''}`}
                            id="confirmed-order-tab" role="tab">Shipping Orders</span>
                    </Link>
                </li>
                <li className="nav-item" role="presentation">
                    <Link to={`${CUSTOMER_ORDER}?order=${ORDER_STATUS.SUCCESSFUL}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.SUCCESSFUL ?'active':''}`}
                            id="shipping-order-tab" role="tab">Successful Orders</span>
                    </Link>
                </li>
                <li className="nav-item" role="presentation">
                    <Link to={`${CUSTOMER_ORDER}?order=${ORDER_STATUS.CANCELED}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.CANCELED ?'active':''}`}
                            id="successful-order-tab" role="tab">Canceled Orders</span>
                    </Link>
                </li>
                <li className="nav-item" role="presentation">
                    <Link to={`${CUSTOMER_ORDER}?order=${ORDER_STATUS.ALL}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.ALL ?'active':''}`}
                            id="canceled-order-tab" role="tab">All Orders</span>
                    </Link>
                </li>
            </ul>
            <div className="tab-pane" id="orders-tabpanel" role="tabpanel" aria-labelledby="orders-tabpanel">
                {orderParam===ORDER_STATUS.SHIPPING ?
                    <ul className="list-group">
                        {listOrders.length>0 ?
                            listOrders.map((order) =>(
                                <li className="list-group-item cursor-default my-2 box-shadow-default py-3 order-item" key={order.orderID}>
                                    <div className="d-flex align-items-center">
                                        <p className="h6 mx-3">OrderID: #{order.orderID}</p>
                                    </div>
                                    <ul className="list-group">
                                        {order.products && order.products.map((product) =>(
                                            <li className="card d-flex flex-row my-md-1 my-sm-2" key={`${order.orderID}&${product.productID}`}>
                                                <div className="col-md-2 p-2">
                                                    <img className="order-img-thumbnail card-img-top rounded float-left" 
                                                        src={displayProductImage(product.picture)} alt="Card image cap"/>
                                                </div>
                                                <div className="card-body d-flex flex-row justify-content-between">
                                                    <div className="col-md-3">
                                                        <h5 className="card-title">{product.productName}</h5>
                                                        <p className="card-text">Quantity: {product.quantity}</p>
                                                    </div>
                                                    <div className="mx-4 col-md-2">
                                                        <p className="card-text">Price: {product.extendedPrice}</p>
                                                    </div>
                                                </div>
                                            </li>
                                        ))}
                                    </ul>
                                    <div className="card-footer mt-2">
                                        <div className="row justify-content-between">
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Status: {order.status}</p>
                                            </div>
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Total Price: {order.totalPrice}</p>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-end">
                                            <a href={`${CUSTOMER_ORDER}/${order.orderID}`} className="btn btn-info text-white">View order</a>
                                            <button className="btn btn-primary mx-2" onClick={(_e)=>onClickReceive(order)}>Receive Order</button>
                                        </div>
                                    </div>
                                </li>
                            )):
                            <li className="list-group-item order-item d-flex justify-content-center align-items-center box-shadow-default">
                                <h5>No orders available</h5>
                            </li>
                        }
                    </ul>:
                    <ul className="list-group">
                        {listOrders.length>0 ?
                            listOrders.map((order) =>(
                                <li className="list-group-item cursor-default my-2 box-shadow-default py-3 order-item" key={order.orderID}>
                                    <div className="d-flex align-items-center">
                                        <p className="h6 mx-3">OrderID: #{order.orderID}</p>
                                    </div>
                                    <ul className="list-group">
                                        {order.products && order.products.map((product) =>(
                                            <li className="card d-flex flex-row my-md-1 my-sm-2" key={`${order.orderID}&${product.productID}`}>
                                                <div className="col-md-2 p-2">
                                                    <img className="order-img-thumbnail card-img-top rounded float-left" 
                                                        src={displayProductImage(product.picture)} alt="Card image cap"/>
                                                </div>
                                                <div className="card-body d-flex flex-row justify-content-between">
                                                    <div className="col-md-3">
                                                        <h5 className="card-title">{product.productName}</h5>
                                                        <p className="card-text">Quantity: {product.quantity}</p>
                                                    </div>
                                                    <div className="mx-4 col-md-2">
                                                        <p className="card-text">Price: {product.extendedPrice}</p>
                                                    </div>
                                                </div>
                                            </li>
                                        ))}
                                    </ul>
                                    <div className="card-footer mt-2">
                                        <div className="row justify-content-between">
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Status: {order.status}</p>
                                            </div>
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Total Price: {order.totalPrice}</p>
                                            </div>
                                        </div>
                                        <div className="row d-flex justify-content-end">
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <a href={`${CUSTOMER_ORDER}/${order.orderID}`} className="btn btn-primary">View order</a>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            )):
                            <li className="list-group-item order-item d-flex justify-content-center align-items-center box-shadow-default">
                                <h5>No orders available</h5>
                            </li>
                        }
                    </ul>
                }
                
                <div className="pagination position-relative justify-content-center my-3">
                    <PaginationSection pageable={page}/>
                </div>
            </div>
        </>
    );
}