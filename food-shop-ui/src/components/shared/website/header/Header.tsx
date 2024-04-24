import { getLogo } from "../../../../service/image";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { cartPath, categoriesPath, customerPath, foodsPath } from "../../../../constant/FoodShoppingURL";
import { Category, Product, UserInfo } from "../../../../model/Type";
import { convertNameForUrl, nonBreakingSpace } from "../../../../service/convertStr";
import Search from "../../functions/search/Search";
import { faCartShopping } from "@fortawesome/free-solid-svg-icons";
import { useContext } from "react";
import { LoginUrl } from "../../../../constant/FoodShoppingApiURL";
import userInfoContext from "../../../contexts/UserInfoContext";
import { logout } from "../../../../api/AuthorizationApi";
import { isOpeningUserDropDownContext } from "../../../ui/homePath/home/Home";

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
    const isOpeningDropdown = useContext(isOpeningUserDropDownContext);
    const userInfo = useContext<UserInfo>(userInfoContext);

    const onClickIsOpenDropdown = ()=>{
        prop.handleIsOpeningUserDropDown(!isOpeningDropdown);
    }

    const onClickLogout = ()=>{
        logout().then((_res) =>{

            window.location.href = "/";
        })
    }
    

    return(
        <header className="header-section bg-white">
            <div className="topbar">
				<div className="content-topbar container h-100">
					<div className="left-topbar">
						
					</div>

					<div className="right-topbar">

                        {userInfo.isAuthenticated ?
                            <div className="navbar-expand-lg navbar-light text-white">
                                <div className="collapse navbar-collapse" id="navbarNavDropdown">
                                    <ul className="navbar-nav ml-auto">
                                        <li className="nav-item dropdown" onMouseEnter={onClickIsOpenDropdown} onMouseLeave={onClickIsOpenDropdown}>
                                            <span className="nav-link dropdown-toggle" id="navbarDropdownMenuLink" role="button" 
                                                data-toggle="dropdown" aria-haspopup={isOpeningDropdown} aria-expanded={isOpeningDropdown} >
                                                {userInfo.username}
                                            </span>
                                            {isOpeningDropdown &&
                                                <div id="appheader-user" className={`dropdown-menu show`} 
                                                    aria-labelledby="navbarDropdownMenuLink">
                                                    <a className="dropdown-item cursor-pointer" href={customerPath}>Profile</a>
                                                    <a className="dropdown-item cursor-pointer" href="#">Settings</a>
                                                    <div className="dropdown-divider"></div>
                                                    <span className="dropdown-item cursor-pointer" onClick={onClickLogout}>Logout</span>
                                                </div>
                                            }
                                        </li>
                                    </ul>
                                </div>
                            </div>:
                            <ul className="nav-fill nav">
                                <li className="nav-item">
                                    <a href="" className="text-light nav-link">Sing up</a>
                                </li>
                                <li className="nav-item">
                                    <a href={LoginUrl} className="text-light nav-link">Log in</a>
                                </li>
                            </ul>
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
                            </ul>
                            <form className="form-inline my-2 my-lg-0 ml-0 ml-lg-4 mb-3 mb-lg-0 search-form">
                                <Search searchInputValue={searchInputValue} searchResult={searchResult}
                                    handleInputSearchChange={prop.handleInputSearchChange}
                                    handleSearchResult={prop.handleSearchResult}/>
                            </form>
                        </div>
                        <div className=" ml-0 ml-lg-4 d-flex justify-content-center  position-relative d-flex align-items-center">
                            <a className="btn btn-light ms-3 cart-icon cart-link" href={cartPath}>
                                <FontAwesomeIcon icon={faCartShopping}/>
                                {/* <span className="cart-badge badge rounded-pill badge-notification bg-danger">9</span> */}
                            </a>
                        </div>
                    </div>
                </nav>
            </div>
        </header>
    )
}
export default HeaderComponent;
