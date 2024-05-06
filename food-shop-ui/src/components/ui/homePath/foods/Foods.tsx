import { useEffect, useState } from "react";
import { CurrentProduct, Pageable } from "../../../../model/Type";
import { getProducts, searchProducts } from "../../../../api/SearchApi";
import PaginationSection from "../../../shared/website/sections/paginationSection/PaginationSection";
import { getPageNumber } from "../../../../service/pageable";
import { PathProvider } from "../../../contexts/PathContext";
import { foodsPath } from "../../../../constant/FoodShoppingURL";
import FoodCardDeck from "../../../shared/functions/foodCardDeck/FoodCardDeck";

export default function FoodsComponent(){
    const [foods, setFoods] = useState<CurrentProduct[]>([]);
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

    // get and handle produts without searching
    async function fetchProducts(pageNumber: number){
        const res = await getProducts(pageNumber);
        if(res.status){
            const data = res.data;
            setFoods(data.content);
            const pageable = {
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            }
            setPage(pageable);
            // console.log(data)
        }
    }
    // get and handle searching produts
    async function fetchSearchingProducts(searchParam: string, pageNumber: number){
        const res = await searchProducts(searchParam, pageNumber)
        if(res.status){
            const data = res.data;
            setFoods(data.content);
            const pageable = {
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            }
            setPage(pageable);

        }
    }

    function initial(){
        const pageNumber = getPageNumber();
        const searchParam = getSearchParam();
        if(searchParam){
            fetchSearchingProducts(searchParam, pageNumber);
        }else{
            fetchProducts(pageNumber);
        }

        
    }
    return(
        <section className="foods-section layout_padding-top">
            <div className="container">
                <h2 className="custom_heading">Foods</h2>
                <p className="custom_heading-text">
                    There are many variations of passages of Lorem Ipsum available, but
                    the majority have
                </p>
                <div className="py-4">
                    <PathProvider value={foodsPath}>
                        <FoodCardDeck foods={foods}/>
                    </PathProvider>
                </div>
                {/* pagination section */}
                <PaginationSection pageable={page}/>
            </div>
        </section>
    );
}