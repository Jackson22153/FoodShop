import { UserInfo } from "../../../../model/Type";
import { useState } from "react";
import { ROLE } from "../../../../constant/WebConstant";
import { ADMIN_PATH, CUSTOMER_PATH, EMPLOYEE_PATH } from "../../../../constant/FoodShoppingURL";
import { Modal } from "../../../../model/WebType";
import ModalComponent from "../modal/Modal";
import { logout } from "../../../../api/AuthorizationApi";
import NotificationDropdown from "../notification-dropdown/NotificationDropdown";
import { Link } from "react-router-dom";

interface Props{
    userInfo: UserInfo
}
function UserInfoNav(prop: Props) {
    const [isExpandedUserDropdown, setIsExpandedUserDropdown] = useState(false);

    // modal
    const [logoutModal, setLogoutModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })

    // logout
    const onClickLogout = ()=>{
        onClickToggleModal();
    }
    const toggleModal = ()=>{
        setLogoutModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }
    const onClickToggleModal = async ()=>{
        toggleModal();
    }

    const onClickConfirmModal = async ()=>{
        try {
            const res = await logout();
            if(res.status){

            }
        } catch (error) {
        } finally{
            window.location.href = "/"
        }
    }

    // toggle user dropdown
    const toggleUserExpanded = (status: boolean)=>{
        setIsExpandedUserDropdown(status)
    }
    const onClickShowUserDropdown = ()=>{
        toggleUserExpanded(true);
    }
    const onClickCloseUserDropdown = ()=>{
        toggleUserExpanded(false);
    }


    // USER ROLES
    const roleNames = ()=>{
        if(prop.userInfo.roles){
            const arr = prop.userInfo.roles.map(role => role.toLowerCase());
            return arr;
        }else return []
    }

    return(
        <div className="navbar-expand navbar-light text-white">
            <div id="navbarNavDropdown">
                <ul className="navbar-nav ml-auto">
                    <li className="nav-item dropdown pe-4">
                        <NotificationDropdown roles={roleNames()}/>
                    </li>
                    <li className="nav-item dropdown" onMouseEnter={onClickShowUserDropdown} onMouseLeave={onClickCloseUserDropdown}>
                        <div className="nav-link dropdown-toggle d-flex align-items-center" id="navbarDropdownMenuLink" role="button" 
                            data-toggle="dropdown" aria-haspopup={isExpandedUserDropdown} 
                            aria-expanded={isExpandedUserDropdown}>
                            {prop.userInfo.user.username}
                        </div>
                        <div id="appheader-user" className={`dropdown-menu ${isExpandedUserDropdown?'show':''}`} 
                            aria-labelledby="navbarDropdownMenuLink">
                            {roleNames().includes(ROLE.CUSTOMER.toLowerCase())&&
                                <Link to={CUSTOMER_PATH}>
                                    <div className="dropdown-item cursor-pointer">Profile</div>
                                </Link>
                            }
                            {roleNames().includes(ROLE.EMPLOYEE.toLowerCase())&&
                                <Link to={EMPLOYEE_PATH}>
                                    <div className="dropdown-item cursor-pointer">Profile</div>
                                </Link>
                            }
                            {roleNames().includes(ROLE.ADMIN.toLowerCase())&&     
                                <Link to={ADMIN_PATH}>
                                    <div className="dropdown-item cursor-pointer">Admin Dashboard</div>
                                </Link>
                            }
                            <div className="dropdown-divider"></div>
                            <span className="dropdown-item cursor-pointer" onClick={onClickLogout}>Logout</span>
                        </div>
                    </li>
                </ul>
            </div>
            <ModalComponent modal={logoutModal} handleCloseButton={onClickToggleModal} 
                handleConfirmButton={onClickConfirmModal}/>
        </div>
    )
}
export default UserInfoNav;