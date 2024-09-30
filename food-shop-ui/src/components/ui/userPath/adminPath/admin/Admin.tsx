import { Link, Route, Routes, useLocation, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { useEffect, useRef, useState } from 'react';
import { logout } from '../../../../../api/AuthorizationApi';
import { ADMIN_CATEGORIES, ADMIN_USERS, ADMIN_PRODUCTS, 
    ADMIN_ADD_CATEGORY, ADMIN_ADD_PRODUCT,
    FORBIDDEN_ERROR_PAGE
 } from '../../../../../constant/FoodShoppingURL';
import AdminCategoriesComponent from '../category/Categories';
import AdminCategoryComponent from '../category/Category';
import AdminFoodsComponent from '../food/Foods';
import AdminFoodComponent from '../food/Food';
import AdminUsersComponent from '../user/Users';
import AdminEmployeeComponent from '../user/Employee';
import AdminCustomerComponent from '../user/Customer';
import { Modal } from '../../../../../model/WebType';
import ModalComponent from '../../../../shared/functions/modal/Modal';
import { isAdmin } from '../../../../../api/AdminApi';
import AdminAddCategoryComponent from '../category/AddNewCategory';
import AdminAddFoodComponent from '../food/AddFood';
import { AuthorizationAdminUrl } from '../../../../../constant/FoodShoppingApiURL';
import WebConfigComponent from '../website/WebConfig';

export default function AdminComponent(){
    const sidebarRef = useRef(null)
    const [selectedPath, setSelectedPath] = useState(0);
    const location = useLocation()
    const navigate = useNavigate()
    const path = location.pathname.toLowerCase();

    const [categoryDropdown, setCategoryDropdown] = useState(false)
    const [productDropdown, setProductDropdown] = useState(false)
    const [isShowedSideBar, setIsShowedSideBar] = useState(false)
    const logoRef = useRef(null)
    const [modal, setModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })


    useEffect(()=>{
        initial();
    }, [path])

    const initial = ()=>{
        setDefaultDropdown();
        checkAuthenticationAdmin();

        if(path==ADMIN_CATEGORIES){
            setSelectedPath(0.1);
        }else if(path === ADMIN_ADD_CATEGORY){
            setSelectedPath(0.2);
        }else if(path===ADMIN_PRODUCTS){
            setSelectedPath(1.1);
        }else if(path===ADMIN_ADD_PRODUCT){
            setSelectedPath(1.2);
        }else if(path===ADMIN_USERS){
            setSelectedPath(2);
        }

        // add event to close sidebar when clicking on outside
        document.addEventListener("click", onClickOutSideSideBar)
    }

    const setDefaultDropdown = ()=>{
        setCategoryDropdown(false);
        setProductDropdown(false);
    }

    // on click outside sidebar
    const onClickOutSideSideBar = (event: any)=>{
        if(logoRef!=null && sidebarRef!=null){
            const logo = logoRef.current as HTMLElement;
            const sidebar = sidebarRef.current as HTMLElement;
            if(!sidebar.contains(event.target as Node) && !logo.contains(event.target as Node)){
                closeShowedSidebar();
            }
        }
    }
    // close sidebar
    const closeShowedSidebar = ()=>{
        setIsShowedSideBar(false)
    }

    async function checkAuthenticationAdmin(){
        try {
            const res = await isAdmin();
            if(res.status){
                const data = res.data;
                const status = data.status;
                if(!status) navigate("/")
            }
        } catch (error) {
            if(error.response){
                const errorResponse = error.response;
                const status = errorResponse.status;
                if(status===403){
                    navigate(FORBIDDEN_ERROR_PAGE)
                }else if(status===401){
                    navigate("/");
                }
            }
        }
    }

    
    const onClickShowSideBar = ()=>{
        setIsShowedSideBar(status => !status)
    }
    // logout modal
    async function onClickLogoutButton(){
        toggleModal();
    }

    const toggleModal = ()=>{
        setModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }

    const onClickCloseModal = ()=>{
        toggleModal()
    }
    const onClickConfirmModal = async ()=>{
        try {
            const res = await logout();
            if(res.status){
            }
        } catch (error) {
            
        } finally{    
            window.location.href="/";
        }
    }

    // category
    const onClickCategoryToggle = ()=>{
        setCategoryDropdown(dropdown => !dropdown)
    }
    // product
    const onClickProductToggle = ()=>{
        setProductDropdown(dropdown => !dropdown)
    }

    return(
        <div className='my-4 customer-page p-4 position-relative'>
            <div className='container my-5'>
                <nav className='z-3'>
                    <div className="logo cursor-pointer" onClick={onClickShowSideBar} ref={logoRef}>
                        <span className='mx-3'><i><FontAwesomeIcon icon={faBars}/></i></span>
                        <span className="logo-name">Phucx</span>
                    </div>
                    <div className={`sidebar ${isShowedSideBar?'show-side-bar':''}`} ref={sidebarRef}>
                        <div className="sidebar-content py-0">
                            <div className="logo cursor-pointer mx-0" onClick={onClickShowSideBar}>
                                <span className='mx-3'><i><FontAwesomeIcon icon={faBars}/></i></span>
                                <span className="logo-name">Phucx</span>
                            </div>
                            <ul className="flex-column lists nav nav-pills mb-auto">
                                <li className="list nav-item">
                                    <span className={`${categoryDropdown?'rounded-bottom-0':''} m-0 dropdown-toggle nav-link ${(selectedPath===0.1)||(selectedPath===0.2)?'active': ''}`}
                                        onClick={onClickCategoryToggle}>
                                        <span className="link cursor-pointer">Categories</span>
                                    </span>
                                    <ul className={`rounded-top-0 p-0 dropdown-menu ${categoryDropdown?'show': ''} position-relative btn-toggle-nav list-unstyled fw-normal small`}>
                                        <li className={`p-0 dropdown-item ${selectedPath===0.1?'bg-body-secondary':''}`}>
                                            <Link to={ADMIN_CATEGORIES}>
                                                <div className={`text-black-50 w-100 d-block px-3 py-2`}>Category</div>
                                            </Link>
                                        </li>
                                        <li className={`p-0 dropdown-item ${selectedPath===0.2?'bg-body-secondary':''}`}>
                                            <Link to={ADMIN_ADD_CATEGORY}>
                                                <div className={`text-black-50 w-100 d-block px-3 py-2`}>Add New Category</div>
                                            </Link>
                                        </li>
                                    </ul>
                                </li>

                                <li className="list nav-item">
                                    <span className={`${productDropdown?'rounded-bottom-0':''} m-0 dropdown-toggle nav-link ${(selectedPath===1.1)||(selectedPath===1.2)?'active': ''}`}
                                        onClick={onClickProductToggle}>
                                        <span className="link cursor-pointer">Products</span>
                                    </span>
                                    <ul className={`rounded-top-0 p-0 dropdown-menu ${productDropdown?'show': ''} position-relative btn-toggle-nav list-unstyled fw-normal small`}>
                                        <li className={`p-0 dropdown-item ${selectedPath===1.1?'bg-body-secondary':''}`}>
                                            <Link to={ADMIN_PRODUCTS}>
                                                <div className={`text-black-50 w-100 d-block px-3 py-2`}>Product</div>
                                            </Link>
                                        </li>
                                        <li className={`p-0 dropdown-item ${selectedPath===1.2?'bg-body-secondary':''}`}>
                                            <Link to={ADMIN_ADD_PRODUCT}>
                                                <div className={`text-black-50 w-100 d-block px-3 py-2`}>Add New Product</div>
                                            </Link>
                                        </li>
                                    </ul>
                                </li>

                                <li className="list nav-item">
                                    <Link to={ADMIN_USERS}>
                                        <div className={`nav-link ${selectedPath===2?'active': ''}`}>
                                            <span className="link">Users</span>
                                        </div>
                                    </Link>
                                </li>
                            </ul>

                            <ul className='flex-column lists nav nav-pills'>
                                <li className="list nav-item">
                                    <a href={AuthorizationAdminUrl} className={`nav-link`}>
                                        <span className="link">Setting</span>
                                    </a>
                                </li>
                                <li className="list nav-item">
                                    <Link to={"/"}>
                                        <div className={`nav-link`}>
                                            <span className="link">Home</span>
                                        </div>
                                    </Link>
                                </li>
                            </ul>

                            <hr />
                            <ul className="bottom-cotent flex-column lists nav nav-pills">
                                <li className="list nav-item">
                                    <div className="nav-link" onClick={onClickLogoutButton}>
                                        <span className="link">Logout</span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
                <div>
                    <Routes>
                        <Route path='config/*' element={<WebConfigComponent/>}/>
                        <Route path='/products' element={<AdminFoodsComponent/>}/> 
                        <Route path='/products/addProduct' element={<AdminAddFoodComponent/>}/> 
                        <Route path='/products/:productName' element={<AdminFoodComponent/>}/> 
                        <Route path='/categories' element={<AdminCategoriesComponent/>}/>
                        <Route path='/categories/addCategory' element={<AdminAddCategoryComponent/>}/>
                        <Route path='*' element={<AdminCategoriesComponent/>}/>
                        <Route path='/categories/:categoryID' element={<AdminCategoryComponent/>}/>
                        <Route path='/users' element={<AdminUsersComponent/>}/> 
                        <Route path='/employee/:employeeID' element={<AdminEmployeeComponent/>}/> 
                        <Route path='/customer/:customerID' element={<AdminCustomerComponent/>}/> 
                    </Routes>
                </div>
            </div>
            <ModalComponent modal={modal} handleCloseButton={onClickCloseModal}
                handleConfirmButton={onClickConfirmModal}/>
        </div>
    );
}