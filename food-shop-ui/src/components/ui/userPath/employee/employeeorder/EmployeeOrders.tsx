import { useEffect, useRef, useState } from "react";
import { getCustomerOrders } from "../../../../../api/UserApi";
import { Order, OrderProduct, Pageable } from "../../../../../model/Type";
import { ORDER_STATUS } from "../../../../../constant/config";
import PaginationSection from "../../../../shared/website/sections/paginationSection/PaginationSection";
import { getPageNumber } from "../../../../../service/pageable";
import { customerOrder } from "../../../../../constant/FoodShoppingURL";
import { getPendingOrders } from "../../../../../api/EmployeeApi";

export default function EmployeeOrdersComponent(){
    const [listOrders, setListOrders] = useState<Order[]>([])
    const navHeaderRef = useRef(null)
    const [page, setPage] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    })

    useEffect(()=>{
        initial();
    }, [])

    function initial(){
        const pageNumber = getPageNumber();
        fetchPendingOrders(pageNumber);
    }

    // async function fetchOrders(pageNumber: number, type:string){
    //     const res = await getCustomerOrders(pageNumber, type)
    //     if(res.status === 200){
    //         // console.log(res.data)
    //         const data = res.data;
    //         setListOrders(data.content)
    //         setPage({
    //             first: data.first,
    //             last: data.last,
    //             number: data.number,
    //             totalPages: data.totalPages
    //         });
    //     }
    // }

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


    const onClickNavTab: React.MouseEventHandler<HTMLSpanElement> = (event)=>{
        const target = event.currentTarget;
        // fetch corresponding data
        const orderType = target.id;
        if(orderType==="pending-order-tab"){
            // const res = await getPendingOrders(page.number);
            fetchPendingOrders(page.number);
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
                <ul className="list-group">
                    {listOrders.length>0 ?
                        listOrders.map((order) =>(
                            <li className="list-group-item cursor-default my-2 box-shadow-default py-3 order-item" key={order.orderID}>
                                <div className="d-flex align-items-center">
                                    <p className="h6 mx-3">OrderID: #{order.orderID}</p>
                                </div>
                                <ul className="list-group">
                                    {order.products && order.products.map((product: OrderProduct) =>(
                                        <li className="card d-flex flex-row my-md-1 my-sm-2" key={`${order.orderID}&${product.productID}`}>
                                            <div className="col-md-2 p-2">
                                                <img className="order-img-thumbnail card-img-top rounded float-left" src={product.picture} alt="Card image cap"/>
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
                    {/* {listOrders && listOrders.map((order) =>(
                        <li className="list-group-item cursor-default my-2 box-shadow-default" key={order.orderID}>
                            <div className="d-flex align-items-center">
                                <p className="h6 mx-3">OrderID: #{order.orderID}</p>
                            </div>
                            <ul className="list-group">
                                {order.products && order.products.map((product: OrderProduct) =>(
                                    <li className="card d-flex flex-row my-md-1 my-sm-2" key={`${order.orderID}&${product.productID}`}>
                                        <div className="col-md-2 p-2">
                                            <img className="card-img-top rounded float-left" src={product.picture} alt="Card image cap"/>

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
                                        <a href={`${custonerOrder}/${order.orderID}`} className="btn btn-primary">View order</a>
                                    </div>
                                </div>
                            </div>
                        </li>
                    ))} */}
                </ul>
                
                <div className="pagination position-relative justify-content-center my-3">
                    <PaginationSection pageable={page}/>
                </div>
            </div>
        </>
    );
}