import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getProductsByCategory } from "../../../../api/SearchApi";
import { CurrentProduct, Pageable } from "../../../../model/Type";
import PaginationSection from "../../../shared/website/sections/paginationSection/PaginationSection";
import { getPageNumber } from "../../../../service/Pageable";
import FoodCardDeck from "../../../shared/functions/foodCardDeck/FoodCardDeck";
import { PathProvider } from "../../../contexts/PathContext";
import { FOODS_PATH, NOT_FOUND_ERROR_PAGE } from "../../../../constant/FoodShoppingURL";

export default function CategoryComponent(){
    const navigate = useNavigate()
    const {categoryName} = useParams();
    const [products, setProducts] = useState<CurrentProduct[]>([]);
    const [pageable, setPageable] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    });
    const pageNumber = getPageNumber();

    useEffect(()=>{
        initial();
    }, [categoryName, pageNumber]);

    function initial(){
        fetchProductsByCategory(pageNumber);
    }
    // fetch products based on category
    const fetchProductsByCategory = async (pageNumber: number)=>{
        try {
            const res = await getProductsByCategory(categoryName, pageNumber)
            if(200<= res.status && res.status<300){
                // console.log(res.data);
                const data = res.data;
                setProducts(data.content);
                setPageable({
                    first: data.first,
                    last: data.last,
                    number: data.number,
                    totalPages: data.totalPages
                })
            }
        } catch (error) {
            const statusCode = error.response.status;
            if(statusCode===404) navigate(NOT_FOUND_ERROR_PAGE)
        }
    }

    return(
        <section className="foods-section layout_padding-top">
            <div className="container">
                <h2 className="custom_heading">Foods</h2>

                <div className="py-4">
                    <PathProvider value={FOODS_PATH}>
                        <FoodCardDeck foods={products}/>
                    </PathProvider>
                </div>
                {/* pagination section */}
                <PaginationSection pageable={pageable}/>
            </div>
        </section>
    );
}