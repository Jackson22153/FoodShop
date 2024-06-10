import HeaderComponent from "../../../shared/website/header/Header";
import { Routes, Route } from "react-router-dom";
import './Home.css';
import FooterComponent from "../../../shared/website/footer/Footer";
import HomeDashBoardComponent from "../homeDashBoard/HomeDashBoard";
import FoodsComponent from "../foods/Foods";
import ContactUsComponent from "../contact/ContactUs";
import FoodComponent from "../food/Food";
import CartComponent from "../cart/Cart";
import { cartPath, categoriesPath, contactPath, foodsPath, orderPath } from "../../../../constant/FoodShoppingURL";
import { useEffect, useState } from "react";
import { getCategories } from "../../../../api/SearchApi";
import { Category } from "../../../../model/Type";
import CategoriesComponent from "../categories/Categories";
import CategoryComponent from "../category/Category";
import { useCookies } from 'react-cookie'
import { NumberOfCartProductsProvider } from "../../../contexts/NumberOfCartProductsContext";
import { CategoriesProvider } from "../../../contexts/CategoriesContext";
import OrderComponent from "../order/Order";
import {getNumberOfCartProducts} from "../../../../service/cookie"

function HomeComponent(){
    const [cookies] = useCookies();
    const [categories, setCategories] = useState<Category[]>([]);
    const [numberOfCartProducts, setNumberOfCartProducts] = useState(0);
    // const [isExpanded, setIsExpanded] = useState(false); 


    useEffect(()=>{
        initial();
    }, []);

    function initial(){
        // get categories
        fetchCategories();
        // get numberOfCartProducts
        fetchNumberOfCartProducts();
    }

    
    // get number of cart products
    const fetchNumberOfCartProducts = ()=>{
        const numberOfCartProducts = getNumberOfCartProducts(cookies.cart);
        setNumberOfCartProducts(numberOfCartProducts);
    }

    const fetchCategories = async ()=>{
        const res = await getCategories(0)
        if(res.status){
            const data = res.data;
            setCategories(data.content);
        }
    }

 

    return(
        <NumberOfCartProductsProvider value={{ numberOfCartProducts, setNumberOfCartProducts }}>
            <HeaderComponent lstCategories={categories}/>

            <CategoriesProvider value={categories}>
                <div id="home-body">
                    <Routes>
                        <Route path="*" element={<HomeDashBoardComponent/>}/>
                        <Route path={categoriesPath} element={<CategoriesComponent/>}/>
                        <Route path={`${categoriesPath}/:categoryName`} element={<CategoryComponent/>}/>
                        <Route path={foodsPath} element={<FoodsComponent/>}/>
                        <Route path={contactPath} element={<ContactUsComponent/>}/>
                        <Route path={`${foodsPath}/:foodName`} element={<FoodComponent/>}/>
                        <Route path={cartPath} element={<CartComponent/>}/>
                        <Route path={orderPath} element={<OrderComponent/>}/>
                    </Routes>
                </div>
            </CategoriesProvider>
            <FooterComponent/>
        </NumberOfCartProductsProvider>
    )
}
export default HomeComponent;