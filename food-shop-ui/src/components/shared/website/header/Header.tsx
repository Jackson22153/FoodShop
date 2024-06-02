import { getLogo } from "../../../../service/image";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { cartPath, categoriesPath, foodsPath } from "../../../../constant/FoodShoppingURL";
import { Category } from "../../../../model/Type";
import { convertNameForUrl, nonBreakingSpace } from "../../../../service/convert";
import Search from "../../functions/search/Search";
import { faCartShopping } from "@fortawesome/free-solid-svg-icons";
import { memo, useContext, useEffect, useRef, useState } from "react";
import { LoginUrl } from "../../../../constant/FoodShoppingApiURL";
import { logout } from "../../../../api/AuthorizationApi";
import numberOfCartProductsContext from "../../../contexts/NumberOfCartProductsContext";
import userInfoContext from "../../../contexts/UserInfoContext";
import ModalComponent from "../../functions/modal/Modal";
import { Modal } from "../../../../model/WebType";
import UserInfoNav from "../../functions/userinfo-nav/UserInfoNav";
 
interface Props{
    lstCategories: Category[]
}
const HeaderComponent = memo(function HeaderComponent(prop: Props){
    const logo = getLogo();
    const lstCategories = prop.lstCategories;
    const  userInfo  = useContext(userInfoContext);
    const { numberOfCartProducts } = useContext(numberOfCartProductsContext);
    const [isNavExpanded, setIsNavExpanded] = useState(false); 

    const navbarDropdownRef = useRef<HTMLDivElement>(null)
    const [logoutModal, setLogoutModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })

    useEffect(()=>{
        document.addEventListener('click', onClickOutSideNavBar)
    }, [])

    // logout
    const toggleModal = ()=>{
        setLogoutModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }
    const onClickToggleModal = async ()=>{
        toggleModal();
    }

    const onClickConfirmModal = async ()=>{
        try {
            const res = await logout();
            if(res.status){
                window.location.href="/";
            }
        } catch (error) {

        }
    }
    // expanded click
    // expand navbar
    const setNavExpandedStatus = (status:boolean)=>{
        setIsNavExpanded(status)
    }
    const onClickExpandNavBar = ()=>{
        setNavExpandedStatus(!isNavExpanded)
    }
    const onClickCloseExpandNavBar = ()=>{
        setNavExpandedStatus(false)
    }
    
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
                            <UserInfoNav 
                                onClickLogout={onClickToggleModal} 
                                userInfo={userInfo} />:
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
                    <a className="navbar-brand" href="/">
                        <img src={logo} alt="" /><span>
                            Phucx
                        </span>
                    </a>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" 
                        data-bs-target="#navbarToggleHeader" aria-controls="navbarToggleHeader" 
                        aria-expanded={isNavExpanded} aria-label="Toggle navigation"
                        onClick={onClickExpandNavBar}>
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className={`bg-light collapse navbar-collapse ${isNavExpanded?'show': ''}`} id="navbarToggleHeader">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item px-2">
                                <a className="nav-link active" aria-current="page" href="/">Home</a>
                            </li>
                            <li className="nav-item px-2">
                                <a className="nav-link" href={foodsPath}>Foods</a>
                            </li>
                            <li className="nav-item px-2">
                                <div className="dropdown category-dropdown-container">
                                    <a className="nav-link dropdown-toggle" href={categoriesPath} role="button" id="category-dropdown-menu-link" 
                                        data-toggle="dropdown" aria-haspopup="true">
                                        Categories
                                    </a>

                                    <div className="dropdown-menu p-0 position-absolute">
                                        <div className="category-dropdown" aria-labelledby="category-dropdown-menu-link">
                                            {lstCategories.map((category, index)=>(
                                                <a key={index} className="dropdown-item category-dropdown-item" 
                                                    href={categoriesPath+"/"+ convertNameForUrl(category.categoryName)}>
                                                    {nonBreakingSpace(category.categoryName)}
                                                </a>
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
                                    <a className="btn btn-light ms-3 cart-icon cart-link" href={cartPath}>
                                        <FontAwesomeIcon icon={faCartShopping}/>
                                        {numberOfCartProducts>0 &&
                                            <span className="cart-badge badge rounded-pill badge-notification bg-danger">
                                                {numberOfCartProducts}
                                            </span>
                                        }
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </nav>
            <ModalComponent modal={logoutModal} handleCloseButton={onClickToggleModal} 
                handleConfirmButton={onClickConfirmModal}/>
        </header>
    )
});
export default HeaderComponent;
