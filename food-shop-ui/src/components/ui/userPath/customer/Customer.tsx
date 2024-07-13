import { useEffect, useRef, useState } from 'react';
import './Customer.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { Routes, Route, useLocation, Link, useNavigate } from 'react-router-dom';
import UserInformationComponent from './information/UserInformation';
import { CUSTOMER_INFO, CUSTOMER_NOTIFICATION, CUSTOMER_ORDER, FORBIDDEN_ERROR_PAGE 
} from '../../../../constant/FoodShoppingURL';
import UserOrdersComponent from './order/UserOrders';
import UserOrderComponent from './order/UserOrder';
import UserNotificationComponent from './notification/UserNotification';
import { logout } from '../../../../api/AuthorizationApi';
import { Modal } from '../../../../model/WebType';
import ModalComponent from '../../../shared/functions/modal/Modal';
import { isCustomer } from '../../../../api/UserApi';
import { AuthorizationUserUrl } from '../../../../constant/FoodShoppingApiURL';

export default function CustomerComponent(){
    const navigate = useNavigate()
    const location = useLocation()
    const path = location.pathname.toLowerCase();
    const [isShowedSideBar, setIsShowedSideBar] = useState(false)
    const [selectedPath, setSelectedPath] = useState(0);
    const sidebarRef = useRef(null)
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
        // check user 
        checkAuthenticationCustomer();
        // check selected path
        if(path==CUSTOMER_INFO){
            setSelectedPath(0);
        }else if(path===CUSTOMER_ORDER){
            setSelectedPath(1);
        }else if(path === CUSTOMER_NOTIFICATION){
            setSelectedPath(2);
        }
        // add event to close sidebar when clicking on outside
        document.addEventListener("click", onClickOutSideSideBar)
    }
    // check customer authentication
    async function checkAuthenticationCustomer(){
        try {
            const res = await isCustomer();
            if(200<=res.status&&res.status<300){
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
                }
            }
        }
    }

    // on click side bar
    function onClickShowSideBar(){
        toggleIsShowedSideBar();
    }
    // toggle sidebar
    const toggleIsShowedSideBar = ()=>{
        setIsShowedSideBar(status => !status);
    }
    // close sidebar
    const closeShowedSidebar = ()=>{
        setIsShowedSideBar(false)
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
    // logout
    function onClickLogoutButton(){
        toggleModal()
    }
    const toggleModal = ()=>{
        setModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }

    // confirm logout 
    const onClickConfirmModal = async ()=>{
        try {
            const res = await logout();
            if(200<=res.status&&res.status<300){
            }
        } catch (error) {

        }finally{
            window.location.href="/";
        }
    }

    return(
        <div className='my-4 customer-page p-4 position-relative'>
            <div className="container my-5">
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
                                    <Link to={CUSTOMER_INFO}>
                                        <div className={`nav-link ${selectedPath===0?'active': ''}`}>
                                            <span className="link">Informaiton</span>
                                        </div>
                                    </Link>
                                </li>
                                <li className="list nav-item">
                                    <Link to={CUSTOMER_ORDER}>
                                        <div className={`nav-link ${selectedPath===1?'active': ''}`}>
                                            <span className="link">Orders</span>
                                        </div>
                                    </Link>
                                </li>
                                <li className="list nav-item">
                                    <Link to={CUSTOMER_NOTIFICATION}>
                                        <div className={`nav-link ${selectedPath===2?'active': ''}`}>
                                            <span className="link">Notifications</span>
                                        </div>
                                    </Link>
                                </li>

                            </ul>

                            <ul className='flex-column lists nav nav-pills'>
                                <li className="list nav-item">
                                    <Link to={AuthorizationUserUrl}>
                                        <div className={`nav-link`}>
                                            <span className="link">Setting</span>
                                        </div>
                                    </Link>
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
                                    <span className="nav-link" onClick={onClickLogoutButton}>
                                        <span className="link">Logout</span>
                                    </span>
                                    <ModalComponent modal={modal} handleCloseButton={toggleModal}
                                        handleConfirmButton={onClickConfirmModal}/>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
                <div >
                    <Routes>
                        <Route path='*' element={<UserInformationComponent/>}></Route>
                        <Route path='info' element={<UserInformationComponent/>}></Route>
                        <Route path='order' element={<UserOrdersComponent/>}></Route>
                        <Route path='order/:orderId' element={<UserOrderComponent/>}></Route>
                        <Route path='notification' element={<UserNotificationComponent/>}></Route>
                    </Routes>
                </div>
            </div>
        </div>
    );
}