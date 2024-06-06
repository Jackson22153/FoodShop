import { useEffect, useRef, useState } from 'react';
import './Customer.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { Routes, Route, useLocation } from 'react-router-dom';
import UserInfomationComponent from './infomation/UserInfomation';
import { customerInfo, customerNotification, customerOrder } from '../../../../constant/FoodShoppingURL';
import UserOrdersComponent from './order/UserOrders';
import UserOrderComponent from './order/UserOrder';
import UserNotificationComponent from './notification/UserNotification';
import { logout } from '../../../../api/AuthorizationApi';
import { Modal } from '../../../../model/WebType';
import ModalComponent from '../../../shared/functions/modal/Modal';
import { isCustomer } from '../../../../api/UserApi';

export default function CustomerComponent(){
    // const [customerInfo, setCustomerInfo] = useState<Customer>();
    const sidebarRef = useRef(null)
    const location = useLocation()
    const [selectedPath, setSelectedPath] = useState(0);
    const [modal, setModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })

    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        checkAuthenticationCustomer();
        const path = location.pathname;
        if(path==customerInfo){
            setSelectedPath(0);
        }else if(path===customerOrder){
            setSelectedPath(1);
        }
        // console.log(subPath)
    }

    async function checkAuthenticationCustomer(){
        try {
            const res = await isCustomer();
            if(res.status){
                const data = res.data;
                const status = data.status;
                if(!status) window.location.href="/"
            }
        } catch (error) {
            window.location.href="/"
        }
    }

    async function onClickLogoutButton(){
        toggleModal()
    }

    function onClickShowSideBar(){
        if(sidebarRef.current){
            const sidebarEle = sidebarRef.current as HTMLDivElement;
            sidebarEle.classList.toggle('show-side-bar')
        }
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
        <div className='my-4 customer-page p-4 position-relative'>
            <div className="container my-5">
                <nav className='z-3'>
                    <div className="logo cursor-pointer" onClick={onClickShowSideBar}>
                        {/* <i className="bx bx-menu menu-icon"></i> */}
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
                                    <a href={customerInfo} className={`nav-link ${selectedPath===0?'active': ''}`}>
                                        <i className="bx bx-home-alt icon"></i>
                                        <span className="link">User{`\u00A0`}Infomation</span>
                                    </a>
                                </li>
                                <li className="list nav-item">
                                    <a href={customerOrder} className={`nav-link ${selectedPath===1?'active': ''}`}>
                                        <i className="bx bx-bar-chart-alt-2 icon"></i>
                                        <span className="link">Orders</span>
                                    </a>
                                </li>
                                <li className="list nav-item">
                                    <a href={customerNotification} className={`nav-link ${selectedPath===2?'active': ''}`}>
                                        <i className="bx bx-bar-chart-alt-2 icon"></i>
                                        <span className="link">Notifications</span>
                                    </a>
                                </li>
                                <li className="list nav-item">
                                    <a href='/' className={`nav-link ${selectedPath===3?'active': ''}`}>
                                        <i className="bx bx-bell icon"></i>
                                        <span className="link">Home</span>
                                    </a>
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
                        <Route path='*' element={<UserInfomationComponent/>}></Route>
                        <Route path='info' element={<UserInfomationComponent/>}></Route>
                        <Route path='order' element={<UserOrdersComponent/>}></Route>
                        <Route path='order/:orderId' element={<UserOrderComponent/>}></Route>
                        <Route path='notification' element={<UserNotificationComponent/>}></Route>
                    </Routes>
                </div>
            </div>
        </div>
    );
}