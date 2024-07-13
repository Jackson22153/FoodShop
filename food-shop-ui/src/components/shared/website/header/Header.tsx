import { getLogo } from "../../../../service/Image";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { CART_PATH, CATEGORIES_PATH, FOODS_PATH } from "../../../../constant/FoodShoppingURL";
import { Category, UserInfo } from "../../../../model/Type";
import { convertNameForUrl, nonBreakingSpace } from "../../../../service/Convert";
import Search from "../../functions/search/Search";
import { faCartShopping } from "@fortawesome/free-solid-svg-icons";
import { memo, useContext, useEffect, useRef, useState } from "react";
import { LoginUrl } from "../../../../constant/FoodShoppingApiURL";
import numberOfCartProductsContext from "../../../contexts/NumberOfCartProductsContext";
import UserInfoNav from "../../functions/userinfo-nav/UserInfoNav";
import userInfoContext from "../../../contexts/UserInfoContext";
import { Link } from "react-router-dom";
 
interface Props{
    lstCategories: Category[]
}
const HeaderComponent = memo(function HeaderComponent(prop: Props){
    const logo = getLogo();
    const lstCategories = prop.lstCategories;
    const { numberOfCartProducts } = useContext(numberOfCartProductsContext);
    const [isNavExpanded, setIsNavExpanded] = useState(false); 
    const userInfo = useContext<UserInfo>(userInfoContext)
    const navbarDropdownRef = useRef<HTMLDivElement>(null)

    useEffect(()=>{
        document.addEventListener('click', onClickOutSideNavBar)
    }, [numberOfCartProducts, userInfo])


    // expanded click
    // expand navbar
    const setNavExpandedStatus = (status:boolean)=>{
        setIsNavExpanded(status)
    }
    // click expand nav bar
    const onClickExpandNavBar = ()=>{
        setNavExpandedStatus(!isNavExpanded)
    }
    // click to close nav bar
    const onClickCloseExpandNavBar = ()=>{
        setNavExpandedStatus(false)
    }    
    // default click event when user click outside of nav
    const onClickOutSideNavBar = (event: any)=>{
        if(navbarDropdownRef.current && !navbarDropdownRef.current.contains(event.target as Node)){
            onClickCloseExpandNavBar();
        }
    }
    

    return(
        <header className="header-section bg-white">
            <div className="topbar">
				<div className="content-topbar container h-100">
					<div className="left-topbar">
						
					</div>

					<div className="right-topbar">
                        {userInfo.user.username ?
                            <UserInfoNav userInfo={userInfo}/>:
                            <ul className="nav-fill nav">
                                <li className="nav-item">
                                    <a href={LoginUrl} className="text-light nav-link">Log in</a>
                                </li>
                            </ul>
                        }
					</div>
				</div>
			</div>
            <nav className="navbar navbar-expand-lg navbar-light bg-light pt-3">
                <div className="container-fluid bg-light" ref={navbarDropdownRef}>
                    <Link to={"/"}>
                        <div className="navbar-brand">
                            <img src={logo} alt="" /><span>
                                Phucx
                            </span>
                        </div>
                    </Link>

                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" 
                        data-bs-target="#navbarToggleHeader" aria-controls="navbarToggleHeader" 
                        aria-expanded={isNavExpanded} aria-label="Toggle navigation"
                        onClick={onClickExpandNavBar}>
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className={`bg-light collapse navbar-collapse ${isNavExpanded?'show': ''}`} id="navbarToggleHeader">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item px-2">
                                <Link to={"/"}>
                                    <div className="nav-link active">
                                        Home
                                    </div>
                                </Link>
                            </li>
                            <li className="nav-item px-2">
                                <Link to={FOODS_PATH}>
                                    <div className="nav-link">
                                        Foods
                                    </div>
                                </Link>
                            </li>
                            <li className="nav-item px-2">
                                <div className="dropdown category-dropdown-container">
                                    <Link to={CATEGORIES_PATH}>
                                        <div className="nav-link dropdown-toggle" id="category-dropdown-menu-link">
                                            Categories
                                        </div>
                                    </Link>
                                    <div className="dropdown-menu p-0 position-absolute">
                                        <div className="category-dropdown" aria-labelledby="category-dropdown-menu-link">
                                            {lstCategories.map((category, index)=>(
                                                <Link key={index} to={`${CATEGORIES_PATH}/${convertNameForUrl(category.categoryName)}`}>
                                                    <div className="dropdown-item category-dropdown-item">
                                                        {nonBreakingSpace(category.categoryName)}
                                                    </div>
                                                </Link>
                                            ))}
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </ul>
                        <div className="row">
                            <div className="col-10">
                                <form className="form-inline my-2 my-lg-0 ml-0 ml-lg-4 mb-3 mb-lg-0 search-form">
                                    <Search/>
                                </form>
                            </div>
                            <div className="col-2">
                                <div className="my-2 my-lg-0 ml-0 ml-lg-4 mb-3 mb-lg-0 d-flex justify-content-center position-relative d-flex align-items-center">
                                    <Link to={CART_PATH}>
                                        <div className="btn btn-light ms-3 cart-icon cart-link">
                                            <FontAwesomeIcon icon={faCartShopping}/>
                                            {numberOfCartProducts>0 &&
                                                <span className="cart-badge badge rounded-pill badge-notification bg-danger">
                                                    {numberOfCartProducts}
                                                </span>
                                            }
                                        </div>
                                    </Link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </nav>
        </header>
    )
});
export default HeaderComponent;
