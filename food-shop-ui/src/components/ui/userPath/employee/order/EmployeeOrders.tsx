import { useContext, useEffect, useRef, useState } from "react";
import { Notification, OrderDetail, OrderSummary, Pageable } from "../../../../../model/Type";
import PaginationSection from "../../../../shared/website/sections/paginationSection/PaginationSection";
import { getPageNumber } from "../../../../../service/Pageable";
import { displayProductImage, displayUserImage } from "../../../../../service/Image";
import { EPMLOYEE_CONFIRMED_ORDER, EMPLOYEE_ORDER, EMPLOYEE_PENDING_ORDER 
} from "../../../../../constant/FoodShoppingURL";
import { ORDER_STATUS } from "../../../../../constant/WebConstant";
import { cancelOrder, confirmOrder, fulfillOrder, getOrderSummary, getOrders } from "../../../../../api/OrderApi";
import notificationMessagesContext from "../../../../contexts/NotificationMessagesContext";
import { ModalContextType } from "../../../../../model/WebType";
import modalContext from "../../../../contexts/ModalContext";
import { Link, useSearchParams } from "react-router-dom";
import { ceilRound } from "../../../../../service/Convert";

export default function EmployeeOrdersComponent(){

    const [searchParams] = useSearchParams()
    const orderParam = searchParams.get("order")!=null?searchParams.get("order").toLowerCase():ORDER_STATUS.PENDING

    const [listOrders, setListOrders] = useState<OrderDetail[]>([])
    const [orderSummary, setOrderSummary] = useState<OrderSummary>({
        totalPendingOrders: 0
    })
    const {setModal: setErrorModal} = useContext<ModalContextType>(modalContext);
    const notificationMessage = useContext<Notification|undefined>(notificationMessagesContext)
    const navHeaderRef = useRef(null)
    const pendingOrdersRef = useRef<any>(null);
    const [page, setPage] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    })
    const pageNumber = getPageNumber();
    const [isPendingOrder, setIsPendingOrder] = useState(true);

    useEffect(()=>{
        initial();
    }, [notificationMessage, orderParam, pageNumber])

    function initial(){  
        fetchOrderSummary();
        fetchOrdersBasedOnStatus(pageNumber);
    }


    function fetchOrdersBasedOnStatus(pageNumber: number){
        setIsPendingOrder(false);
        // get orders
        switch (orderParam){
            // pending orders
            case ORDER_STATUS.PENDING:{
                setIsPendingOrder(true);
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
            // canceled orders
            case ORDER_STATUS.ALL:{
                fetchOrders(pageNumber, ORDER_STATUS.ALL);
                break;
            }
        }
    }

    // get orders
    async function fetchOrders(pageNumber: number, type: string){
        try {
            const res = await getOrders(pageNumber, type)
            if(res.status){
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

    // get order summary
    const fetchOrderSummary = async ()=>{
        const res = await getOrderSummary();
        if(200<=res.status&&res.status<300){
            const data = res.data;
            setOrderSummary(data);
        }
    }

    // process order
    // click confirm order
    const onClickConfirmOrder = (order: OrderDetail)=>{
        employeeConfirmOrder(order)
    }
    // confirm order
    const employeeConfirmOrder = async (order: OrderDetail)=>{
        try {
            const data = {
                orderID: order.orderID,
                customerID: order.customerID
            }
            const res = await confirmOrder(data);
            if(200<=res.status&&res.status<300){
                window.location.reload();
            }
        } catch (error) {
            window.location.reload();
        }
    }

    // fullfill order
    const onClickFullFillOrder = (order: OrderDetail)=>{
        employeeFullfillOrder(order);
    }
    // fullfill order
    const employeeFullfillOrder = async (order: OrderDetail)=>{
        try {
            const data = {
                orderID: order.orderID,
                customerID: order.customerID
            }
            const res = await fulfillOrder(data);
            if(200<=res.status&&res.status<300){
                window.location.reload();
            }
        } catch (error) {
            window.location.reload();
        }
    }

    // cancel order
    const onClickCancelOrder = (order: OrderDetail, orderType: string)=>{
        employeeCancelOrder(order, orderType);
    }
    // cancel order
    const employeeCancelOrder = async (order: OrderDetail, orderType: string)=>{
        try {
            const data = {
                orderID: order.orderID,
                customerID: order.customerID
            }
            const res = await cancelOrder(data, orderType);
            if(200<=res.status&&res.status<300){
                window.location.reload();
            }
        } catch (error) {
            window.location.reload();
        }
    }

    return(
        <>
            <ul className="nav nav-fill nav-tabs emp-profile p-0 mb-3 cursor-pointer box-shadow-default" 
                role="tablist" ref={navHeaderRef}>
  
                <li className="nav-item" role="presentation">
                    <Link to={`${EMPLOYEE_ORDER}?order=${ORDER_STATUS.PENDING}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.PENDING ?'active':''}`}
                            id="pending-order-tab" role="tab">
                            Pending Orders
                            {orderSummary.totalPendingOrders>0 &&
                                <span className="order-badge badge rounded-pill badge-notification bg-danger z-1">
                                    {orderSummary.totalPendingOrders}
                                </span>
                            }
                        </span>
                    </Link>
                </li>
                <li className="nav-item" role="presentation">
                    <Link to={`${EMPLOYEE_ORDER}?order=${ORDER_STATUS.CONFIRMED}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.CONFIRMED ?'active':''}`}
                            id="all-order-tab" role="tab">Confirmed Orders</span>
                    </Link>
                </li>
                <li className="nav-item" role="presentation">
                    <Link to={`${EMPLOYEE_ORDER}?order=${ORDER_STATUS.SHIPPING}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.SHIPPING ?'active':''}`}
                            id="confirmed-order-tab" role="tab">Shipping Orders</span>
                    </Link>
                </li>
                <li className="nav-item" role="presentation">
                    <Link to={`${EMPLOYEE_ORDER}?order=${ORDER_STATUS.SUCCESSFUL}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.SUCCESSFUL ?'active':''}`}
                            id="shipping-order-tab" role="tab">Successful Orders</span>
                    </Link>
                </li>
                <li className="nav-item" role="presentation">
                    <Link to={`${EMPLOYEE_ORDER}?order=${ORDER_STATUS.CANCELED}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.CANCELED ?'active':''}`}
                            id="successful-order-tab" role="tab">Canceled Orders</span>
                    </Link>
                </li>
                <li className="nav-item" role="presentation">
                    <Link to={`${EMPLOYEE_ORDER}?order=${ORDER_STATUS.ALL}`}>
                        <span className={`nav-link text-dark ${orderParam===ORDER_STATUS.ALL ?'active':''}`}
                            id="canceled-order-tab" role="tab">All Orders</span>
                    </Link>
                </li>
            </ul>
            <div className="tab-pane" id="orders-tabpanel" role="tabpanel" aria-labelledby="orders-tabpanel">

                {orderParam===ORDER_STATUS.PENDING ?
                    <ul className={`list-group pending-orders fade ${isPendingOrder?'show':''}`} ref={pendingOrdersRef}>
                        {listOrders.length>0 ?
                            listOrders.map((order) =>(
                                <li className="list-group-item cursor-default my-2 box-shadow-default py-3 order-item" key={order.orderID}>
                                    <div className="d-flex align-items-center">
                                        <p className="h6 mx-3">OrderID: #{order.orderID}</p>
                                    </div>
                                    <ul className="list-group mb-3">
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
                                    <div className="row">
                                        <p className="h6 mx-3">Customer</p>
                                        <div className="d-flex mx-4">
                                            <div>
                                                <img width={"50px"} className="img-thumbnail" src={displayUserImage(order.picture)} alt={order.contactName} />
                                            </div>
                                            <div className="mx-2">
                                                <p className="m-0">ID: {order.customerID}</p>
                                                <p className="h6">Name: {order.contactName}</p>
                                            </div>
                                        </div>
                                    </div>
                                    <hr />
                                    <div className="card-footer mt-2">
                                        <div className="row justify-content-between">
                                            <div className="col-md-3 flex-column px-5 d-flex justify-content-center">
                                                <p className="h6">Status: {order.status}</p>
                                                <p className="h6">Freight: {order.freight}</p>
                                            </div>
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Total Price: {order.totalPrice}</p>
                                            </div>
                                        </div>
                                    </div>
                                    <hr />
                                    <div className="d-flex justify-content-end">
                                        <Link to={`${EMPLOYEE_PENDING_ORDER}/${order.orderID}`}>
                                            <div className="btn btn-info text-light mx-2">View Order</div>
                                        </Link>
                                        <button className="btn btn-primary mx-2" onClick={(_e)=>onClickCancelOrder(order, ORDER_STATUS.PENDING)}>Cancel</button>
                                        <button className="btn btn-primary mx-2" onClick={(_e)=>onClickConfirmOrder(order)}>Confirm</button>
                                    </div>
                                </li>
                            )):
                            <li className="list-group-item order-item d-flex justify-content-center align-items-center box-shadow-default">
                                <h5>No orders available</h5>
                            </li>
                        }
                    </ul>:
                orderParam===ORDER_STATUS.CONFIRMED?
                    <ul className={`list-group fade ${!isPendingOrder?'show':''}`}>
                        {listOrders.length>0 ?
                            listOrders.map((order) =>(
                                <li className="list-group-item cursor-default my-2 box-shadow-default py-3 order-item" key={order.orderID}>
                                    <div className="d-flex align-items-center">
                                        <p className="h6 mx-3">OrderID: #{order.orderID}</p>
                                    </div>
                                    <ul className="list-group mb-3">
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
                                    <div className="row">
                                        <p className="h6 mx-3">Customer</p>
                                        <div className="d-flex mx-4">
                                            <div>
                                                <img width={"50px"} className="img-thumbnail" src={displayUserImage(order.picture)} alt={order.contactName} />
                                            </div>
                                            <div className="mx-2">
                                                <p className="m-0">{order.customerID}</p>
                                                <p className="h6">{order.contactName}</p>
                                            </div>
                                        </div>
                                    </div>
                                    <hr />
                                    <div className="card-footer mt-2">
                                        <div className="row justify-content-between">
                                            <div className="col-md-3 flex-column px-5 d-flex justify-content-center">
                                                <p className="h6">Status: {order.status}</p>
                                                <p className="h6">Freight: {order.freight}</p>
                                            </div>
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Total Price: {ceilRound(order.totalPrice + order.freight)}</p>
                                            </div>
                                        </div>
                                    </div>
                                    <hr />
                                    <div className="d-flex justify-content-end">
                                        <Link to={`${EPMLOYEE_CONFIRMED_ORDER}/${order.orderID}`}>
                                            <div className="btn btn-info text-light mx-2">View Order</div>
                                        </Link>
                                        <button className="btn btn-primary mx-2" onClick={(_e)=>onClickCancelOrder(order, ORDER_STATUS.CONFIRMED)}>Cancel</button>
                                        <button className="btn btn-primary mx-2" onClick={(_e)=>onClickFullFillOrder(order)}>Fulfill Order</button>
                                    </div>
                                </li>
                            )):
                            <li className="list-group-item order-item d-flex justify-content-center align-items-center box-shadow-default">
                                <h5>No orders available</h5>
                            </li>
                        }
                    </ul>:
                    <ul className={`list-group fade ${!isPendingOrder?'show':''}`}>
                        {listOrders.length>0 ?
                            listOrders.map((order) =>(
                                <li className="list-group-item cursor-default my-2 box-shadow-default py-3 order-item" key={order.orderID}>
                                    <div className="d-flex align-items-center">
                                        <p className="h6 mx-3">OrderID: #{order.orderID}</p>
                                    </div>
                                    <ul className="list-group mb-3">
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
                                    <div className="row">
                                        <p className="h6 mx-3">Customer</p>
                                        <div className="d-flex mx-4">
                                            <div>
                                                <img width={"50px"} className="img-thumbnail" src={displayUserImage(order.picture)} alt={order.contactName} />
                                            </div>
                                            <div className="mx-2">
                                                <p className="m-0">{order.customerID}</p>
                                                <p className="h6">{order.contactName}</p>
                                            </div>
                                        </div>
                                    </div>
                                    <hr />
                                    <div className="card-footer mt-2">
                                        <div className="row justify-content-between">
                                            <div className="col-md-3 flex-column px-5 d-flex justify-content-center">
                                                <p className="h6">Status: {order.status}</p>
                                                <p className="h6">Freight: {order.freight}</p>
                                            </div>
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Total Price: {ceilRound(order.totalPrice + order.freight)}</p>
                                            </div>
                                        </div>
                                    </div>
                                    <hr />
                                    <div className="d-flex justify-content-end">
                                        <Link to={`${EMPLOYEE_ORDER}/${order.orderID}`}>
                                            <div className="btn btn-info text-light mx-2">
                                                View Order
                                            </div>
                                        </Link>
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