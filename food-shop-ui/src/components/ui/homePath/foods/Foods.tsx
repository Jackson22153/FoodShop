import { useEffect, useState } from "react";
import { Pageable, Product } from "../../../../model/Type";
import FoodCard from "../../../stateless/function/foodCard/FoodCard";
import { getProducts, getProductsByProductName, searchProducts } from "../../../../api/SearchApi";
import PaginationSection from "../../../stateless/section/paginationSection/PaginationSection";

export default function FoodsComponent(){
    const [foods, setFoods] = useState([]);
    const urlParams = new URLSearchParams(window.location.search);
    const [page, setPage] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    });

    useEffect(()=>{
        initial();
    }, [])


    function getSearchParam(){
        const searchParam = urlParams.get('s');
        return searchParam;
    }
    
    // get page
    function getPageNumber(){
        const pageParam = urlParams.get('page');
        const pageNumberStr = pageParam?pageParam:'0';
        const pageNumber = !isNaN(+pageNumberStr)?+pageNumberStr:0;
        return pageNumber;
    }

    // get and handle produts without searching
    function handleProducts(pageNumber: number){
        getProducts(pageNumber).then(res =>{
            const data = res.data;
            setFoods(data.content);
            return data;
        }).then(data =>{
            const pageable = {
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            }
            setPage(pageable);
        })
    }
    // get and handle searching produts
    function handleSearchingProducts(searchParam: string, pageNumber: number){
        getProductsByProductName(searchParam, pageNumber).then(res =>{
            const data = res.data;
            setFoods(data.content);
            return data;
        }).then(data =>{
            const pageable = {
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            }
            setPage(pageable);
        })
    }

    function initial(){
        const pageNumber = getPageNumber();
        const searchParam = getSearchParam();
        if(searchParam){
            handleSearchingProducts(searchParam, pageNumber);
        }else{
            handleProducts(pageNumber);
        }

        
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
                        {foods.length>0 && foods.map((productInfo:Product, index) =>(
                            <div className="col-md-4 col-sm-6 row-md-5 mb-3" key={index}>
                                <FoodCard foodName={productInfo.productName} foodID={productInfo.productID}
                                    foodImageSrc={productInfo.picture}
                                />
                            </div>
                        ))}
                    </div>
                </div>
                {/* pagination section */}
                <PaginationSection pageable={page}/>
            </div>
        </section>
    );
}