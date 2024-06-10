import { useEffect, useRef, useState } from "react";
import { OrderDetail, Pageable } from "../../../../../model/Type";
import PaginationSection from "../../../../shared/website/sections/paginationSection/PaginationSection";
import { getPageNumber } from "../../../../../service/pageable";
import { displayProductImage, displayUserImage } from "../../../../../service/image";
import { employeeConfirmedOrder, employeeOrder, employeePendingOrder } from "../../../../../constant/FoodShoppingURL";
import { getOrders, getPendingOrders } from "../../../../../api/EmployeeApi";
import { ORDER_STATUS } from "../../../../../constant/config";
import { CompatClient } from "@stomp/stompjs";
import { CancelOrderWsUrl, ConfirmOrderWsUrl, FulfillOrderWsUrl } from "../../../../../constant/FoodShoppingApiURL";
import { getAccessToken } from "../../../../../service/cookie";

export default function EmployeeOrdersComponent(){
    const [pendingOrders, setPendingOrders] = useState<OrderDetail[]>([])
    const [listOrders, setListOrders] = useState<OrderDetail[]>([])
    const navHeaderRef = useRef(null)
    const stompClientAccount = useRef<CompatClient | null>(null);
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
    }, [])

    function initial(){
        // connectReceiveOrderEmployee(getNewPendingOrder);
        const pageNumber = getPageNumber();
        // fetchPendingOrders(pageNumber);
    }

    async function fetchOrders(pageNumber: number, type: string){
        const res = await getOrders(pageNumber, type)
        if(res.status){
            // console.log(res.data)
            const data = res.data;
            setListOrders(data.content)
            setPage({
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            });
        }
    }

    // async function fetchPendingOrders(pageNumber: number){
    //     const res = await getPendingOrders(pageNumber)
    //     if(res.status){
    //         const data = res.data;
    //         setPendingOrders(data.content)
    //         // console.log(data)
    //         setPage({
    //             first: data.first,
    //             last: data.last,
    //             number: data.number,
    //             totalPages: data.totalPages
    //         });
    //     }
    // }

    // get new pending order
    function getNewPendingOrder(newOrder: any){
        if(newOrder){
            // fetchPendingOrders(page.number);
            setPendingOrders([...pendingOrders, newOrder]);
        }
    }


    // process order
    const onClickConfirmOrder = (order: OrderDetail)=>{
        if(stompClientAccount.current && order){
            stompClientAccount.current.send(ConfirmOrderWsUrl, {
                "Authorization": `Bearer ${getAccessToken()}`
            }, JSON.stringify({
                orderID: order.orderID,
                customerID: order.customerID
            }))
            window.location.reload();
        }
    }
    const onClickFullFillOrder = (order: OrderDetail)=>{
        if(stompClientAccount.current && order){
            stompClientAccount.current.send(FulfillOrderWsUrl, {
                "Authorization": `Bearer ${getAccessToken()}`
            }, JSON.stringify({
                orderID: order.orderID,
                customerID: order.customerID
            }))
            window.location.reload();
        }
    }
    const onClickCancelOrder = (order: OrderDetail)=>{
        if(stompClientAccount.current && order){
            stompClientAccount.current.send(CancelOrderWsUrl, {
                "Authorization": `Bearer ${getAccessToken()}`
            }, JSON.stringify({
                orderID: order.orderID,
                customerID: order.customerID
            }))
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
                // fetchPendingOrders(page.number);
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
                        id="pending-order-tab" role="tab" onClick={(_e)=>onClickNavTab(0)}>Pending Orders</span>
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
                        {pendingOrders.length>0 ?
                            pendingOrders.map((order) =>(
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
                                        <a href={`${employeePendingOrder}/${order.orderID}`} 
                                            className="btn btn-info text-light mx-2">View Order</a>
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
                                        <a href={`${employeeConfirmedOrder}/${order.orderID}`} 
                                            className="btn btn-info text-light mx-2">View Order</a>
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
                                        <a href={`${employeeOrder}/${order.orderID}`} className="btn btn-info text-light mx-2">View Order</a>
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