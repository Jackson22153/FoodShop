import { Route, Routes, useLocation } from 'react-router-dom';
import TopBarComponent from '../../../../shared/functions/admin/topbar/TopBar';
import FooterComponent from '../../../../shared/website/footer/Footer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { useEffect, useRef, useState } from 'react';
import { logout } from '../../../../../api/AuthorizationApi';
import { adminCategories, adminUsers, adminProducts, 
    adminAddUser, adminAddCategory, adminAddProduct
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
import AdminAddUserComponent from '../user/AddUser';
import { isAdmin } from '../../../../../api/AdminApi';
import AdminAddCategoryComponent from '../category/AddNewCategory';
import AdminAddFoodComponent from '../food/AddFood';

export default function AdminComponent(){
    const sidebarRef = useRef(null)
    const [selectedPath, setSelectedPath] = useState(0);
    const location = useLocation()
    const [userDropdown, setUserDropdown] = useState(false)
    const [categoryDropdown, setCategoryDropdown] = useState(false)
    const [productDropdown, setProductDropdown] = useState(false)
    const [modal, setModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })


    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        checkAuthenticationAdmin();
        const path = location.pathname;
        if(path==adminCategories){
            setSelectedPath(0.1);
        }else if(path === adminAddCategory){
            setSelectedPath(0.2);
        }else if(path===adminProducts){
            setSelectedPath(1.1);
        }else if(path===adminAddProduct){
            setSelectedPath(1.2);
        }else if(path===adminUsers){
            setSelectedPath(2.1);
        }else if(path===adminAddUser){
            setSelectedPath(2.2)
        }
    }

    async function checkAuthenticationAdmin(){
        try {
            const res = await isAdmin();
            if(res.status){
                const data = res.data;
                const status = data.status;
                if(!status) window.location.href="/"
            }
        } catch (error) {
            window.location.href="/"
        }
    }

    
    const onClickShowSideBar = ()=>{
        if(sidebarRef.current){
            const sidebarEle = sidebarRef.current as HTMLDivElement;
            sidebarEle.classList.toggle('show-side-bar')
        }
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

    // user
    const onClickUserToggle = ()=>{
        setUserDropdown(dropdown => !dropdown)
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
                    <div className="logo cursor-pointer" onClick={onClickShowSideBar}>
                        <span className='mx-3'><i><FontAwesomeIcon icon={faBars}/></i></span>
                        <span className="logo-name">Phucx</span>
                    </div>
                    <div className="sidebar" ref={sidebarRef}>
                        <div className="sidebar-content py-0">
                            <div className="logo cursor-pointer mx-0" onClick={onClickShowSideBar}>
                                <span className='mx-3'><i><FontAwesomeIcon icon={faBars}/></i></span>
                                <span className="logo-name">Phucx</span>
                            </div>
                            <ul className="flex-column lists nav nav-pills mb-auto">
                                <li className="list nav-item">
                                    <span className={`${categoryDropdown?'rounded-bottom-0':''} m-0 dropdown-toggle nav-link ${(selectedPath===0.1)||(selectedPath===0.2)?'active': ''}`}
                                        onClick={onClickCategoryToggle}>
                                        <i className="bx bx-bar-chart-alt-2 icon"></i>
                                        <span className="link cursor-pointer">Categories</span>
                                    </span>
                                    <ul className={`rounded-top-0 p-0 dropdown-menu ${categoryDropdown?'show': ''} position-relative btn-toggle-nav list-unstyled fw-normal small`}>
                                        <li className={`p-0 dropdown-item ${selectedPath===0.1?'bg-body-secondary':''}`}>
                                            <a className={`text-black-50 w-100 d-block px-3 py-2`} href={adminCategories}>Category</a>
                                        </li>
                                        <li className={`p-0 dropdown-item ${selectedPath===0.2?'bg-body-secondary':''}`}>
                                            <a className='text-black-50 w-100 d-block px-3 py-2' href={adminAddCategory}>Add New Category</a>
                                        </li>
                                    </ul>
                                </li>

                                <li className="list nav-item">
                                    <span className={`${productDropdown?'rounded-bottom-0':''} m-0 dropdown-toggle nav-link ${(selectedPath===1.1)||(selectedPath===1.2)?'active': ''}`}
                                        onClick={onClickProductToggle}>
                                        <i className="bx bx-bar-chart-alt-2 icon"></i>
                                        <span className="link cursor-pointer">Products</span>
                                    </span>
                                    <ul className={`rounded-top-0 p-0 dropdown-menu ${productDropdown?'show': ''} position-relative btn-toggle-nav list-unstyled fw-normal small`}>
                                        <li className={`p-0 dropdown-item ${selectedPath===1.1?'bg-body-secondary':''}`}>
                                            <a className={`text-black-50 w-100 d-block px-3 py-2`} href={adminProducts}>Product</a>
                                        </li>
                                        <li className={`p-0 dropdown-item ${selectedPath===1.2?'bg-body-secondary':''}`}>
                                            <a className='text-black-50 w-100 d-block px-3 py-2' href={adminAddProduct}>Add New Product</a>
                                        </li>
                                    </ul>
                                </li>
                                
                                <li className="list nav-item">
                                    <span className={`${userDropdown?'rounded-bottom-0':''} m-0 dropdown-toggle nav-link ${(selectedPath===2.1)||(selectedPath===2.2)?'active': ''}`}
                                        onClick={onClickUserToggle}>
                                        <i className="bx bx-bar-chart-alt-2 icon"></i>
                                        <span className="link cursor-pointer">Users</span>
                                    </span>
                                    <ul className={`rounded-top-0 p-0 dropdown-menu ${userDropdown?'show': ''} position-relative btn-toggle-nav list-unstyled fw-normal small`}>
                                        <li className={`p-0 dropdown-item ${selectedPath===2.1?'bg-body-secondary':''}`}>
                                            <a className={`text-black-50 w-100 d-block px-3 py-2`} href={adminUsers}>User</a>
                                        </li>
                                        <li className={`p-0 dropdown-item ${selectedPath===2.2?'bg-body-secondary':''}`}>
                                            <a className='text-black-50 w-100 d-block px-3 py-2' href={adminAddUser}>Add New User</a>
                                        </li>
                                    </ul>
                                </li>
                                <li className="list nav-item">
                                    <a href="/" className={`nav-link ${selectedPath===3?'active': ''}`}>
                                        <i className="bx bx-bell icon"></i>
                                        <span className="link">Home</span>
                                    </a>
                                </li>
                            </ul>
                            <hr />
                            <ul className="bottom-cotent flex-column lists nav nav-pills">
                                <li className="list nav-item">
                                    <div className="nav-link" onClick={onClickLogoutButton}>
                                        <span className="link">Logout</span>
                                    </div>
                                    <ModalComponent modal={modal} handleCloseButton={onClickCloseModal}
                                        handleConfirmButton={onClickConfirmModal}/>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
                <div>
                    <Routes>
                        <Route path='/products' element={<AdminFoodsComponent/>}/> 
                        <Route path='/products/addProduct' element={<AdminAddFoodComponent/>}/> 
                        <Route path='/products/:productName' element={<AdminFoodComponent/>}/> 
                        {/* <Route path='*' element={<AdminDashBoardComponent/>}/>  */}
                        <Route path='/categories' element={<AdminCategoriesComponent/>}/>
                        <Route path='/categories/addCategory' element={<AdminAddCategoryComponent/>}/>
                        <Route path='*' element={<AdminCategoriesComponent/>}/>
                        <Route path='/categories/:categoryID' element={<AdminCategoryComponent/>}/>
                        <Route path='/users' element={<AdminUsersComponent/>}/> 
                        <Route path='/users/adduser' element={<AdminAddUserComponent/>}/> 
                        <Route path='/employee/:employeeID' element={<AdminEmployeeComponent/>}/> 
                        <Route path='/customer/:customerID' element={<AdminCustomerComponent/>}/> 
                    </Routes>
                </div>
            </div>
        </div>
    );
}