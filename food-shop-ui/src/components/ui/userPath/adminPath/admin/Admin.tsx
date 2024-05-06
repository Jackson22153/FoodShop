import { Route, Routes, useLocation } from 'react-router-dom';
import TopBarComponent from '../../../../shared/functions/admin/topbar/TopBar';
import FooterComponent from '../../../../shared/website/footer/Footer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { useEffect, useRef, useState } from 'react';
import { logout } from '../../../../../api/AuthorizationApi';
import { adminCategories, adminUsers, adminProducts } from '../../../../../constant/FoodShoppingURL';
import AdminCategoriesComponent from '../category/Categories';
import AdminCategoryComponent from '../category/Category';
import AdminFoodsComponent from '../food/Foods';
import AdminFoodComponent from '../food/Food';
import AdminUsersComponent from '../user/Users';
import AdminEmployeeComponent from '../user/Employee';
import AdminCustomerComponent from '../user/Customer';
import { Modal } from '../../../../../model/WebType';
import ModalComponent from '../../../../shared/functions/modal/Modal';

export default function AdminComponent(){
    const sidebarRef = useRef(null)
    const [selectedPath, setSelectedPath] = useState(0);
    const location = useLocation()
    const [modal, setModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })


    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        const path = location.pathname;
        if(path==adminCategories){
            setSelectedPath(0);
        }else if(path===adminProducts){
            setSelectedPath(1);
        }else if(path===adminUsers){
            setSelectedPath(2);
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
                window.location.href="/";
            }
        } catch (error) {

        }
    }

    return(
        <>
            <div id="wrapper" className='d-flex'>
                {/* <!-- Sidebar --> */}
                <div className="sidebar col-md-2 px-4">
                    {/* <SidebarComponent/> */}
                    <nav className='z-3'>
                        <div className="logo cursor-pointer" onClick={onClickShowSideBar}>
                            <span className='mx-3'><i><FontAwesomeIcon icon={faBars}/></i></span>
                            <span className="logo-name">Phucx</span>
                        </div>
                        <div className="sidebar"  ref={sidebarRef}>
                            <div className="sidebar-content py-0">
                                <div className="logo cursor-pointer mx-0" onClick={onClickShowSideBar}>
                                    <span className='mx-3'><i><FontAwesomeIcon icon={faBars}/></i></span>
                                    <span className="logo-name">Phucx</span>
                                </div>
                                <ul className="flex-column lists nav nav-pills mb-auto">
                                    <li className="list nav-item">
                                        <a href={adminCategories} className={`nav-link ${selectedPath===0?'active': ''}`}>
                                            <i className="bx bx-home-alt icon"></i>
                                            <span className="link">Categories</span>
                                        </a>
                                    </li>
                                    <li className="list nav-item">
                                        <a href={adminProducts} className={`nav-link ${selectedPath===1?'active': ''}`}>
                                            <i className="bx bx-bar-chart-alt-2 icon"></i>
                                            <span className="link">Products</span>
                                        </a>
                                    </li>
                                    <li className="list nav-item">
                                        <a href={adminUsers} className={`nav-link ${selectedPath===2?'active': ''}`}>
                                            <i className="bx bx-bar-chart-alt-2 icon"></i>
                                            <span className="link">Users</span>
                                        </a>
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
                </div>
                {/* <!-- End of Sidebar --> */}

                {/* <!-- Content Wrapper --> */}
                <div id="content-wrapper" className="d-flex flex-column col-md-10 bg-light">

                    {/* <!-- Main Content --> */}
                    <div id="content">
                        {/* <!-- Topbar --> */}
                        <TopBarComponent/>
                        {/* <!-- End of Topbar --> */}

                        {/* <!-- Begin Page Content --> */}
                        <Routes>
                            <Route path='/products' element={<AdminFoodsComponent/>}/> 
                            <Route path='/products/:productName' element={<AdminFoodComponent/>}/> 
                            {/* <Route path='*' element={<AdminDashBoardComponent/>}/>  */}
                            <Route path='/categories' element={<AdminCategoriesComponent/>}/>
                            <Route path='*' element={<AdminCategoriesComponent/>}/>
                            <Route path='/categories/:categoryID' element={<AdminCategoryComponent/>}/>
                            <Route path='/users' element={<AdminUsersComponent/>}/> 
                            <Route path='/employee/:employeeID' element={<AdminEmployeeComponent/>}/> 
                            <Route path='/customer/:customerID' element={<AdminCustomerComponent/>}/> 
                        </Routes>
                        
                        {/* <!-- /.container-fluid --> */}
                    </div>
                    {/* <!-- End of Main Content --> */}

                </div>
                {/* <!-- End of Content Wrapper --> */}
            </div>
            {/* <!-- Footer --> */}
            <FooterComponent/>
            {/* <!-- End of Footer --> */}
                {/* <!-- Scroll to Top Button--> */}
            <a className="scroll-to-top rounded" href="#page-top">
                <i className="fas fa-angle-up"></i>
            </a>
        </>
    );
}