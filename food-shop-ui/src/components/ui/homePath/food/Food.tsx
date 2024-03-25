import { useParams } from 'react-router-dom';
import './Food.css'
import { ChangeEventHandler, useEffect, useState } from 'react';
import { getProductByID } from '../../../../api/SearchApi';
import { CartProduct, Category, Product } from '../../../../model/Type';
import { cartPath } from '../../../../constant/FoodShoppingURL';

export default function FoodComponent(){
    const urlParams = new URLSearchParams(window.location.search);
    const {foodName} = useParams();
    const cre = "https://mdbootstrap.com/snippets/standard/mdbootstrap/4852176?view=project"
    const [foodInfo, setFoodInfo] = useState<Product>({
        productID: 0,
        productName: "",
        quantityPerUnit: 0,
        unitPrice: 0,
        unitsInStock: 0,
        unitsOnOrder: 0,
        reorderLevel: 0,
        discontinued: true,
        picture: ""
    });
    const [category, setCategory] = useState<Category>({
        categoryID: 0,
        categoryName: '',
        description: '',
        picture: ''
    })
    const [supplier, setSupplier] = useState({
        companyName: ''
    })
    const [cartProduct, setCartProduct] = useState<CartProduct>({
        productID: 0,
        productName: '',
        discount: 0,
        quantity: 0
    })

    useEffect(()=>{
        initial();
    }, []);

    function handleClickMinusBtn(){
        // const limit = foodInfo.unitsInStock;
        // console.log("click")
        
        setCartProduct((product)=>({
            ...product,
            quantity: product.quantity-1>=0?product.quantity-1:0,
        }))

    }
    function handleClickAddOnBtn(){
        // const limit = foodInfo.unitsInStock;
        // console.log("click")
        
        const limit = foodInfo.unitsInStock;
        setCartProduct((product)=>({...product, quantity: 
            product.quantity+1<=limit?product.quantity+1:limit
        }))

    }

    const handleInputChange: ChangeEventHandler<HTMLInputElement> = (event) => {
        const quantity = +event.target.value;
        // console.log(event.target.value);
        setCartProduct({...cartProduct, quantity: quantity});

      };


    function initial(){
        const productID = urlParams.get('sp');
        getProductByID(productID).then(res =>{
            const data = res.data;
            console.log(res.data);
            setFoodInfo(data);
            setCategory(data.categoryID);
            setSupplier(data.supplierID);
        })
        // console.log(foodName);
    }


    return(
        <>
            <section className="py-5">
                <div className="container">
                    <div className="row gx-5">
                        <aside className="col-lg-6">
                            <div className="border rounded-4 mb-3 d-flex justify-content-center">
                                    <img style={{maxWidth: "100%", maxHeight: "100vh", margin: "auto"}} className="rounded-4 fit" src={foodInfo.picture} />
                                {/* <a data-fslightbox="mygalley" className="rounded-4" target="_blank" data-type="image" href="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/detail1/big.webp">
                                </a> */}
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
                            {/* <!-- thumbs-wrap.// --> */}
                            {/* <!-- gallery-wrap .end// --> */}
                        </aside>
                        <main className="col-lg-6">
                            {foodInfo &&
                                <div className="ps-lg-3">
                                    <h4 className="title text-dark">
                                        {foodInfo.productName}
                                    </h4>
                                    <div className="d-flex flex-row my-3">
                                        {/* <div className="text-warning mb-1 me-2">
                                            <i className="fa fa-star"></i>
                                            <i className="fa fa-star"></i>
                                            <i className="fa fa-star"></i>
                                            <i className="fa fa-star"></i>
                                            <i className="fas fa-star-half-alt"></i>
                                            <span className="ms-1">
                                                4.5
                                            </span>
                                        </div> */}
                                        {/* <span className="text-muted">
                                            <i className="fas fa-shopping-basket fa-sm mx-1"></i>154 orders
                                        </span> */}
                                        <span className="text-success ms-2">
                                            {foodInfo.unitsInStock>0? "In stock": "Out of stock"}
                                        </span>
                                    </div>

                                    <div className="mb-3">
                                        <span className="h5">{`$${foodInfo.unitPrice}`}</span>
                                        <span className="text-muted">/per {foodInfo.quantityPerUnit}</span>
                                    </div>

                                    <p>
                                        Modern look and quality demo item is a streetwear-inspired collection that continues to break away from the conventions of mainstream fashion. Made in Italy, these black and brown clothing low-top shirts for
                                        men.
                                    </p>

                                    <div className="row">
                                        <dt className="col-3">Category:</dt>
                                        <dd className="col-9">{category.categoryName}</dd>

                                        <dt className="col-3">Supplier:</dt>
                                        <dd className="col-9">{supplier.companyName}</dd>
                                    </div>

                                    <hr />

                                    <div className="row mb-4">
                                        {/* <div className="col-md-4 col-6">
                                            <label className="mb-2">Size</label>
                                            <select className="form-select border border-secondary" style={{height: "35px"}}>
                                                <option>Small</option>
                                                <option>Medium</option>
                                                <option>Large</option>
                                            </select>
                                        </div> */}
                                        {/* <!-- col.// --> */}
                                        <div className="col-md-4 col-6 mb-3">
                                            <label className="mb-2 d-block">Quantity</label>
                                            <div className="input-group mb-3" style={{width: "170px"}}>
                                                <button className="btn btn-white border border-secondary px-3" 
                                                    type="button" id="button-addon1" data-mdb-ripple-color="dark"
                                                    onClick={handleClickMinusBtn}>
                                                    <i className="fas fa-minus"></i>
                                                </button>
                                                <input type="text" className="form-control text-center border border-secondary" 
                                                    value={cartProduct.quantity} aria-label="Example text with button addon" 
                                                    aria-describedby="button-addon1" onChange={handleInputChange} />
                                                <button className="btn btn-white border border-secondary px-3" 
                                                    type="button" id="button-addon2" data-mdb-ripple-color="dark"
                                                    onClick={handleClickAddOnBtn}>
                                                    <i className="fas fa-plus"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="#" className="btn btn-warning shadow-0"> Buy now </a>
                                    <a href={cartPath} className="btn btn-primary shadow-0"> 
                                        <i className="me-1 fa fa-shopping-basket"></i> Add to cart 
                                    </a>
                                    {/* <a href="#" className="btn btn-light border border-secondary py-2 icon-hover px-3"> <i className="me-1 fa fa-heart fa-lg"></i> Save </a> */}
                                </div>
                            }
                        </main>
                    </div>
                </div>
            </section>            

            <section className="bg-light border-top py-4">
                <div className="container">
                    <div className="row gx-4">
                        <div className="col-lg-8 mb-4">
                            <div className="border rounded-2 px-3 py-2 bg-white">
                                {/* <!-- Pills navs --> */}
                                {/* <ul className="nav nav-pills nav-justified mb-3" id="ex1" role="tablist">
                                    <li className="nav-item d-flex" role="presentation">
                                        <a className="nav-link d-flex align-items-center justify-content-center w-100 active" id="ex1-tab-1" data-mdb-toggle="pill" href="#ex1-pills-1" role="tab" aria-controls="ex1-pills-1" aria-selected="true">Specification</a>
                                    </li>
                                    <li className="nav-item d-flex" role="presentation">
                                        <a className="nav-link d-flex align-items-center justify-content-center w-100" id="ex1-tab-2" data-mdb-toggle="pill" href="#ex1-pills-2" role="tab" aria-controls="ex1-pills-2" aria-selected="false">Warranty info</a>
                                    </li>
                                    <li className="nav-item d-flex" role="presentation">
                                        <a className="nav-link d-flex align-items-center justify-content-center w-100" id="ex1-tab-3" data-mdb-toggle="pill" href="#ex1-pills-3" role="tab" aria-controls="ex1-pills-3" aria-selected="false">Shipping info</a>
                                    </li>
                                    <li className="nav-item d-flex" role="presentation">
                                        <a className="nav-link d-flex align-items-center justify-content-center w-100" id="ex1-tab-4" data-mdb-toggle="pill" href="#ex1-pills-4" role="tab" aria-controls="ex1-pills-4" aria-selected="false">Seller profile</a>
                                    </li>
                                </ul> */}
                                {/* <!-- Pills navs --> */}

                                {/* <!-- Pills content --> */}
                                <div className="tab-content" id="ex1-content">
                                    <div className="tab-pane fade show active" id="ex1-pills-1" role="tabpanel" aria-labelledby="ex1-tab-1">
                                        <p>
                                            With supporting text below as a natural lead-in to additional content. Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut
                                            enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla
                                            pariatur.
                                        </p>
                                        <div className="row mb-2">
                                            <div className="col-12 col-md-6">
                                                <ul className="list-unstyled mb-0">
                                                    <li><i className="fas fa-check text-success me-2"></i>Some great feature name here</li>
                                                    <li><i className="fas fa-check text-success me-2"></i>Lorem ipsum dolor sit amet, consectetur</li>
                                                    <li><i className="fas fa-check text-success me-2"></i>Duis aute irure dolor in reprehenderit</li>
                                                    <li><i className="fas fa-check text-success me-2"></i>Optical heart sensor</li>
                                                </ul>
                                            </div>
                                            <div className="col-12 col-md-6 mb-0">
                                                <ul className="list-unstyled">
                                                    <li><i className="fas fa-check text-success me-2"></i>Easy fast and ver good</li>
                                                    <li><i className="fas fa-check text-success me-2"></i>Some great feature name here</li>
                                                    <li><i className="fas fa-check text-success me-2"></i>Modern style and design</li>
                                                </ul>
                                            </div>
                                        </div>
                                        {/* <table className="table border mt-3 mb-2">
                                            <tr>
                                            <th className="py-2">Display:</th>
                                            <td className="py-2">13.3-inch LED-backlit display with IPS</td>
                                            </tr>
                                            <tr>
                                            <th className="py-2">Processor capacity:</th>
                                            <td className="py-2">2.3GHz dual-core Intel Core i5</td>
                                            </tr>
                                            <tr>
                                            <th className="py-2">Camera quality:</th>
                                            <td className="py-2">720p FaceTime HD camera</td>
                                            </tr>
                                            <tr>
                                            <th className="py-2">Memory</th>
                                            <td className="py-2">8 GB RAM or 16 GB RAM</td>
                                            </tr>
                                            <tr>
                                            <th className="py-2">Graphics</th>
                                            <td className="py-2">Intel Iris Plus Graphics 640</td>
                                            </tr>
                                        </table> */}
                                    </div>
                                    <div className="tab-pane fade mb-2" id="ex1-pills-2" role="tabpanel" aria-labelledby="ex1-tab-2">
                                        Tab content or sample information now <br />
                                        Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut
                                        aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui
                                        officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis
                                        nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
                                    </div>
                                    <div className="tab-pane fade mb-2" id="ex1-pills-3" role="tabpanel" aria-labelledby="ex1-tab-3">
                                        Another tab content or sample information now <br />
                                        Dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
                                        commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt
                                        mollit anim id est laborum.
                                    </div>
                                    <div className="tab-pane fade mb-2" id="ex1-pills-4" role="tabpanel" aria-labelledby="ex1-tab-4">
                                        Some other tab content or sample information now <br />
                                        Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut
                                        aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui
                                        officia deserunt mollit anim id est laborum.
                                    </div>
                                </div>
                                {/* <!-- Pills content --> */}
                            </div>
                        </div>
                        <div className="col-lg-4">
                            <div className="px-0 border rounded-2 shadow-0">
                                <div className="card">
                                    <div className="card-body">
                                    <h5 className="card-title">Similar items</h5>
                                    <div className="d-flex mb-3">
                                        <a href="#" className="me-3">
                                        <img src="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/8.webp" style={{minWidth:"96px", height:"96px"}} className="img-md img-thumbnail" />
                                        </a>
                                        <div className="info">
                                        <a href="#" className="nav-link mb-1">
                                            Rucksack Backpack Large <br />
                                            Line Mounts
                                        </a>
                                        <strong className="text-dark"> $38.90</strong>
                                        </div>
                                    </div>

                                    <div className="d-flex mb-3">
                                        <a href="#" className="me-3">
                                        <img src="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/9.webp" style={{minWidth:"96px", height:"96px"}} className="img-md img-thumbnail" />
                                        </a>
                                        <div className="info">
                                        <a href="#" className="nav-link mb-1">
                                            Summer New Men's Denim <br />
                                            Jeans Shorts
                                        </a>
                                        <strong className="text-dark"> $29.50</strong>
                                        </div>
                                    </div>

                                    <div className="d-flex mb-3">
                                        <a href="#" className="me-3">
                                        <img src="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/10.webp" style={{minWidth:"96px", height:"96px"}} className="img-md img-thumbnail" />
                                        </a>
                                        <div className="info">
                                        <a href="#" className="nav-link mb-1"> T-shirts with multiple colors, for men and lady </a>
                                        <strong className="text-dark"> $120.00</strong>
                                        </div>
                                    </div>

                                    <div className="d-flex">
                                        <a href="#" className="me-3">
                                        <img src="https://bootstrap-ecommerce.com/bootstrap5-ecommerce/images/items/11.webp" style={{minWidth:"96px", height:"96px"}} className="img-md img-thumbnail" />
                                        </a>
                                        <div className="info">
                                        <a href="#" className="nav-link mb-1"> Blazer Suit Dress Jacket for Men, Blue color </a>
                                        <strong className="text-dark"> $339.90</strong>
                                        </div>
                                    </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
        // <section className="product col-1 mt-3">
        //     <div className="product__photo">
        //         <div className="photo-container">
        //             <div className="photo-main">
        //                 <img src="https://res.cloudinary.com/john-mantas/image/upload/v1537291846/codepen/delicious-apples/green-apple-with-slice.png" alt="green apple slice"/>
        //             </div>
        //             <div className="photo-album">
        //                 <ul>
        //                     <li><img src="https://res.cloudinary.com/john-mantas/image/upload/v1537302064/codepen/delicious-apples/green-apple2.png" alt="green apple"/></li>
        //                     <li><img src="https://res.cloudinary.com/john-mantas/image/upload/v1537303532/codepen/delicious-apples/half-apple.png" alt="half apple"/></li>
        //                     <li><img src="https://res.cloudinary.com/john-mantas/image/upload/v1537303160/codepen/delicious-apples/green-apple-flipped.png" alt="green apple"/></li>
        //                     <li><img src="https://res.cloudinary.com/john-mantas/image/upload/v1537303708/codepen/delicious-apples/apple-top.png" alt="apple top"/></li>
        //                 </ul>
        //             </div>
        //         </div>
        //     </div>
        //     <div className="product__info">
        //         <div className="title">
        //             <h1>Delicious Apples</h1>
        //             <span>COD: 45999</span>
        //         </div>
        //         <div className="price">
        //             R$ <span>7.93</span>
        //         </div>
        //         <div className="variant">
        //             <h3>SELECT A COLOR</h3>
        //             <ul>
        //                 <li><img src="https://res.cloudinary.com/john-mantas/image/upload/v1537302064/codepen/delicious-apples/green-apple2.png" alt="green apple"/></li>
        //                 <li><img src="https://res.cloudinary.com/john-mantas/image/upload/v1537302752/codepen/delicious-apples/yellow-apple.png" alt="yellow apple"/></li>
        //                 <li><img src="https://res.cloudinary.com/john-mantas/image/upload/v1537302427/codepen/delicious-apples/orange-apple.png" alt="orange apple"/></li>
        //                 <li><img src="https://res.cloudinary.com/john-mantas/image/upload/v1537302285/codepen/delicious-apples/red-apple.png" alt="red apple"/></li>
        //             </ul>
        //         </div>
        //         <div className="description">
        //             <h3>BENEFITS</h3>
        //             <ul>
        //                 <li>Apples are nutricious</li>
        //                 <li>Apples may be good for weight loss</li>
        //                 <li>Apples may be good for bone health</li>
        //                 <li>They're linked to a lowest risk of diabetes</li>
        //             </ul>
        //         </div>
        //         <button className="buy--btn">ADD TO CART</button>
        //     </div>
        // </section>
    );
}