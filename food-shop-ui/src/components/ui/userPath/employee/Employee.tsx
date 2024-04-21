import { useEffect, useRef, useState } from 'react';
// import './Customer.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { Routes, Route, useLocation } from 'react-router-dom';
import { employeeInfo, employeeNotification, employeeOrder, homePath } from '../../../../constant/FoodShoppingURL';
import { logout } from '../../../../api/AuthorizationApi';
import EmployeeInfomationComponent from './employeeinfomation/EmployeeInfomation';
import EmployeeOrdersComponent from './employeeorder/EmployeeOrders';
import EmployeeOrderComponent from './employeeorder/EmployeeOrder';
import EmployeeNotificationComponent from './notification/EmployeeNotification';
import EmployeePendingOrderComponent from './employeeorder/EmployeePendingOrder';

export default function EmployeeComponent(){
    // const [customerInfo, setCustomerInfo] = useState<Customer>();
    const sidebarRef = useRef(null)
    const location = useLocation()
    const [selectedPath, setSelectedPath] = useState(0);

    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        const path = location.pathname;
        // const subPath = path.substring(customerPath.length, path.length);
        if(path==employeeInfo){
            // console.log(customerInfo)
            setSelectedPath(0);
        }else if(path===employeeOrder){
            setSelectedPath(1);
        }else if(path === employeeNotification){
            setSelectedPath(2);
        }
        // console.log(subPath)
    }

    async function onClickLogoutButton(){
        logout().then((_res)=>{
            window.location.href=homePath;
        })
    }


    function onClickShowSideBar(){
        if(sidebarRef.current){
            const sidebarEle = sidebarRef.current as HTMLDivElement;
            sidebarEle.classList.toggle('show-side-bar')
        }
    }

    return(
        <div className='my-4 customer-page p-4 position-relative'>
            <div className="container my-5">
                <nav className='z-3'>
                    <div className="logo cursor-pointer" onClick={onClickShowSideBar}>
                        {/* <i className="bx bx-menu menu-icon"></i> */}
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
                                    <a href={employeeInfo} className={`nav-link ${selectedPath===0?'active': ''}`}>
                                        <i className="bx bx-home-alt icon"></i>
                                        <span className="link">User{`\u00A0`}Infomation</span>
                                    </a>
                                </li>
                                <li className="list nav-item">
                                    <a href={employeeOrder} className={`nav-link ${selectedPath===1?'active': ''}`}>
                                        <i className="bx bx-bar-chart-alt-2 icon"></i>
                                        <span className="link">Orders</span>
                                    </a>
                                </li>
                                <li className="list nav-item">
                                    <a href={employeeNotification} className={`nav-link ${selectedPath===2?'active': ''}`}>
                                        <i className="bx bx-bell icon"></i>
                                        <span className="link">Notifications</span>
                                    </a>
                                </li>
                            </ul>
                            <hr />
                            <ul className="bottom-cotent flex-column lists nav nav-pills">
                                {/* <li className="list nav-item">
                                    <a href="#" className="nav-link">
                                        <i className="bx bx-cog icon"></i>
                                        <span className="link">Settings</span>
                                    </a>
                                </li> */}
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
                <div >
                    <Routes>
                        <Route path='*' element={<EmployeeInfomationComponent/>}></Route>
                        <Route path='info' element={<EmployeeInfomationComponent/>}></Route>
                        <Route path='order' element={<EmployeeOrdersComponent/>}></Route>
                        <Route path='order/:orderId' element={<EmployeeOrderComponent/>}></Route>
                        <Route path='order/pending/:orderId' element={<EmployeePendingOrderComponent/>} ></Route>
                        <Route path='notification' element={<EmployeeNotificationComponent/>}></Route>
                    </Routes>
                </div>  
            </div>
        </div>
    );
}