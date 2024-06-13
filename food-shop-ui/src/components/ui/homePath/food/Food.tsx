import '../../../../init'

import './Food.css'
import { ChangeEventHandler, useEffect, useState, useContext } from 'react';
import { getProductByID, getRecommendedProductsByCategory } from '../../../../api/SearchApi';
import { CartProduct, CurrentProduct, CurrentProductDetail } from '../../../../model/Type';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMinus, faPlus, faShoppingCart } from '@fortawesome/free-solid-svg-icons';
import { addProductToCart } from '../../../../api/CartApi';
import { displayProductImage } from '../../../../service/Image';
import numberOfCartProductsContext from '../../../contexts/NumberOfCartProductsContext';
import { ceilRound } from '../../../../service/Convert';
import { foodsPath } from '../../../../constant/FoodShoppingURL';
import { numberOfProductsInCart } from '../../../../service/Cart';

export default function FoodComponent(){
    const urlParams = new URLSearchParams(window.location.search);
    const [foodInfo, setFoodInfo] = useState<CurrentProductDetail>();
    const [similarFoods, setSimilarFoods] = useState<CurrentProduct[]>([]);
    const {  setNumberOfCartProducts } = useContext(numberOfCartProductsContext);
    const [cartProduct, setCartProduct] = useState<CartProduct>({
        productID: 0,
        quantity: 1
    })
    
    useEffect(()=>{
        initial();
    }, []);

    function initial(){
        const productID = urlParams.get('sp');
        if(productID){
            fetchProduct(productID);
        }
    }

    // handle product
    function handleClickMinusBtn(){
        setCartProduct((product)=>({
            ...product,
            quantity: product.quantity-1>0?product.quantity-1:1,
        }))

    }
    function handleClickAddOnBtn(){
        if(foodInfo){
            const limit = foodInfo.unitsInStock;
            setCartProduct((product)=>({...product, quantity: 
                product.quantity+1<=limit?product.quantity+1:limit
            }))
        }

    }

    async function onClickAddToCart(){
        const res = await addProductToCart(cartProduct);
        if(res.status){
            const numberProducts = numberOfProductsInCart();
            setNumberOfCartProducts(numberProducts)
        }
    }

    const handleInputChange: ChangeEventHandler<HTMLInputElement> = (event) => {
        const quantity = +event.target.value;
        setCartProduct({...cartProduct, quantity: quantity});
      };

    async function fetchSimilarProducts(productID: string, categoryName: string, page: number){
        const res = await getRecommendedProductsByCategory(categoryName, productID, page);
        if(200<=res.status&&res.status<300){
            const data = res.data;
            setSimilarFoods(data.content);
        }
    }

    async function fetchProduct(productID: string){
        const res = await getProductByID(productID)
        if(res.status){
            const data = res.data;
            // console.log(res.data);
            setCartProduct({
                ...cartProduct,
                productID: data.productID,
                quantity: 1,
            })
            setFoodInfo(data);
            fetchSimilarProducts(data.productID, data.categoryName, 0);
        }
    }

    return(
        <>
            {foodInfo &&
                <>
                    <section className="py-5">
                        <div className="container">
                            <div className="row gx-5">
                                <aside className="col-lg-5 img-large">
                                    <div className="rounded-4 mb-3 position-relative w-100 h-100">
                                        {foodInfo.discountID!=null && foodInfo.discountPercent>0 &&
                                            <div className="position-absolute mt-5">
                                                <span className="badge rounded-pill badge-discount bg-danger ">
                                                    -{foodInfo.discountPercent}%
                                                </span>
                                            </div>
                                        }
                                        <img style={{maxWidth: "100%", margin: "auto"}} className="rounded-4 fit h-100" 
                                            src={displayProductImage(foodInfo.picture)} />
                                    </div>
                                    {/* <div className="d-flex justify-content-center mb-3">
                                        <a data-fslightbox="mygalley" className="border mx-1 item-thumb rounded-2" target="_blank" data-type="image" href="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/detail1/big1.webp">
                                            <img width="60" height="60" className="rounded-2" src="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/detail1/big1.webp" />
                                        </a>
                                        <a data-fslightbox="mygalley" className="border mx-1 item-thumb rounded-2" target="_blank" data-type="image" href="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/detail1/big2.webp">
                                            <img width="60" height="60" className="rounded-2" src="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/detail1/big2.webp" />
                                        </a>
                                        <a data-fslightbox="mygalley" className="border mx-1 item-thumb rounded-2" target="_blank" data-type="image" href="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/detail1/big3.webp">
                                            <img width="60" height="60" className="rounded-2" src="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/detail1/big3.webp" />
                                        </a>
                                        <a data-fslightbox="mygalley" className="border mx-1 item-thumb rounded-2" target="_blank" data-type="image" href="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/detail1/big4.webp">
                                            <img width="60" height="60" className="rounded-2" src="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/detail1/big4.webp" />
                                        </a>
                                        <a data-fslightbox="mygalley" className="border mx-1 item-thumb rounded-2" target="_blank" data-type="image" href="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/detail1/big.webp">
                                            <img width="60" height="60" className="rounded-2" src="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/detail1/big.webp" />
                                        </a>
                                    </div> */}
                                </aside>
                                <main className="col-lg-7">
                                    <div className="ps-lg-3 h-100">
                                        <h4 className="title text-dark">
                                            {foodInfo.productName}
                                        </h4>
                                        <div className="d-flex flex-row my-3">
                                            <span className={`${foodInfo.unitsInStock>0?"text-success": 'text-danger'} ms-2`}>
                                                {foodInfo.unitsInStock>0? "In stock": "Out of stock"}
                                            </span>
                                        </div>

                                        <div className="mb-3">
                                            {/* <span className="h5">{`$${foodInfo.unitPrice}`}</span> */}
                                            <ins>
                                                <span className='h5'>
                                                    <b>
                                                        <span>$</span>
                                                        {ceilRound(foodInfo.unitPrice*(1-foodInfo.discountPercent/100))}
                                                    </b> 
                                                </span>
                                            </ins>
                                            {foodInfo.discountID!= null &&
                                                <del className="text-body-secondary ms-3">
                                                    <span>
                                                        <span>$</span>
                                                        {foodInfo.unitPrice}
                                                    </span>
                                                </del>
                                            }
                                            <span className="text-muted">/per {foodInfo.quantityPerUnit}</span>
                                        </div>
                                        <p>
                                            <b>Category: </b>
                                            {foodInfo.categoryName}
                                        </p>
                                        <hr />

                                        <div className="row">
                                            <div className="col-md-4 col-6 mb-3">
                                                <label className="mb-2 d-block">Quantity</label>
                                                <div className="input-group mb-3 z-0" style={{width: "170px"}}>
                                                    <button className="btn btn-white border border-secondary px-3" 
                                                        type="button" id="button-addon1" data-mdb-ripple-color="dark"
                                                        onClick={handleClickMinusBtn}>
                                                        <FontAwesomeIcon icon={faMinus}/>
                                                    </button>
                                                    <input type="text" className="form-control text-center border border-secondary" 
                                                        value={cartProduct.quantity} aria-label="Example text with button addon" 
                                                        aria-describedby="button-addon1" onChange={handleInputChange} />
                                                    <button className="btn btn-white border border-secondary px-3" 
                                                        type="button" id="button-addon2" data-mdb-ripple-color="dark"
                                                        onClick={handleClickAddOnBtn}>
                                                        <FontAwesomeIcon icon={faPlus}/>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="row mb-4">
                                            <p className="col-12"><b>Price: ${ceilRound(foodInfo.unitPrice*(1-foodInfo.discountPercent/100)*cartProduct.quantity)}</b></p>
                                        </div>
                                        <a href="#" className="btn btn-warning shadow-0"> Buy now </a>
                                        <button className="btn btn-primary shadow-0 mx-2" onClick={onClickAddToCart}> 
                                            <FontAwesomeIcon icon={faShoppingCart}/> Add to cart 
                                        </button>
                                        {/* <a href="#" className="btn btn-light border border-secondary py-2 icon-hover px-3"> <i className="me-1 fa fa-heart fa-lg"></i> Save </a> */}
                                    </div>
                                </main>
                            </div>
                        </div>
                    </section>            

                    <section className="bg-light border-top py-4">
                        <div className="container">
                            <div className="row gx-4">
                                <div className="col-lg-8 mb-4">
                                    <div className="border rounded-2 px-3 py-2 bg-white">
                                        <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
                                            <li className="nav-item" role="presentation">
                                                <span className="nav-link text-dark active"
                                                    id="all-order-tab" role="tab">Description</span>
                                            </li>
                                        </ul>

                                        <div className="tab-content" id="product-content">
                                            <div dangerouslySetInnerHTML={{__html: foodInfo.description}}></div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-lg-4" id='similar-foods-container'>
                                    <div className="px-0 border rounded-2 shadow-0">
                                        <div className="card">
                                            <div className="card-body">
                                                <h5 className="card-title">Similar products</h5>

                                                {similarFoods.map((food) =>(
                                                    <div className="d-flex mb-3" key={food.productID}>
                                                        <a href={`${foodsPath}/${food.productName}?sp=${food.productID}`} className="me-3">
                                                            <img src={displayProductImage(food.picture)} style={{minWidth:"96px", height:"96px"}} className="img-md img-thumbnail" />
                                                        </a>
                                                        <div className="cart-text w-100">
                                                            <div className="info">
                                                                <div className='col-md-2'>

                                                                </div>
                                                                <a href={`${foodsPath}/${food.productName}?sp=${food.productID}`} className="nav-link mb-1">
                                                                    {food.productName}
                                                                </a>
                                                                <div className='d-flex'>
                                                                    {food.discountID!= null && food.discountPercent>0 &&
                                                                        <del className="text-body-secondary me-2">
                                                                            <span>
                                                                                <span>$</span>
                                                                                {food.unitPrice}
                                                                            </span>
                                                                        </del>
                                                                    }
                                                                    <ins>
                                                                        <span>
                                                                            <b>
                                                                                <span>$</span>
                                                                                {ceilRound(food.unitPrice*(1-food.discountPercent/100))}
                                                                            </b> 
                                                                        </span>
                                                                    </ins>


                                                                    {food.discountID!=null && food.discountPercent>0 &&
                                                                        <div className="sale-tag discount text-white d-flex justify-content-center align-items-center small-ele ms-4">
                                                                            -{food.discountPercent}%
                                                                        </div>
                                                                    }
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </>
            }
        </>
    );
}