import { useContext, useEffect, useRef } from "react";
import userInfoContext from "../../../contexts/UserInfoContext";
import { isOpeningUserDropDownContext } from "../../../ui/homePath/home/Home";
import { customerPath } from "../../../../constant/FoodShoppingURL";
import { logout } from "../../../../api/AuthorizationApi";

export default function AppHeaderUser(){
    const userInfo = useContext(userInfoContext);
    const isOpeningDropdown = useContext(isOpeningUserDropDownContext);
    const dropdownRef = useRef<HTMLLIElement>(null);

    useEffect(() => {
        // disable user dropdown when clicking outside of the dropdown
        // const handleClickOutside = (event: any) => {
        //     const dropdown = dropdownRef.current;
        //     if(dropdown){
        //         if (!dropdown.contains(event.target)) {
        //           handleIsOpeningUserDropDown(false)
        //         }
        //     }
        // };

        // document.addEventListener("mousedown", handleClickOutside);
        // return () => {
        //   document.removeEventListener("mousedown", handleClickOutside);
        // };
      }, []);

    // const handleIsOpeningUserDropDown = (status: boolean) =>{
    //     prop.handleIsOpeningDropdown(status);
    // }
    // const onClickUser: MouseEventHandler<any> = (_event) =>{
    //     handleIsOpeningUserDropDown(!isOpeningDropdown);
    // }

    const onClickLogout = ()=>{
        logout().then((_res) =>{

            window.location.href = "/";
        })
    }

    const onClickOpeningUserDropDown = ()=>{

    }


    // if(isOpeningDropdown) return null;

    return(
        <div className="navbar-expand-lg navbar-light text-white">
            <div className="collapse navbar-collapse" id="navbarNavDropdown">
                <ul className="navbar-nav ml-auto">
                    <li className="nav-item dropdown" ref={dropdownRef}>
                        <span className="nav-link dropdown-toggle" id="navbarDropdownMenuLink" role="button" 
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded={isOpeningDropdown} >
                            {userInfo.username}
                        </span>
                        <div id="appheader-user" className={`dropdown-menu ${isOpeningDropdown? 'show': ''}`} 
                            aria-labelledby="navbarDropdownMenuLink">
                            <a className="dropdown-item cursor-pointer" href={customerPath}>Profile</a>
                            <a className="dropdown-item cursor-pointer" href="#">Settings</a>
                            <div className="dropdown-divider"></div>
                            <span className="dropdown-item cursor-pointer" onClick={onClickLogout}>Logout</span>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    );
}