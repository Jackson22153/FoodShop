// import { useContext, useEffect, useRef } from "react";
// import userInfoContext from "../../../contexts/UserInfoContext";
// import { customerPath } from "../../../../constant/FoodShoppingURL";
// import { logout } from "../../../../api/AuthorizationApi";

// export default function AppHeaderUser(){
//     const userInfo = useContext(userInfoContext);
//     const dropdownRef = useRef<HTMLLIElement>(null);

//     useEffect(() => {

//     }, []);

//     const onClickLogout = ()=>{
//         logout().then((_res) =>{

//             window.location.href = "/";
//         })
//     }

//     return(
//         <div className="navbar-expand-lg navbar-light text-white">
//             <div className="collapse navbar-collapse" id="navbarNavDropdown">
//                 <ul className="navbar-nav ml-auto">
//                     <li className="nav-item dropdown" ref={dropdownRef}>
//                         <span className="nav-link dropdown-toggle" id="navbarDropdownMenuLink" role="button" 
//                             data-toggle="dropdown" aria-haspopup="true" aria-expanded={isOpeningDropdown} >
//                             {userInfo.user.username}
//                         </span>
//                         <div id="appheader-user" className={`dropdown-menu ${isOpeningDropdown? 'show': ''}`} 
//                             aria-labelledby="navbarDropdownMenuLink">
//                             <a className="dropdown-item cursor-pointer" href={customerPath}>Profile</a>
//                             <a className="dropdown-item cursor-pointer" href="#">Settings</a>
//                             <div className="dropdown-divider"></div>
//                             <span className="dropdown-item cursor-pointer" onClick={onClickLogout}>Logout</span>
//                         </div>
//                     </li>
//                 </ul>
//             </div>
//         </div>
//     );
// }