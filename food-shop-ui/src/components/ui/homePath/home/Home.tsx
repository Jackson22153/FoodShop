import HeaderComponent from "../../../shared/website/header/Header";
import { Routes, Route } from "react-router-dom";
import './Home.css';
// import '../../../../css/bootstrap.css';
// import '../../../../css/responsive.css';
import FooterComponent from "../../../shared/website/footer/Footer";
import HomeDashBoardComponent from "../homeDashBoard/HomeDashBoard";
import FoodsComponent from "../foods/Foods";
import ContactUsComponent from "../contact/ContactUs";
import FoodComponent from "../food/Food";
import CartComponent from "../cart/Cart";
import { cartPath, categoriesPath, contactPath, foodsPath } from "../../../../constant/FoodShoppingURL";
import { ChangeEventHandler, createContext, 
    useEffect, useState } 
    from "react";
import { getCategories } from "../../../../api/SearchApi";
import { Category, Product, UserInfo } from "../../../../model/Type";
import CategoriesComponent from "../categories/Categories";
import CategoryComponent from "../category/Category";
import { getUsername, isAuthenticated } from "../../../../api/AuthorizationApi";
import { UserInfoProvider } from "../../../contexts/UserInfoContext";

export const isOpeningUserDropDownContext = createContext(false);
// export const userInfoContext = createContext<UserInfo>({
//     username: "",
//     isAuthenticated: false
// })

function HomeComponent(){
    const [categories, setCategories] = useState<Category[]>([]);
    const [searchInputValue, setSearchInputValue] = useState('');
    const [searchResult, setSearchResult] = useState<Product[]>([]);
    const [userInfo, setUserInfo] = useState<UserInfo>({
        username: "",
        isAuthenticated: false
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
        // check authentication
        var isAuthen = false;
        isAuthenticated().then(res =>{
            // console.log(res.data)
            const data = res.data;
            console.log(data)
            if(data.isAuthenticated) isAuthen=true;
            setUserInfo({
                username: data.username,
                isAuthenticated: data.authenticated
            })
            // if(res.data===200){
            // }
        })

        // get categories
        getCategories(0).then(res =>{
            if(res.status===200){
                const data = res.data;
                setCategories(data.content);
            }
        });

        // checkauthenticated
        if(userInfo.isAuthenticated){
            // get username
            getUsername().then(res =>{
                if(res.status===200){
                    const data = res.data;
                    console.log(data)
                }
            })
        }
    }

    return(
        <>
            <isOpeningUserDropDownContext.Provider value={isOpeningUserDropDown}>
                <UserInfoProvider value={userInfo}>
                    <HeaderComponent lstCategories={categories} searchInputValue={searchInputValue} 
                        handleInputSearchChange={handleInputSearchChange} searchResult={searchResult}
                        handleIsOpeningUserDropDown={handleIsOpeningUserDropDown}
                        handleSearchResult={handleSearchResult} />
                </UserInfoProvider>
            </isOpeningUserDropDownContext.Provider>
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
            <FooterComponent/>
        </>
    )
}
export default HomeComponent;