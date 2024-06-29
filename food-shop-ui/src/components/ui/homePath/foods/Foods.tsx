import { useEffect, useState } from "react";
import { CurrentProduct, Pageable } from "../../../../model/Type";
import { getProducts, searchProducts } from "../../../../api/SearchApi";
import PaginationSection from "../../../shared/website/sections/paginationSection/PaginationSection";
import { getPageNumber } from "../../../../service/Pageable";
import { PathProvider } from "../../../contexts/PathContext";
import { FOODS_PATH } from "../../../../constant/FoodShoppingURL";
import FoodCardDeck from "../../../shared/functions/foodCardDeck/FoodCardDeck";
import { useSearchParams } from "react-router-dom";

export default function FoodsComponent(){
    const [foods, setFoods] = useState<CurrentProduct[]>([]);
    const [searchParam] = useSearchParams()
    const searchProduct = searchParam.get('s')
    const pageNumber = getPageNumber();
    const [page, setPage] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    });

    useEffect(()=>{
        initial();
    }, [searchProduct, pageNumber])

    function initial(){
        if(searchProduct){
            fetchSearchingProducts(searchProduct, pageNumber);
        }else{
            fetchProducts(pageNumber);
        }
    }

    // get and handle produts without searching
    async function fetchProducts(pageNumber: number){
        const res = await getProducts(pageNumber);
        if(200<=res.status&&res.status<300){
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
    // get and handle searching produts
    async function fetchSearchingProducts(searchParam: string, pageNumber: number){
        const res = await searchProducts(searchParam, pageNumber)
        if(200<=res.status&&res.status<300){
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

    return(
        <section className="foods-section layout_padding-top">
            <div className="container">
                <h2 className="custom_heading">Foods</h2>
                <p className="custom_heading-text">
                    There are many variations of passages of Lorem Ipsum available, but
                    the majority have
                </p>
                <div className="py-4">
                    <PathProvider value={FOODS_PATH}>
                        <FoodCardDeck foods={foods}/>
                    </PathProvider>
                </div>
                <div className="pb-3">
                    <PaginationSection pageable={page}/>
                </div>
            </div>
        </section>
    );
}