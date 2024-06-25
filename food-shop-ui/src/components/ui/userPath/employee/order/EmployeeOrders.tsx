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
import { Link } from "react-router-dom";

export default function EmployeeOrdersComponent(){
    const [listOrders, setListOrders] = useState<OrderDetail[]>([])
    const [orderSummary, setOrderSummary] = useState<OrderSummary>({
        totalPendingOrders: 0
    })
    const {setModal: setErrorModal} = useContext<ModalContextType>(modalContext);
    const notificationMessage = useContext<Notification|undefined>(notificationMessagesContext)
    const navHeaderRef = useRef(null)
    const pendingOrdersRef = useRef<any>(null);
    const [selectedTagOrder, setSelectedTagOrder] = useState(0); 
    const [page, setPage] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    })
    const [isPendingOrder, setIsPendingOrder] = useState(true);

    useEffect(()=>{
        initial();
    }, [notificationMessage])

    function initial(){
        if(notificationMessage) console.log(notificationMessage)
        const pageNumber = getPageNumber();
        fetchOrderSummary();
        fetchOrders(pageNumber, ORDER_STATUS.PENDING);
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
            console.log(data);
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
    const onClickCancelOrder = (order: OrderDetail)=>{
        employeeCancelOrder(order);
    }
    // cancel order
    const employeeCancelOrder = async (order: OrderDetail)=>{
        try {
            const data = {
                orderID: order.orderID,
                customerID: order.customerID
            }
            const res = await cancelOrder(data);
            if(200<=res.status&&res.status<300){
                window.location.reload();
            }
        } catch (error) {
            window.location.reload();
        }
    }

    // select order tab
    const onClickNavTab = (tab: number)=>{
        setIsPendingOrder(false);
        setSelectedTagOrder(tab)
        // get orders
        switch (tab){
            // pending orders
            case 0:{
                setIsPendingOrder(true);
                fetchOrders(page.number, ORDER_STATUS.PENDING);
                break;
            }
            // confirmed orders
            case 1:{
                fetchOrders(page.number, ORDER_STATUS.CONFIRMED);
                break;
            }
            // shipping orders
            case 2:{
                fetchOrders(page.number, ORDER_STATUS.SHIPPING);
                break;
            }
            // successful orders
            case 3:{
                fetchOrders(page.number, ORDER_STATUS.SUCCESSFUL);
                break;
            }
            // canceled orders
            case 4:{
                fetchOrders(page.number, ORDER_STATUS.CANCELED);
                break;
            }
            // canceled orders
            case 5:{
                fetchOrders(page.number, ORDER_STATUS.ALL);
                break;
            }
        }
    }
    return(
        <>
            <ul className="nav nav-fill nav-tabs emp-profile p-0 mb-3 cursor-pointer box-shadow-default" 
                role="tablist" ref={navHeaderRef}>
  
                <li className="nav-item" role="presentation">
                    <span className={`nav-link text-dark ${selectedTagOrder===0 ?'active':''}`}
                        id="pending-order-tab" role="tab" onClick={(_e)=>onClickNavTab(0)}>
                        Pending Orders
                        {orderSummary.totalPendingOrders>0 &&
                            <span className="order-badge badge rounded-pill badge-notification bg-danger z-1">
                                {orderSummary.totalPendingOrders}
                            </span>
                        }
                    </span>
                </li>
                <li className="nav-item" role="presentation">
                    <span className={`nav-link text-dark ${selectedTagOrder===1 ?'active':''}`}
                        id="all-order-tab" role="tab" onClick={(_e)=>onClickNavTab(1)}>Confirmed Orders</span>
                </li>
                <li className="nav-item" role="presentation">
                    <span className={`nav-link text-dark ${selectedTagOrder===2 ?'active':''}`}
                        id="confirmed-order-tab" role="tab" onClick={(_e)=>onClickNavTab(2)}>Shipping Orders</span>
                </li>
                <li className="nav-item" role="presentation">
                    <span className={`nav-link text-dark ${selectedTagOrder===3 ?'active':''}`}
                        id="shipping-order-tab" role="tab" onClick={(_e)=>onClickNavTab(3)}>Successful Orders</span>
                </li>
                <li className="nav-item" role="presentation">
                    <span className={`nav-link text-dark ${selectedTagOrder===4 ?'active':''}`}
                        id="successful-order-tab" role="tab" onClick={(_e)=>onClickNavTab(4)}>Canceled Orders</span>
                </li>
                <li className="nav-item" role="presentation">
                    <span className={`nav-link text-dark ${selectedTagOrder===5 ?'active':''}`}
                        id="canceled-order-tab" role="tab" onClick={(_e)=>onClickNavTab(5)}>All Orders</span>
                </li>
            </ul>
            <div className="tab-pane" id="orders-tabpanel" role="tabpanel" aria-labelledby="orders-tabpanel">

                {selectedTagOrder===0 ?
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
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Status: {order.status}</p>
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
                                        <button className="btn btn-primary mx-2" onClick={(_e)=>onClickCancelOrder(order)}>Cancel</button>
                                        <button className="btn btn-primary mx-2" onClick={(_e)=>onClickConfirmOrder(order)}>Confirm</button>
                                    </div>
                                </li>
                            )):
                            <li className="list-group-item order-item d-flex justify-content-center align-items-center box-shadow-default">
                                <h5>No orders available</h5>
                            </li>
                        }
                    </ul>:
                selectedTagOrder===1?
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
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Status: {order.status}</p>
                                            </div>
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Total Price: {order.totalPrice}</p>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-end">
                                        <Link to={`${EPMLOYEE_CONFIRMED_ORDER}/${order.orderID}`}>
                                            <div className="btn btn-info text-light mx-2">View Order</div>
                                        </Link>
                                        <button className="btn btn-primary mx-2" onClick={(_e)=>onClickCancelOrder(order)}>Cancel</button>
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
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Status: {order.status}</p>
                                            </div>
                                            <div className="col-md-3 d-flex justify-content-center">
                                                <p className="h6">Total Price: {order.totalPrice}</p>
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