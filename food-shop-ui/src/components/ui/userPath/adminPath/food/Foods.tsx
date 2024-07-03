import FoodSection from '../../../../shared/website/sections/foodSection/FoodSection';
import { useEffect, useState } from 'react';
import { CurrentProduct, ExistedProduct, Pageable } from '../../../../../model/Type';
import PaginationSection from '../../../../shared/website/sections/paginationSection/PaginationSection';
import { getPageNumber } from '../../../../../service/Pageable';
import { PathProvider } from '../../../../contexts/PathContext';
import { ADMIN_PRODUCTS } from '../../../../../constant/FoodShoppingURL';
import { getProducts } from '../../../../../api/AdminApi';
import { Link } from 'react-router-dom';
import { displayProductImage } from '../../../../../service/Image';
import { ceilRound } from '../../../../../service/Convert';

export default function AdminFoodsComponent(){
    const [foodlist, setFoodlist] = useState<ExistedProduct[]>([]);
    const [pageable, setPageable] = useState<Pageable>({
        first: false,
        last: false,
        number: 0,
        totalPages: 0
    })
    const pageNumber = getPageNumber()

    async function fetchFoods(pageNumber: number){
        const response = await getProducts(pageNumber);
        if(200<=response.status && response.status<300){
            const data = response.data;
            console.log(data)
            setFoodlist(data.content);
            setPageable({
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            });
        }
    }

    useEffect(()=>{
        initial();
    }, [pageNumber])

    function initial(){
        fetchFoods(pageNumber)
    }


    return(
        <div className="container-fluid container">
            <div className="row">

                <section className="foods-section layout_padding ">
                    <div className="container">
                        <h2 className="custom_heading">Foods</h2>
                        <p className="custom_heading-text">
                            There are many variations of passages of Lorem Ipsum available, but
                            the majority have
                        </p>
                        <div className=" layout_padding2">
                            <div className="card-deck">
                                {foodlist.map((productInfo, index) =>(
                                    <div className="col-md-3 col-sm-6 mb-3" key={index}>
                                        <div className="card position-relative" style={{height:"100%"}}>
                                            {productInfo.discountID!=null && productInfo.discountPercent>0 &&
                                                <div className="position-absolute mt-5">
                                                    <span className="badge rounded-pill badge-discount bg-danger ">
                                                        -{productInfo.discountPercent}%
                                                    </span>
                                                </div>
                                            }
                                            <div className="card-img-top product-card-image-container">
                                                <Link to={`${ADMIN_PRODUCTS}/${productInfo.productName}?sp=${productInfo.productID}`}>
                                                    <img className="w-100 h-100" src={displayProductImage(productInfo.picture)} alt="Card image cap" />
                                                </Link>
                                            </div>
                                            <div className="card-body pt-0 product-card-body">
                                                <span className="w-100 d-block text-body-tertiary">
                                                    {productInfo.categoryName}
                                                </span>
                                                <h5 className="card-title">
                                                    <Link to={`${ADMIN_PRODUCTS}/${productInfo.productName}?sp=${productInfo.productID}`}>
                                                        <div className="card-title">{productInfo.productName}</div>   
                                                    </Link>
                                                </h5>
                                                <span className="card-text w-100 d-block">
                                                    {!productInfo.discontinued ?
                                                        <>
                                                            <ins className="mx-3">
                                                                <span>
                                                                    <b>
                                                                        <span>$</span>
                                                                        {ceilRound(productInfo.unitPrice*(1-productInfo.discountPercent/100))}
                                                                    </b> 
                                                                </span>
                                                            </ins>
                                                            {productInfo.discountID!= null &&
                                                                <del className="text-body-secondary">
                                                                    <span>
                                                                        <span>$</span>
                                                                        {productInfo.unitPrice}
                                                                    </span>
                                                                </del>
                                                            }
                                                        </>:
                                                        <b className='m-0'>Discontinued</b>
                                                    }
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                </section>


                {/* <PathProvider value={ADMIN_PRODUCTS}>
                    <FoodSection lstFoodProducts={foodlist} sectionTitle='Foods'/>
                </PathProvider> */}
            </div>
            <div className="row">
                <PaginationSection pageable={pageable}/>
            </div>
        </div>
    );
}