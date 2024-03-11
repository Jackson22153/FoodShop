import { getLogo } from "../../../../service/image";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { cartPath, categoriesPath, foodsPath } from "../../../../constant/FoodShoppingURL";
import { Category, Product, UserInfo } from "../../../../model/Type";
import { convertNameForUrl, nonBreakingSpace } from "../../../../service/convertStr";
import Search from "../../functions/search/Search";
import { faCartShopping } from "@fortawesome/free-solid-svg-icons";
import AppHeaderUser from "../../functions/appHeaderUser/AppHeaderUser";
import { MouseEventHandler, useContext, useRef } from "react";
import { userInfoContext } from "../../../stateful/homePath/home/Home";
import { LoginUrl } from "../../../../constant/FoodShoppingApiURL";

interface Props{
    lstCategories: Category[]
    searchInputValue: string,
    searchResult: Product[],
    handleSearchResult: any,
    handleIsOpeningUserDropDown: any,
    handleInputSearchChange: any,
}
function HeaderComponent(prop: Props){
    const logo = getLogo();
    const lstCategories = prop.lstCategories;
    const searchInputValue = prop.searchInputValue;
    const searchResult = prop.searchResult;
    const categoriesDropdownRef = useRef<HTMLDivElement>(null);
    const userInfo = useContext(userInfoContext);
    
    const handleIsOpeningUserDropDown = (status: boolean) =>{
        prop.handleIsOpeningUserDropDown(status);
    }

    function onMouseEnterCategories(){
        console.log("vailoz")
        const categoryDropdown = categoriesDropdownRef.current;
        if(categoryDropdown){
            categoryDropdown.classList.toggle('show');
        }
    }
    

    return(
        <header className="header_section">
            <div className="topbar">
				<div className="content-topbar container h-100">
					<div className="left-topbar">
						
					</div>

					<div className="right-topbar">

                        {!userInfo.isAuthenticated ?
                            <>
                                <a href="" className="left-topbar-item">
                                    Sing up
                                </a>

                                <a href={LoginUrl} className="left-topbar-item">
                                    Log in
                                </a>
                            </>:
                            <>
                                <AppHeaderUser handleIsOpeningDropdown={handleIsOpeningUserDropDown}/>
                            </>

                        }
					</div>
				</div>
			</div>
            <div className="container">
                <nav className="navbar navbar-expand-lg custom_nav-container pt-3">
                    <a className="navbar-brand" href="index">
                        <img src={logo} alt="" /><span>
                            Tropiko
                        </span>
                    </a>
                    <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon">
                            {/* <FontAwesomeIcon icon={faBars}/> */}
                        </span>
                    </button>

                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <div className="d-flex ml-auto flex-column flex-lg-row align-items-center">
                            <ul className="navbar-nav  ">
                                <li className="nav-item active">
                                    <a className="nav-link" href="/">Home <span className="sr-only">(current)</span></a>
                                </li>
                                <li className="nav-item">
                                    <a className="nav-link" href={foodsPath}> Foods</a>
                                </li>
                                <li className="nav-item dropdown categories-selector">
                                    <a className="nav-link" id="categoryDropdownmMenuLink" href={categoriesPath}>Categories</a>
                                    <div className="category-dropdown-format">
                                        <div className="nav-dropdown-clippath"></div>
                                        <div className={`dropdown-menu category-dropdown`} aria-labelledby="categoryDropdownmMenuLink">
                                            {lstCategories.map((category, index)=>(
                                                <a key={index} className="dropdown-item category-dropdown-item" 
                                                    href={categoriesPath+"/"+ convertNameForUrl(category.categoryName)}>
                                                    {nonBreakingSpace(category.categoryName)}
                                                </a>
                                            ))}
                                        </div>
                                    </div>
                                </li>
                                {/* <li className="nav-item">
                                    <a className="nav-link" href={contactPath}>Contact us</a>
                                </li> */}
                            </ul>
                            <form className="form-inline my-2 my-lg-0 ml-0 ml-lg-4 mb-3 mb-lg-0 search-form">
                                <Search searchInputValue={searchInputValue} searchResult={searchResult}
                                    handleInputSearchChange={prop.handleInputSearchChange}
                                    handleSearchResult={prop.handleSearchResult}/>
                            </form>
                        </div>
                        <div className="cart_btn-container ml-0 ml-lg-4 d-flex justify-content-center">
                            <a className="cart-link" href={cartPath}>
                                <FontAwesomeIcon icon={faCartShopping}/>
                            </a>
                        </div>
                    </div>

                    {/* <button type="button" className="btn btn-outline-primary">login</button> */}
                </nav>
            </div>
        </header>
    )
}
export default HeaderComponent;

function MutableRefObject<T>(arg0: null) {
    throw new Error("Function not implemented.");
}
