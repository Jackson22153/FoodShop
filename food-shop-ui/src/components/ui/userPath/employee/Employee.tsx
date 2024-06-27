import { useEffect, useRef, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { Routes, Route, useLocation, Link } from 'react-router-dom';
import { EMPLOYEE_INFO, EMPLOYEE_NOTIFICATION, EMPLOYEE_ORDER, FORBIDDEN_ERROR_PAGE 
} from '../../../../constant/FoodShoppingURL';
import { logout } from '../../../../api/AuthorizationApi';
import EmployeeInformationComponent from './information/EmployeeInformation';
import EmployeeOrdersComponent from './order/EmployeeOrders';
import EmployeeOrderComponent from './order/EmployeeOrder';
import EmployeeNotificationComponent from './notification/EmployeeNotification';
import EmployeePendingOrderComponent from './order/EmployeePendingOrder';
import { Modal } from '../../../../model/WebType';
import ModalComponent from '../../../shared/functions/modal/Modal';
import EmployeeConfirmedOrderComponent from './order/EmployeeConfirmOrder';
import { isEmployee } from '../../../../api/EmployeeApi';

export default function EmployeeComponent(){
    const [isShowedSideBar, setIsShowedSideBar] = useState(false)
    const location = useLocation()
    const [selectedPath, setSelectedPath] = useState(0);
    const [modal, setModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })
    const logoRef = useRef(null)
    const sidebarRef = useRef(null)

    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        checkAuthenticationEmployee();
        const path = location.pathname;
        if(path==EMPLOYEE_INFO){
            setSelectedPath(0);
        }else if(path===EMPLOYEE_ORDER){
            setSelectedPath(1);
        }else if(path === EMPLOYEE_NOTIFICATION){
            setSelectedPath(2);
        }

        document.addEventListener('click', onClickOutSideSideBar)
    }


    async function checkAuthenticationEmployee(){
        try {
            const res = await isEmployee();
            if(200<=res.status&&res.status<300){
                const data = res.data;
                const status = data.status;
                if(!status) window.location.href="/"
            }
        } catch (error) {
            if(error.response){
                const errorResponse = error.response;
                const status = errorResponse.status;
                if(status===403){
                    window.location.href=FORBIDDEN_ERROR_PAGE
                }
            }
        }
    }

    
    // click side bar
    function onClickShowSideBar(){
        toggleIsShowedSideBar();
    }
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
                console.log('outside')
                closeShowedSidebar();
            }
        }
    }    
    
    // logout modal
    async function onClickLogoutButton(){
        toggleModal();
    }
    // TOGGLE MODAL
    const toggleModal = ()=>{
        setModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }
    // close modal
    const onClickCloseModal = ()=>{
        toggleModal()
    }
    // confirm modal
    const onClickConfirmModal = async ()=>{
        try {
            const res = await logout();
            if(res.status){
                window.location.href="/";
            }
        } catch (error) {
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
                                    <Link to={EMPLOYEE_INFO}>
                                        <div className={`nav-link ${selectedPath===0?'active': ''}`}>
                                            <i className="bx bx-home-alt icon"></i>
                                            <span className="link">User{`\u00A0`}Information</span>
                                        </div>
                                    </Link>
                                </li>
                                <li className="list nav-item">
                                    <Link to={EMPLOYEE_ORDER}>
                                        <div className={`nav-link ${selectedPath===1?'active': ''}`}>
                                            <i className="bx bx-bar-chart-alt-2 icon"></i>
                                            <span className="link">Orders</span>
                                        </div>
                                    </Link>
                                </li>
                                <li className="list nav-item">
                                    <Link to={EMPLOYEE_NOTIFICATION}>
                                        <div className={`nav-link ${selectedPath===2?'active': ''}`}>
                                            <i className="bx bx-bar-chart-alt-2 icon"></i>
                                            <span className="link">Notifications</span>
                                        </div>
                                    </Link>
                                </li>
                                <li className="list nav-item">
                                    <Link to={"/"}>
                                        <div className={`nav-link ${selectedPath===3?'active': ''}`}>
                                            <i className="bx bx-bell icon"></i>
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
                                    <ModalComponent modal={modal} handleCloseButton={onClickCloseModal}
                                        handleConfirmButton={onClickConfirmModal}/>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
                <div >
                    <Routes>
                        <Route path='*' element={<EmployeeInformationComponent/>}></Route>
                        <Route path='info' element={<EmployeeInformationComponent/>}></Route>
                        <Route path='order' element={<EmployeeOrdersComponent/>}></Route>
                        <Route path='order/:orderId' element={<EmployeeOrderComponent/>}></Route>
                        <Route path='order/pending/:orderId' element={<EmployeePendingOrderComponent/>} ></Route>
                        <Route path='order/confirmed/:orderId' element={<EmployeeConfirmedOrderComponent/>} ></Route>
                        <Route path='notification' element={<EmployeeNotificationComponent/>}></Route>
                    </Routes>
                </div>  
            </div>
        </div>
    );
}