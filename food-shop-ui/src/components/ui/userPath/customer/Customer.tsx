import { useEffect, useState } from 'react';
import './Customer.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { Routes, Route, useLocation } from 'react-router-dom';
import UserInformationComponent from './infomation/UserInfomation';
import { customerInfo, customerNotification, customerOrder } from '../../../../constant/FoodShoppingURL';
import UserOrdersComponent from './order/UserOrders';
import UserOrderComponent from './order/UserOrder';
import UserNotificationComponent from './notification/UserNotification';
import { logout } from '../../../../api/AuthorizationApi';
import { Modal } from '../../../../model/WebType';
import ModalComponent from '../../../shared/functions/modal/Modal';
import { isCustomer } from '../../../../api/UserApi';

export default function CustomerComponent(){
    const location = useLocation()
    const [isShowedSideBar, setIsShowedSideBar] = useState(false)
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
        }else if(path === customerNotification){
            setSelectedPath(2);
        }
    }
    // check customer authentication
    async function checkAuthenticationCustomer(){
        try {
            const res = await isCustomer();
            if(200<=res.status&&res.status<300){
                const data = res.data;
                const status = data.status;
                if(!status) window.location.href="/"
            }
        } catch (error) {
            window.location.href="/"
        }
    }

    // click side bar
    function onClickShowSideBar(){
        toggleIsShowedSideBar();
    }
    const toggleIsShowedSideBar = ()=>{
        setIsShowedSideBar(status => !status);
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
                    <div className="logo cursor-pointer" onClick={onClickShowSideBar}>
                        <span className='mx-3'><i><FontAwesomeIcon icon={faBars}/></i></span>
                        <span className="logo-name">Phucx</span>
                    </div>
                    <div className={`sidebar ${isShowedSideBar?'show-side-bar':''}`}>
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