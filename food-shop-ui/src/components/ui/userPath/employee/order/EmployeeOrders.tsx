import { useEffect, useRef, useState } from "react";
import { OrderWithProduct, Pageable } from "../../../../../model/Type";
import PaginationSection from "../../../../shared/website/sections/paginationSection/PaginationSection";
import { getPageNumber } from "../../../../../service/pageable";
import { displayProductImage } from "../../../../../service/image";
import { employeeOrder, employeePendingOrder } from "../../../../../constant/FoodShoppingURL";
import { getOrders, getPendingOrders } from "../../../../../api/EmployeeApi";
import { ORDER_STATUS } from "../../../../../constant/config";

export default function EmployeeOrdersComponent(){
    const [listOrders, setListOrders] = useState<OrderWithProduct[]>([])
    const navHeaderRef = useRef(null)
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
        const pageNumber = getPageNumber();
        fetchPendingOrders(pageNumber);
    }

    async function fetchOrders(pageNumber: number, type: string){
        const res = await getOrders(pageNumber, type)
        if(res.status === 200){
            console.log(res.data)
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

    async function fetchPendingOrders(pageNumber: number){
        const res = await getPendingOrders(pageNumber)
        if(res.status === 200){
            console.log(res.data)
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

    const onClickConfirmOrder = ()=>{

    }
    const onClickCancelOrder = ()=>{

    }
    const onClickViewOrder = ()=>{

    }

    const onClickNavTab: React.MouseEventHandler<HTMLSpanElement> = (event)=>{
        setIsPendingOrder(false);
        const target = event.currentTarget;
        // fetch corresponding data
        const orderType = target.id;
        if(orderType==="pending-order-tab"){
            setIsPendingOrder(true);
            fetchPendingOrders(page.number);
        }else if(orderType === 'all-order-tab'){
            fetchOrders(page.number, ORDER_STATUS.ALL);
        }else if(orderType === 'successful-order-tab'){
            fetchOrders(page.number, ORDER_STATUS.SUCCESSFUL);
        }else if(orderType === 'canceled-order-tab'){
            fetchOrders(page.number, ORDER_STATUS.CANCELED);
        }
        // remove and then highligh tab
        if(navHeaderRef.current){
            const navHeaderEle = navHeaderRef.current as HTMLUListElement;
            const navLinks = navHeaderEle.getElementsByClassName("nav-link");
            for(var navLink of navLinks){
                navLink.classList.remove("active")
            }
        }
        target.classList.add("active")
    }

    return(
        <>
            <ul className="nav nav-fill nav-tabs emp-profile p-0 mb-3 cursor-pointer box-shadow-default" 
                role="tablist" ref={navHeaderRef}>
  
                <li className="nav-item" role="presentation">
                    <span className="nav-link text-dark active"
                        id="pending-order-tab" role="tab" onClick={onClickNavTab}>Pending</span>
                </li>
                <li className="nav-item" role="presentation">
                    <span className="nav-link text-dark"
                        id="all-order-tab" role="tab" aria-controls="all-order-tab"
                        onClick={onClickNavTab}>All Orders</span>
                </li>
                <li className="nav-item" role="presentation">
                    <span className="nav-link text-dark"
                        id="successful-order-tab" role="tab" onClick={onClickNavTab}>Successful Orders</span>
                </li>
                <li className="nav-item" role="presentation">
                    <span className="nav-link text-dark"
                        id="canceled-order-tab" role="tab" onClick={onClickNavTab}>Canceled Orders</span>
                </li>
            </ul>
            <div className="tab-pane" id="orders-tabpanel" role="tabpanel" aria-labelledby="orders-tabpanel">

                {isPendingOrder ?
                    <ul className={`list-group pending-orders fade ${isPendingOrder?'show':''}`}>
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
                                    </div>
                                    <hr />
                                    <div className="d-flex justify-content-end">
                                        <a href={`${employeePendingOrder}/${order.orderID}`} 
                                            className="btn btn-info text-light mx-2" 
                                            onClick={onClickViewOrder}>View Order</a>
                                        <button className="btn btn-primary mx-2" onClick={onClickCancelOrder}>Cancel</button>
                                        <button className="btn btn-primary mx-2" onClick={onClickConfirmOrder}>Confirm</button>
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
                                    </div>
                                    <hr />
                                    <div className="d-flex justify-content-end">
                                        <a href={`${employeeOrder}/${order.orderID}`} className="btn btn-info text-light mx-2" onClick={onClickViewOrder}>View Order</a>
                                    </div>
                                </li>
                            )):
                            <li className="list-group-item order-item d-flex justify-content-center align-items-center box-shadow-default">
                                <h5>No order available</h5>
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