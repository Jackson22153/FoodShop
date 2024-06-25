import { UserInfo } from "../../../../model/Type";
import { useState } from "react";
import { ROLE } from "../../../../constant/WebConstant";
import { ADMIN_PATH, CUSTOMER_PATH, EMPLOYEE_PATH } from "../../../../constant/FoodShoppingURL";
import { Modal } from "../../../../model/WebType";
import ModalComponent from "../modal/Modal";
import { logout } from "../../../../api/AuthorizationApi";
import NotificationDropdown from "../notification-dropdown/NotificationDropdown";

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
        const arr = prop.userInfo.roles.map(role => role.roleName.toLowerCase());
        return arr;
    }

    return(
        <div className="navbar-expand navbar-light text-white">
            <div id="navbarNavDropdown">
                <ul className="navbar-nav ml-auto">
                    <li className="nav-item dropdown pe-4">
                        <NotificationDropdown roles={roleNames()}/>
                    </li>
                    <li className="nav-item dropdown" onMouseEnter={onClickShowUserDropdown} onMouseLeave={onClickCloseUserDropdown}>
                        <span className="nav-link dropdown-toggle" id="navbarDropdownMenuLink" role="button" 
                            data-toggle="dropdown" aria-haspopup='true' aria-expanded={isExpandedUserDropdown} >
                            {prop.userInfo.user.username}
                        </span>
                        <div id="appheader-user" className={`dropdown-menu ${isExpandedUserDropdown?'show':''}`} 
                            aria-labelledby="navbarDropdownMenuLink">
                            {roleNames().includes(ROLE.CUSTOMER.toLowerCase())&&
                                <a className="dropdown-item cursor-pointer" href={CUSTOMER_PATH}>Profile</a>
                            }
                            {roleNames().includes(ROLE.EMPLOYEE.toLowerCase())&&
                                <a className="dropdown-item cursor-pointer" href={EMPLOYEE_PATH}>Profile</a>
                            }
                            {roleNames().includes(ROLE.ADMIN.toLowerCase())&&     
                                <a className="dropdown-item cursor-pointer" href={ADMIN_PATH}>Admin Dashboard</a>
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