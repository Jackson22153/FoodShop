import { useEffect, useRef, useState } from 'react';
import './Customer.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { Routes, Route, useLocation } from 'react-router-dom';
import UserInfomationComponent from './userinfomation/UserInfomation';
import { customerInfo, customerNotification, customerOrder, homePath } from '../../../../constant/FoodShoppingURL';
import UserOrdersComponent from './userorder/UserOrders';
import UserOrderComponent from './userorder/UserOrder';
import UserNotificationComponent from './notification/UserNotification';
import { logout } from '../../../../api/AuthorizationApi';

export default function CustomerComponent(){
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
        if(path==customerInfo){
            // console.log(customerInfo)
            setSelectedPath(0);
        }else if(path===customerOrder){
            setSelectedPath(1);
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
                 {/* <form method="post">
                     <div className="row">
                         <div className="col-md-4">
                             <div className="profile-img">
                                 <img src={displayUserImage(customerInfo.picture)} alt=""/>
                                 <div className="file btn btn-lg btn-primary">
                                     Change Photo
                                     <input type="file" name="file"/>
                                 </div>
                             </div>
                         </div>
                         <div className="col-md-6">
                             <div className="profile-head">
                                     <h5>
                                         {customerInfo.username}
                                     </h5>

                                     <h6>
                                         Web Developer and Designer
                                     </h6>
                                     <p className="proile-rating">RANKINGS : <span>8/10</span></p>
                                 <ul className="nav nav-tabs" id="myTab" role="tablist">
                                     <li className="nav-item">
                                         <a className="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">About</a>
                                     </li>
                                     <li className="nav-item">
                                         <a className="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">Timeline</a>
                                     </li>
                                 </ul>
                             </div>
                         </div>
                         <div className="col-md-2">
                             <input type="submit" className="profile-edit-btn" name="btnAddMore" value="Edit Profile"/>
                         </div>
                     </div>
                     <div className="row">
                         <div className="col-md-4">
                             <div className="profile-work">
                                 <p>WORK LINK</p>
                                 <a href="">Website Link</a><br/>
                                 <a href="">Bootsnipp Profile</a><br/>
                                 <a href="">Bootply Profile</a>
                                 <p>SKILLS</p>
                                 <a href="">Web Designer</a><br/>
                                 <a href="">Web Developer</a><br/>
                                 <a href="">WordPress</a><br/>
                                 <a href="">WooCommerce</a><br/>
                                 <a href="">PHP, .Net</a><br/>
                             </div>
                         </div>
                         <div className="col-md-8">
                             <div className="tab-content profile-tab" id="myTabContent">
                                 <div className="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                             <div className="row">
                                                 <div className="col-md-6">
                                                     <label>Customer Id</label>
                                                 </div>
                                                 <div className="col-md-6">
                                                     <p>{customerInfo.customerID}</p>
                                                 </div>
                                             </div>
                                             <div className="row">
                                                 <div className="col-md-6">
                                                     <label>Name</label>
                                                 </div>
                                                 <div className="col-md-6">
                                                     <p>{customerInfo.contactName}</p>
                                                 </div>
                                             </div>
                                             <div className="row">
                                                 <div className="col-md-6">
                                                     <label>Email</label>
                                                 </div>
                                                 <div className="col-md-6">
                                                     <p>kshitighelani@gmail.com</p>
                                                 </div>
                                             </div>
                                             <div className="row">
                                                 <div className="col-md-6">
                                                     <label>Phone</label>
                                                 </div>
                                                 <div className="col-md-6">
                                                     <p>{customerInfo.phone}</p>
                                                 </div>
                                             </div>
                                             {/* <div className="row">
                                                 <div className="col-md-6">
                                                     <label>Profession</label>
                                                 </div>
                                                 <div className="col-md-6">
                                                     <p>Web Developer and Designer</p>
                                                 </div>
                                             </div> */}
                                 {/* </div>
                                 <div className="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                                             <div className="row">
                                                 <div className="col-md-6">
                                                     <label>Experience</label>
                                                 </div>
                                                 <div className="col-md-6">
                                                     <p>Expert</p>
                                                 </div>
                                             </div>
                                             <div className="row">
                                                 <div className="col-md-6">
                                                     <label>Hourly Rate</label>
                                                 </div>
                                                 <div className="col-md-6">
                                                     <p>10$/hr</p>
                                                 </div>
                                             </div>
                                             <div className="row">
                                                 <div className="col-md-6">
                                                     <label>Total Projects</label>
                                                 </div>
                                                 <div className="col-md-6">
                                                     <p>230</p>
                                                 </div>
                                             </div>
                                             <div className="row">
                                                 <div className="col-md-6">
                                                     <label>English Level</label>
                                                 </div>
                                                 <div className="col-md-6">
                                                     <p>Expert</p>
                                                 </div>
                                             </div>
                                             <div className="row">
                                                 <div className="col-md-6">
                                                     <label>Availability</label>
                                                 </div>
                                                 <div className="col-md-6">
                                                     <p>6 months</p>
                                                 </div>
                                             </div>
                                     <div className="row">
                                         <div className="col-md-12">
                                             <label>Your Bio</label><br/>
                                             <p>Your detail description</p>
                                         </div>
                                     </div>
                                 </div>
                             </div>
                         </div>
                     </div> */}
                 {/* </form>   */}
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