import { useContext, useEffect, useState } from "react";
import { OrderDetail, Pageable } from "../../../../../model/Type";
import { ORDER_STATUS } from "../../../../../constant/WebConstant";
import PaginationSection from "../../../../shared/website/sections/paginationSection/PaginationSection";
import { getPageNumber } from "../../../../../service/Pageable";
import { customerOrder } from "../../../../../constant/FoodShoppingURL";
import { displayProductImage } from "../../../../../service/Image";
import { getCustomerOrders, receiveOrder } from "../../../../../api/OrderApi";
import notificationMessagesContext from "../../../../contexts/NotificationMessagesContext";

export default function UserOrdersComponent(){
    const [listOrders, setListOrders] = useState<OrderDetail[]>([])
    const notificationMessage = useContext(notificationMessagesContext)
    const [selectedTagOrder, setSelectedTagOrder] = useState(0)
    const [page, setPage] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    })

    useEffect(()=>{
        initial();
    }, [notificationMessage])

    function initial(){
        const pageNumber = getPageNumber();
        fetchOrders(pageNumber, ORDER_STATUS.PENDING);
    }
    
    // get orders
    async function fetchOrders(pageNumber: number, type:string){
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
    }
    // click select nav tab
    const onClickNavTab = (tab: number)=>{
        setSelectedTagOrder(tab)
        // get orders
        switch (tab){
            // pending orders
            case 0:{
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
                {selectedTagOrder===2 ?
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
                                            <a href={`${customerOrder}/${order.orderID}`} className="btn btn-info text-white">View order</a>
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
                                                    {/* <a href="#" className="btn btn-primary">Go somewhere</a> */}
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
                                                <a href={`${customerOrder}/${order.orderID}`} className="btn btn-primary">View order</a>
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