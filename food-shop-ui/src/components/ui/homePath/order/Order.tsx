import { ChangeEventHandler, useContext, useEffect, useState } from "react"
import { District, OrderInfo, PaymentMethod, Province, Ward } from "../../../../model/Type"
import { displayProductImage, getError } from "../../../../service/Image";
import { deleteCartProducts, getOrder } from "../../../../api/CartApi";
import { faLongArrowAltLeft } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { CART_PATH } from "../../../../constant/FoodShoppingURL";
import { Notification } from "../../../../model/Type";
import { 
    ALERT_TIMEOUT, ALERT_TYPE, NOTIFICATION_TYPE, ORDER_PAYMENT_STATUS 
} from "../../../../constant/WebConstant";
import { placeOrder } from "../../../../api/OrderApi";
import { Link, useNavigate } from "react-router-dom";
import ScrollToTop from "../../../shared/functions/scroll-to-top/ScrollToTop";
import numberOfCartProductsContext from "../../../contexts/NumberOfCartProductsContext";
import { getPaymentMethods } from "../../../../api/PaymentApi";
import AlertComponent from "../../../shared/functions/alert/Alert";
import { Alert } from "../../../../model/WebType";
import { getDistricts, getProvinces, getShippingCost, getWards } from "../../../../api/ShippingApi";
import { ceilRound } from "../../../../service/Convert";

export default function OrderComponent(){
    const [orderInfo, setOrderInfo] = useState<OrderInfo>();
    const urlParams = new URLSearchParams(window.location.search);
    const status = urlParams.get('status');
    const [provinces, setProvinces] = useState<Province[]>([])
    const [districts, setDistricts] = useState<District[]>([])
    const [wards, setWards] = useState<Ward[]>([])
    const {numberOfCartProducts: cartproducts, setNumberOfCartProducts: setNumberOfCartProducts}= useContext(numberOfCartProductsContext)
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
        picture: '',
        isShowed: false,
    })
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: "",
        isShowed: false
    })
    const navigate = useNavigate();
    const currentUrl = new URL(window.location.href);
    const [paymentMethods, setPaymentMethods] = useState<PaymentMethod[]>([]);

    useEffect(()=>{
        initial();
    }, [status])

    const initial = ()=>{
        paymentStatus(status);
        fetchCartOrder();
        fetchPaymentMethods();
    }

    // get address
    const fetchProvinces = async (orderInfo: OrderInfo)=>{
        try {
            const res = await getProvinces();
            if(res.status){
                const data = res.data as Province[];
                setProvinces(data)
                const provinceID = data.find(province => province.ProvinceName===orderInfo.shipCity).ProvinceID;
                if(provinceID)
                    fetchDistricts(provinceID, orderInfo);
            }
        } catch (error) {

        }
    }

    const fetchDistricts = async (provinceID: string, orderInfo: OrderInfo)=>{
        try {
            const res = await getDistricts(provinceID);
            if(res.status){
                const data = res.data as District[];
                setDistricts(data);
                const districtID = data.find(district => district.DistrictName===orderInfo.shipDistrict).DistrictID;
                if(districtID)
                    fetchWards(provinceID, districtID, orderInfo);
            }
        } catch (error) {

        }
    }
    const fetchWards = async (cityID: string, districtID: string, orderInfo: OrderInfo)=>{
        try {
            const res = await getWards(districtID);
            if(res.status){
                const data = res.data as Ward[];
                setWards(data);

                const wardCode = data.find(ward => ward.WardName===orderInfo.shipWard).WardCode;
                fetchShippingCost(wardCode, districtID, cityID);
            }
        } catch (error) {

        }
    }

    // payment
    const paymentStatus = (status: string)=>{
        if(status===ORDER_PAYMENT_STATUS.SUCCESSFUL){
            setNotification({
                ...notification, 
                isShowed:true, 
                status: NOTIFICATION_TYPE.SUCCESSFUL,
                message: "Your order has been placed!"
            })
        }else if(status===ORDER_PAYMENT_STATUS.CANCELED){
            setNotification({
                ...notification, 
                isShowed:true, 
                status: NOTIFICATION_TYPE.CANCEL,
                message: "Your order has been canceled!"
            })
        }
    }

    async function fetchPaymentMethods(){
        try {
            const res = await getPaymentMethods();
            if(200<=res.status&&res.status<300){
                const data = res.data;
                setPaymentMethods(data)
            }
        } catch (error) {
            
        }
    }

    // cart order
    async function fetchCartOrder(){
        try {
            const res = await getOrder();
            if(200<=res.status&&res.status<300){
                const data = res.data;
                setOrderInfo(data);
                fetchProvinces(data);
            }
        } catch (error) {

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
                freight: orderInfo.freight,
                shipName: orderInfo.shipName,
                shipAddress: orderInfo.shipAddress,
                shipCity: orderInfo.shipCity,
                shipDistrict: orderInfo.shipDistrict,
                shipWard: orderInfo.shipWard,
                phone: orderInfo.phone,
                contactName: orderInfo.shipName,
                shipVia: 1,
                method: orderInfo.method
            }
            // console.log(data)
            userPlaceOrder(data);
        }
    }

    // place order
    async function userPlaceOrder(order: any){
        try {
            if(!order.shipAddress || !order.shipAddress.length){
                setAlert({
                    message: "Your address is missing",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }else if(!order.shipCity || !order.shipCity.length){
                setAlert({
                    message: "Your city is not selected",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }else if(!order.shipDistrict || !order.shipDistrict.length){
                setAlert({
                    message: "Your district is not selected",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }else if(!order.shipWard || !order.shipWard.length){
                setAlert({
                    message: "Your ward is not selected",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }else if(!order.shipName || !order.shipName.length){
                setAlert({
                    message: "Your contact name is missing",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                })
            }else if(!order.phone || !order.phone.length){
                setAlert({
                    message: "Your phone is missing",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                })
            }else if(!orderInfo.method || !orderInfo.method.length){
                setAlert({
                    message: "Select your payment method",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                })
            }else{
                const res = await placeOrder(order);
                if(200<=res.status&&res.status<300){
                    const data = res.data;
                    const productIds = orderInfo.products.map(product => product.productID);
                    removeProductsInCart(productIds)
                    if(data.redirect_url){
                        window.location.href = data.redirect_url;
                    }else{
                        if(data.status){
                            // Add or update the query parameter
                            currentUrl.searchParams.set('status', ORDER_PAYMENT_STATUS.SUCCESSFUL);
                            // Navigate to the updated URL
                            navigate(currentUrl.pathname + currentUrl.search);
                        }else{
                            // Add or update the query parameter
                            currentUrl.searchParams.set('status', ORDER_PAYMENT_STATUS.CANCELED);
                            // Navigate to the updated URL
                            navigate(currentUrl.pathname + currentUrl.search);
                        }
                    }
                }
            }
        } catch (error) {
            setAlert({
                message: `${error.response.data.error}`,
                type: ALERT_TYPE.DANGER,
                isShowed: true
            }) 
        }
        finally{
            setTimeout(()=>{
                setAlert({...alert, isShowed: false});
            }, ALERT_TIMEOUT)
        }        
    }
    

    // get shipping service
    const fetchShippingCost = async (wardCode: string, districtID: string, cityID: string)=>{
        try {
            const res = await getShippingCost(wardCode, districtID, cityID)
            if(res.status){
                const data = res.data;
                // setShippingCost(data.total)
                setOrderInfo(order => ({
                    ...order,
                    freight: data.total
                }))
                // console.log(data)
            }
        } catch (error) {
            
        }
    }
    async function removeProductsInCart(productIDs: number[]){
        const res = await deleteCartProducts(productIDs);
        if(200<=res.status&&res.status<300){
            setNumberOfCartProducts(cartproducts-productIDs.length)
        }
    }

    function capitalizeFirstLetter(str: string): string {
        return str.charAt(0).toUpperCase() + str.slice(1);
    } 

    const onChangeSelectPaymentMethod: ChangeEventHandler<HTMLSelectElement>= (event)=>{
        const value = event.target.value;
        setOrderInfo({...orderInfo, method: value})
    }

    // get address id
    const getCityID = (cityName: string, provinces: Province[])=>{
        return provinces.find(province => province.ProvinceName === cityName)?.ProvinceID;
    }
    const getDistricID = (districtName: string, districts: District[])=>{
        return districts.find(district => district.DistrictName === districtName)?.DistrictID;
    }
    const getWardCode = (wardName: string, wards: Ward[])=>{
        return wards.find(ward => ward.WardName === wardName)?.WardCode;
    }
    // on change select
    const onChangeSelectProvince = (e)=>{
        const value = e.target.value; 
        setOrderInfo({...orderInfo, shipCity: value, shipDistrict: null, shipWard: null})
        const cityID = getCityID(value, provinces);
        fetchDistricts(cityID, orderInfo)
    }

    const onChangeSelectDistrict = (e)=>{
        const name = e.target.name;
        const value = e.target.value;
        setOrderInfo({...orderInfo, [name]:value, shipWard: null})
        fetchWards(getCityID(orderInfo.shipCity, provinces), 
            getDistricID(value, districts), orderInfo)
    }

    const onChangeSelectWard = (e)=>{
        const name = e.target.name;
        const value = e.target.value; 
        setOrderInfo({...orderInfo, [name]:value})
        fetchShippingCost(getWardCode(value, wards), 
            getDistricID(orderInfo.shipDistrict, districts), 
            getCityID(orderInfo.shipCity, provinces));
    }

    return(
        <div className="container my-5" id="checkout-order">
            <ScrollToTop/>
            <AlertComponent alert={alert}/>
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
                        {orderInfo.products && orderInfo.products.map(product =>(
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
                                {/* <div>COD</div> */}
                                {/* <img src="https://www.freepnglogos.com/uploads/mastercard-png/mastercard-logo-logok-15.png" alt=""
                                    width="30" height="30"/>
                                <label>Mastercard ******5342</label> */}

                                <select className="form-select mb-3" value={orderInfo.method || ""} onChange={onChangeSelectPaymentMethod}>
                                    <option>Select payment option</option>
                                    {paymentMethods.map((method, index) => (
                                        <option key={index} value={method.methodName}>
                                            {capitalizeFirstLetter(method.methodName)}
                                        </option>
                                    ))}
                                </select>
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
                                ${ceilRound(orderInfo.totalPrice + orderInfo.freight)}
                            </div>
                        </div>
                        <div className="row border rounded p-1 my-3 d-flex">
                            <div className="col-md-12 py-3">
                                <div className="d-flex flex-column align-items start">
                                    <b>Shipping Address</b>

                                    <div className="row gx-3 mb-3">
                                        <div className="col-md-6">
                                            <label className="small mb-1" htmlFor="inputPhone">Phone number</label>
                                            {((!orderInfo.phone || !orderInfo.phone.length)) &&
                                            <span className="badge bg-danger ms-1">!</span> 
                                            }
                                            <input className="form-control" id="inputPhone" name="phone" type="tel" 
                                                placeholder="Enter your phone number" value={orderInfo.phone} 
                                                onChange={onChangeOrderInfo}/>
                                        </div>
                                        <div className="col-md-6">
                                            <label className="small mb-1" htmlFor="inputShipName">Contact name</label>
                                            {!orderInfo.shipName || !orderInfo.shipName.length&&
                                            <span className="badge bg-danger ms-1">!</span> 
                                            }
                                            <input className="form-control" id="inputShipName" type="text" name="shipName" 
                                                placeholder="Enter your contact name" value={orderInfo.shipName} 
                                                onChange={onChangeOrderInfo}/>
                                        </div>
                                    </div>

                                    <div className="row gx-3 mb-3">
                                        <div className="col-md-6">
                                            <label className="small mb-1" htmlFor="inputShipAddress">Address</label>
                                            {(!orderInfo.shipAddress || !orderInfo.shipAddress.length) &&
                                            <span className="badge bg-danger ms-1">!</span> 
                                            }
                                            <input className="form-control" id="inputShipAddress" name="shipAddress" type="text" 
                                                placeholder="Enter your Address" value={orderInfo.shipAddress || ""} 
                                                onChange={onChangeOrderInfo}/>
                                        </div>
                                        <div className="col-md-6">
                                            <label className="small mb-1" htmlFor="inputCity">City / Province</label>
                                            { (!orderInfo.shipCity || !orderInfo.shipCity.length) &&
                                            <span className="badge bg-danger ms-1">!</span> 
                                            }
                                            <select className="form-select" name="shipCity" 
                                                value={orderInfo.shipCity || ""} 
                                                onChange={onChangeSelectProvince}>

                                                <option>Select your city/province</option>
                                                {provinces.map((province, index)=>(
                                                    <option key={index} value={`${province.ProvinceName}`}>
                                                        {province.ProvinceName}
                                                    </option>
                                                ))}
                                            </select>
                                        </div>
                                    </div>

                                    <div className="row gx-3 mb-3">
                                        <div className="col-md-6">
                                            <label className="small mb-1" htmlFor="inputDistrict">District</label>
                                            {(!orderInfo.shipDistrict || !orderInfo.shipDistrict.length) &&
                                            <span className="badge bg-danger ms-1">!</span> 
                                            }
                                            <select className="form-select" name="shipDistrict" 
                                                onChange={onChangeSelectDistrict} 
                                                value={orderInfo.shipDistrict || ""}>
                                                {!districts.length ?
                                                    <option>Select your province/city first</option>:
                                                    <>
                                                        <option>Select your district</option>
                                                        {districts.map((district, index)=>(
                                                            <option key={index} value={`${district.DistrictName}`}>
                                                                {district.DistrictName}
                                                            </option>
                                                        ))}
                                                    </>
                                                }
                                            </select>
                                        </div>
                                        <div className="col-md-6">
                                            <label className="small mb-1" htmlFor="inputWard">Ward</label>
                                            {(!orderInfo.shipWard || !orderInfo.shipWard.length) &&
                                            <span className="badge bg-danger ms-1">!</span> 
                                            }
                                            <select className="form-select" name="shipWard" 
                                                onChange={onChangeSelectWard} 
                                                value={orderInfo.shipWard || ""}>

                                                {!wards.length ?
                                                    <option>Select your district first</option>:
                                                    <>
                                                        <option>Select your ward</option>
                                                        {wards.map((ward, index)=>(
                                                            <option key={index} value={`${ward.WardName}`}>
                                                                {ward.WardName}
                                                            </option>
                                                        ))}
                                                    </>
                                                }
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <button onClick={onClickOrder} className="btn btn-primary">Checkout</button>
                        </div>
                        <div className="row mt-3">
                            <div className="col-md-4">
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