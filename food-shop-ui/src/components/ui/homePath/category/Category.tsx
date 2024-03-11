import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getProductsByCategory } from "../../../../api/SearchApi";
import { Pageable, Product } from "../../../../model/Type";
import FoodCard from "../../../stateless/function/foodCard/FoodCard";
import PaginationSection from "../../../stateless/section/paginationSection/PaginationSection";
import { categoriesPath } from "../../../../constant/FoodShoppingURL";

export default function CategoryComponent(){
    const {categoryName} = useParams();
    const urlParams = new URLSearchParams(window.location.search);
    const [products, setProducts] = useState<Product[]>([]);
    const [pageable, setPageable] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    });

    useEffect(()=>{
        initial();
    }, []);

    function getPageNumber(){
        const pageParam = urlParams.get('page');
        const pageNumberStr = pageParam?pageParam:'0';
        const pageNumber = !isNaN(+pageNumberStr)?+pageNumberStr:0;
        return pageNumber;
    }

    function initial(){
        const pageNumber = getPageNumber();
        getProductsByCategory(categoryName, pageNumber)
        .then(res =>{
            if(res.status===200){
                console.log(res.data);
                const data = res.data;
                setProducts(data.content);
                setPageable({
                    first: data.first,
                    last: data.last,
                    number: data.number,
                    totalPages: data.totalPages
                })
            }
        });
    }

    return(
        <section className="service_section layout_padding-top">
            <div className="container">
                <h2 className="custom_heading">Foods</h2>
                <p className="custom_heading-text">
                    There are many variations of passages of Lorem Ipsum available, but
                    the majority have
                </p>
                <div className=" layout_padding2">
                    <div className="card-deck">
                        {products.length>0 && products.map((productInfo:Product, index) =>(
                            <div className="col-md-4 col-sm-6 row-md-5 mb-3" key={index}>
                                <FoodCard foodName={productInfo.productName} foodID={productInfo.productID}
                                    foodImageSrc={productInfo.picture}
                                />
                            </div>
                        ))}
                    </div>
                </div>
                {/* pagination section */}
                <PaginationSection pageable={pageable}/>
            </div>
        </section>
    );
}