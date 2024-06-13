import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getProductsByCategory } from "../../../../api/SearchApi";
import { CurrentProduct, Pageable } from "../../../../model/Type";
import PaginationSection from "../../../shared/website/sections/paginationSection/PaginationSection";
import { getPageNumber } from "../../../../service/Pageable";
import FoodCardDeck from "../../../shared/functions/foodCardDeck/FoodCardDeck";
import { PathProvider } from "../../../contexts/PathContext";
import { foodsPath } from "../../../../constant/FoodShoppingURL";

export default function CategoryComponent(){
    const {categoryName} = useParams();
    const [products, setProducts] = useState<CurrentProduct[]>([]);
    const [pageable, setPageable] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    });

    useEffect(()=>{
        initial();
    }, []);

    function initial(){
        const pageNumber = getPageNumber();
        fetchProductsByCategory(pageNumber);
    }

    const fetchProductsByCategory = async (pageNumber: number)=>{
        const res = await getProductsByCategory(categoryName, pageNumber)
        if(res.status===200){
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
                        <FoodCardDeck foods={products}/>
                    </PathProvider>
                </div>
                {/* pagination section */}
                <PaginationSection pageable={pageable}/>
            </div>
        </section>
    );
}