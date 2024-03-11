import './Cart.css';
import CartCard from '../../../shared/functions/cartCard/CartCard';
import { useEffect, useState } from 'react';
import { CartProduct, Pageable } from '../../../../model/Type';
import { getShippers } from '../../../../api/SearchApi';

export default function CartComponent(){
    const [cartProducts, setCartProducts] = useState<CartProduct[]>([]);
    const [pageable, setPageable] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    })

    useEffect(()=>{
        initial();
    }, []);

    function onClickAddon(){

    }
    function onClickMinuson(){

    }
    function onClickRemoveCartProduct(){
        
    }

    function handleShippers(){
        getShippers(0).then(res =>{
            if(res.status===200){
                const data = res.data;
                console.log(data);
            }
        })
    }

    function initial(){
        
    }



    return(
        <section className="h-100 h-custom cart-section">
            <div className="container py-5 h-100">
                <div className="row d-flex justify-content-center align-items-center h-100">
                    <div className="col-12">
                        <div className="card card-registration card-registration-2">
                            <div className="card-body p-0">
                                <div className="row g-0">
                                    <div className="col-lg-8">
                                        <div className="p-5">
                                            <div className="d-flex justify-content-between align-items-center mb-5">
                                                <h1 className="fw-bold mb-0 text-black">Shopping Cart</h1>
                                                <h6 className="mb-0 text-muted">3 items</h6>
                                            </div>
                                            <hr className="my-4"/>

                                            <div className="row mb-4 d-flex justify-content-between align-items-center">
                                                <CartCard foodName='Cotton T-shirt' foodCategory='Shirt' foodPrice={44.00} foodQuantity={1}
                                                    foodImgSrc='https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-shopping-carts/img5.webp'/>
                                            </div>

                                            <hr className="my-4"/>

                                            <div className="row mb-4 d-flex justify-content-between align-items-center">
                                                <CartCard foodName='Cotton T-shirt' foodCategory='Shirt' foodPrice={44.00} foodQuantity={1}
                                                    foodImgSrc='https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-shopping-carts/img6.webp'/>
                                            </div>

                                            <hr className="my-4"/>

                                            <div className="row mb-4 d-flex justify-content-between align-items-center">
                                                <CartCard foodName='Cotton T-shirt' foodCategory='Shirt' foodPrice={44.00} foodQuantity={1}
                                                    foodImgSrc='https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-shopping-carts/img7.webp'/>
                                            </div>

                                            <hr className="my-4"/>

                                            {/* <div className="pt-5">
                                                <h6 className="mb-0"><a href="#!" className="text-body">
                                                    <FontAwesomeIcon icon={faLongArrowAltLeft}/> Back to shop</a>
                                                </h6>
                                            </div> */}
                                        </div>
                                    </div>
                                    <div className="col-lg-4 bg-grey">
                                        <div className="p-5">
                                            <h3 className="fw-bold mb-5 mt-2 pt-1">Summary</h3>
                                            <hr className="my-4"/>

                                            <div className="d-flex justify-content-between mb-4">
                                                <h5 className="text-uppercase">items 3</h5>
                                                <h5>€ 132.00</h5>
                                            </div>

                                            <h5 className="text-uppercase mb-3">Shipping</h5>

                                            <div className="mb-4 pb-2">
                                                <select className="select">
                                                    <option value="1">Standard-Delivery- €5.00</option>
                                                    <option value="2">Two</option>
                                                    <option value="3">Three</option>
                                                    <option value="4">Four</option>
                                                </select>
                                            </div>

                                            {/* <h5 className="text-uppercase mb-3">Give code</h5>

                                            <div className="mb-5">
                                                <div className="form-outline">
                                                    <input type="text" id="form3Examplea2" className="form-control form-control-lg"/>
                                                    <label className="form-label" htmlFor="form3Examplea2">Enter your code</label>
                                                </div>
                                            </div> */}

                                            <hr className="my-4"/>

                                            <div className="d-flex justify-content-between mb-5">
                                                <h5 className="text-uppercase">Total price</h5>
                                                <h5>€ 137.00</h5>
                                            </div>

                                            <button type="button" className="btn btn-dark btn-block btn-lg"
                                                data-mdb-ripple-color="dark">Register</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}