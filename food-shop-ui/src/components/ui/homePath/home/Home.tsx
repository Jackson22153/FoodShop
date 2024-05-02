import HeaderComponent from "../../../shared/website/header/Header";
import { Routes, Route } from "react-router-dom";
import './Home.css';
import FooterComponent from "../../../shared/website/footer/Footer";
import HomeDashBoardComponent from "../homeDashBoard/HomeDashBoard";
import FoodsComponent from "../foods/Foods";
import ContactUsComponent from "../contact/ContactUs";
import FoodComponent from "../food/Food";
import CartComponent from "../cart/Cart";
import { cartPath, categoriesPath, contactPath, foodsPath } from "../../../../constant/FoodShoppingURL";
import { ChangeEventHandler, createContext, useEffect, useState } from "react";
import { useCookies } from 'react-cookie';
import { getCategories } from "../../../../api/SearchApi";
import { Category, Product } from "../../../../model/Type";
import CategoriesComponent from "../categories/Categories";
import CategoryComponent from "../category/Category";
import { NumberOfCartProductsProvider } from "../../../contexts/NumberOfCartProductsContext";
import { getNumberOfCartProducts } from "../../../../service/cookie";
import { CategoriesProvider } from "../../../contexts/CategoriesContext";
import { Modal } from "../../../../model/WebType";
import { logout } from "../../../../api/AuthorizationApi";

export const isOpeningUserDropDownContext = createContext(false);
function HomeComponent(){
    const [cartCookie] = useCookies(['cart']);
    const [categories, setCategories] = useState<Category[]>([]);
    const [searchInputValue, setSearchInputValue] = useState('');
    const [searchResult, setSearchResult] = useState<Product[]>([]);
    const [numberOfCartProducts, setNumberOfCartProducts] = useState(0);
    const [modal, setModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })
    
    const [isOpeningUserDropDown, setIsOpeningUserDropDown] = useState<boolean>(false);



    useEffect(()=>{
        initial();
    }, []);

    const handleIsOpeningUserDropDown = (status:boolean)=>{
        setIsOpeningUserDropDown(status);
    }

    const handleInputSearchChange : ChangeEventHandler<HTMLInputElement> = event =>{
        const value = event.target.value;
        setSearchInputValue(value);
    }

    function handleSearchResult(searcResult: Product[]){
        setSearchResult(searcResult);
    }

    function initial(){
        // get categories
        fetchCategories();
        // get numberOfCartProducts
        fetchedNumberOfCartProducts();
    }



    const fetchedNumberOfCartProducts = ()=>{
        const numberOfCartProducts = getNumberOfCartProducts(cartCookie.cart)
        setNumberOfCartProducts(numberOfCartProducts);
    }

    const fetchCategories = async ()=>{
        const res = await getCategories(0)
        if(res.status===200){
            const data = res.data;
            setCategories(data.content);
        }
    }

    const toggleModal = ()=>{
        setModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }

    return(
        <NumberOfCartProductsProvider value={{ numberOfCartProducts, setNumberOfCartProducts }}>
            <isOpeningUserDropDownContext.Provider value={isOpeningUserDropDown}>
                <HeaderComponent lstCategories={categories} searchInputValue={searchInputValue} 
                    handleInputSearchChange={handleInputSearchChange} searchResult={searchResult}
                    handleIsOpeningUserDropDown={handleIsOpeningUserDropDown}
                    handleSearchResult={handleSearchResult} modal={modal}
                    toggleModal={toggleModal}/>
            </isOpeningUserDropDownContext.Provider>

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
                    </Routes>
                </div>
            </CategoriesProvider>
            <FooterComponent/>
        </NumberOfCartProductsProvider>
    )
}
export default HomeComponent;