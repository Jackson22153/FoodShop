import { Route, Routes, useLocation } from 'react-router-dom';
import TopBarComponent from '../../../../shared/functions/admin/topbar/TopBar';
import FooterComponent from '../../../../shared/website/footer/Footer';
import AdminDashBoardComponent from '../adminDashBoard/AdminDashBoard';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { useEffect, useRef, useState } from 'react';
import { logout } from '../../../../../api/AuthorizationApi';
import { adminCategories, adminUsers, adminProducts, homePath } from '../../../../../constant/FoodShoppingURL';
import AdminCategoriesComponent from '../category/Categories';
import AdminCategoryComponent from '../category/Category';
import AdminFoodsComponent from '../food/Foods';
import AdminFoodComponent from '../food/Food';
import AdminUsersComponent from '../user/Users';

export default function AdminComponent(){
    const sidebarRef = useRef(null)
    const [selectedPath, setSelectedPath] = useState(0);
    const location = useLocation()


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
    const onClickLogoutButton = async ()=>{
        const res = await logout();
        if(res.status){
            window.location.href=homePath;
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
                            <span className="logo-name">CodingLab</span>
                        </div>
                        <div className="sidebar"  ref={sidebarRef}>
                            <div className="sidebar-content py-0">
                                <div className="logo cursor-pointer mx-0" onClick={onClickShowSideBar}>
                                    <span className='mx-3'><i><FontAwesomeIcon icon={faBars}/></i></span>
                                    <span className="logo-name">CodingLab</span>
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
                                        <a href="#" className={`nav-link ${selectedPath===3?'active': ''}`}>
                                            <i className="bx bx-bell icon"></i>
                                            <span className="link">Notifications</span>
                                        </a>
                                    </li>
                                </ul>
                                <hr />
                                <ul className="bottom-cotent flex-column lists nav nav-pills">
                                    <li className="list nav-item">
                                        <a href="#" className="nav-link">
                                            <i className="bx bx-log-out icon"></i>
                                            <span className="link" onClick={onClickLogoutButton}>Logout</span>
                                        </a>
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
                            <Route path='*' element={<AdminDashBoardComponent/>}/> 
                            <Route path='/categories' element={<AdminCategoriesComponent/>}/>
                            <Route path='/categories/:categoryID' element={<AdminCategoryComponent/>}/>
                            <Route path='/users' element={<AdminUsersComponent/>}/> 

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
            {/* <!-- Logout Modal--> */}
            <div className="modal fade" id="logoutModal" tabIndex={-1}  role="dialog" aria-labelledby="exampleModalLabel"
                aria-hidden="true">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
                            <button className="close" type="button" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                        </div>
                        <div className="modal-body">Select "Logout" below if you are ready to end your current session.</div>
                        <div className="modal-footer">
                            <button className="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                            <a className="btn btn-primary" href="login.html">Logout</a>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}