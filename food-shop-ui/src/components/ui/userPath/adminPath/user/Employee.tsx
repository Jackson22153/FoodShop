import { ChangeEventHandler, MouseEventHandler, useEffect, useRef, useState } from "react";
import { EmployeeDetail, Role } from "../../../../../model/Type";
import { assignRoles, getEmployee, getRolesEmployee, resetPassword, updateEmployee } from "../../../../../api/AdminApi";
import { useParams } from "react-router-dom";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import dayjs from "dayjs";
import { PickerChangeHandlerContext, DateValidationError } from "@mui/x-date-pickers/models";
import ModalComponent from "../../../../shared/functions/modal/Modal";
import { Alert, Modal } from "../../../../../model/WebType";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import { ALERT_TIMEOUT, ALERT_TYPE } from "../../../../../constant/config";
import { UserImageChangeInput } from "../../../../shared/functions/user-image-change-input/UserImageChangeInput";

export default function AdminEmployeeComponent(){
    const {employeeID} = useParams();
    const [employeeInfo, setEmployeeInfo] = useState<EmployeeDetail>();
    const [employeeRoles, setEmployeeRoles] = useState<Role[]>([]);
    const [roles, setRoles] = useState<Role[]>([]);
    const [isRolesDropdownShowed, setIsRolesDropdownShowed] = useState(false);
    const [editable, setEditable] = useState(false);
    const navHeaderRef = useRef(null);
    const [selectedTab, setSelectedTab] = useState(0);
    const [updateInfoModal, setUpdateInfoModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })
    const [resetPasswordModal, setResetPasswordModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })
    const [assignRolesModal, setAssignRolesModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: "",
        isShowed: false
    })

    const roleDropDownRef = useRef<HTMLDivElement>(null);

    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        document.addEventListener('click', onClickCloseRoleDropdownOutsideRange)
        if(employeeID){
            fetchEmployee(employeeID);
            fetchRoles(0);
        }
    }

    const fetchEmployee = async (employeeID: string)=>{
        const res = await getEmployee(employeeID);
        if(res.status){
            const data = res.data;
            // console.log(data)
            setEmployeeInfo({
                employeeID: data.employeeID,
                lastName: data.lastName || '',
                firstName: data.firstName || '',
                birthDate: data.birthDate||'',
                hireDate: data.hireDate||'',
                address: data.address||'',
                city: data.city||'',
                homePhone: data.homePhone||'',
                description: data.description||'',
                photo: data.photo||'',
                notes: data.notes||'',
                userInfo: {
                    user:{
                        userID: data.userInfo.user.userID||'',
                        username: data.userInfo.user.username||'',
                        email: data.userInfo.user.email||''
                    },
                    roles: data.userInfo.roles || []
                }
            });
            setEmployeeRoles(data.userInfo.roles || [])
        }
    }
    const fetchRoles = async (pageNumber:number)=>{
        const res = await getRolesEmployee(pageNumber);
        if(res.status){
            const data = res.data;
            setRoles(data.content);
        }
    }

    const onClickEditInfo:MouseEventHandler<HTMLButtonElement> = (_event)=>{
        setEditable(editable => !editable);
    }

    const onChangeEmployeeInfo: ChangeEventHandler<any> = (event)=>{
        if(employeeInfo){
            const name = event.target.name;
            const value = event.target.value;
            setEmployeeInfo({...employeeInfo, [name]:value})
        }
    }
    const onChangeEmployeeInfoHireDate = (value: any, _context: PickerChangeHandlerContext<DateValidationError>)=>{
        if(employeeInfo){
            const date = value.format('YYYY-MM-DD');
            setEmployeeInfo({...employeeInfo, hireDate: date})
        }
    }

    const onChangePicture = (_event: any)=>{

    }

    const onClickSelectTab= (tab:number)=>{
        setSelectedTab(tab);
    }
    // reset modal
    const onClickResetPassword = ()=>{
        setResetPasswordModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }
    const onClickCloseModal = ()=>{
        setResetPasswordModal({...resetPasswordModal, isShowed:false})
    }
    const onClickConfirmResetPassModal = async ()=>{
        try {
            const userID = employeeInfo?.userInfo.user.userID;
            const res = await resetPassword(userID);
            if(res.status){
                const data = res.data
                const status = data.status
                setAlert({
                    message: status?"Password has been reset successfully":"Password can not be reset",
                    type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                    isShowed: true
                })   
            }
        } 
        catch (error) {
            setAlert({
                message: "Password can not be reset",
                type: ALERT_TYPE.DANGER,
                isShowed: true
            }) 
        }
        finally{
            setTimeout(()=>{
                setAlert({...alert, isShowed: false});
            }, ALERT_TIMEOUT)
        }
    }
    // update info modal
    const toggleUpdateInfoModal = ()=>{
        setUpdateInfoModal(modal => ({...modal, isShowed:!modal.isShowed}))
    }
    const onClickUpdate:MouseEventHandler<HTMLButtonElement> = (event)=>{
        event.preventDefault();
        toggleUpdateInfoModal();
    }
    const onClickCloseUpdateInfoModal = ()=>{
        toggleUpdateInfoModal();
    }
    const onClickConfirmUpdateInfoModal = async ()=>{
        try {
            if(employeeInfo){
                const data = {
                    employeeID: employeeInfo.employeeID, 
                    firstName: employeeInfo.firstName, 
                    lastName: employeeInfo.lastName,
                    hireDate: employeeInfo.hireDate,
                    photo: employeeInfo.photo,
                    notes: employeeInfo.notes
                }
                console.log(data)
                const res = await updateEmployee(data);
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?"Employee's information has been updated successfully":"Employee's information can not be updated",
                        type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                        isShowed: true
                    })   
                }
            }
        } catch (error) {
            setAlert({
                message: "Employee's information can not be updated",
                type: ALERT_TYPE.DANGER,
                isShowed: true
            }) 
        } finally{
            setTimeout(()=>{
                setAlert({...alert, isShowed: false});
            }, ALERT_TIMEOUT)
        }
    }

    // change role employee
    const toggleRoleDropdown = ()=>{
        setIsRolesDropdownShowed(isShowed => !isShowed);
    }

    const onClickRoleDropdown = ()=>{
        console.log("click")
        toggleRoleDropdown();
    }

    const onClickCloseRoleDropdownOutsideRange = (event:any)=>{
        if(roleDropDownRef.current && !roleDropDownRef.current.contains(event.target as Node)){
            setIsRolesDropdownShowed(false);
        }
    }

    const onChangeSelectCheckBox:ChangeEventHandler<HTMLInputElement> = (event)=>{
        const roleID = +event.target.value;
        const isChecked = event.target.checked;
        const index = roles.findIndex((role)=> role.roleID===roleID);
        if(index>=0){
            const role = roles[index];
            if(isChecked){
                // add new role
                setEmployeeRoles([...employeeRoles, role]);
            }else{
                if(employeeRoles.length>1){
                    // remove role
                    var newRoles = [] as Role[];
                    for(var tempRole of employeeRoles){
                        if(tempRole.roleID!==roleID){
                            newRoles.push(tempRole);
                        }
                    }
                    setEmployeeRoles(newRoles);
                }
            }
        }
    }
    // assign modal
    const toggleAssignRolesModal = ()=>{
        setAssignRolesModal(modal => ({...modal, isShowed:!modal.isShowed}))
    }
    const onClickAssignRolesModal:MouseEventHandler<HTMLButtonElement> = (event)=>{
        event.preventDefault();
        toggleAssignRolesModal();
    }
    const onClickCloseAssignRolesModal = ()=>{
        toggleAssignRolesModal();
    }
    const onClickConfirmAssignRolesModal = async ()=>{
        try {
            if(employeeRoles && employeeInfo){
                // console.log(employeeRoles)
                const data = {
                    user: employeeInfo.userInfo.user,
                    roles: employeeRoles
                }
                const res = await assignRoles(data);
                if(res.status){
                    const data = res.data;
                    const status = data.status;
                    setAlert({
                        message: status?"Roles have been assigned to employee": "Roles can not be assigned to employee",
                        type: status?ALERT_TYPE.SUCCESS: ALERT_TYPE.DANGER,
                        isShowed: true
                    }) 
                }
            }
        } 
        catch (error) {
            setAlert({
                message: "Roles can not be assigned to employee",
                type: ALERT_TYPE.DANGER,
                isShowed: true
            }) 
        } finally{
            setTimeout(()=>{
                setAlert({...alert, isShowed: false});
            }, ALERT_TIMEOUT)
        }
    }

    return(
        <div className="container">
            <AlertComponent alert={alert}/>
            {employeeInfo &&
                <>
                    <div className="row emp-profile box-shadow-default mb-5">
                        <div className="col-md-4">
                            <UserImageChangeInput imageSrc={employeeInfo.photo} disable={!editable}
                                onChangePicture={onChangePicture}/>
                        </div>
                        <div className="col-md 6">
                            <div className="profile-head">
                                <h5>
                                    Username: {employeeInfo.userInfo.user.username}
                                </h5>
                            </div>
                            <div className="profile-about">
                                <div className="row my-3 justify-content-end">
                                    <div className="col-md-3 col-sm-4">
                                        <button className="profile-edit-btn btn btn-secondary w-100 rounded-2 p-1" onClick={onClickEditInfo}>
                                            Edit{`\u00A0`}Profile
                                        </button>
                                    </div>
                                </div>

                                <ul className="nav nav-tabs emp-profile p-0 mb-3 cursor-pointer" 
                                    role="tablist" ref={navHeaderRef}>
                                    <li className="nav-item" role="presentation">
                                        <span className={`nav-link text-dark ${selectedTab===0?'active':''}`}
                                            id="employee-info-tab" role="tab" aria-controls="employee-info-tab"
                                            aria-selected="true" onClick={(_e)=>onClickSelectTab(0)}>Information</span>
                                    </li>
                                    <li className="nav-item" role="presentation">
                                        <span className={`nav-link text-dark ${selectedTab===1?'active':''}`}
                                            id="pending-order-tab" role="tab" onClick={(_e)=>onClickSelectTab(1)}>Account</span>
                                    </li>
                                </ul>

                                {selectedTab===0 ?
                                    <div className={`row fade ${selectedTab===0?'show':''}`}>
                                        <form action="post">
                                            <div className="form-row row">
                                                <div className="col-md-3 mb-3">
                                                    <label  htmlFor="first-name-employee">First Name</label>
                                                    <input type="text" className="form-control" id="first-name-employee" 
                                                        placeholder="First Name" value={employeeInfo.firstName} required
                                                        disabled={!editable} name="firstName" onChange={onChangeEmployeeInfo}/>
                                                </div>
                                                <div className="col-md-3 mb-3">
                                                    <label  htmlFor="last-name-employee">Last Name</label>
                                                    <input type="text" className="form-control" id="last-name-employee" 
                                                        placeholder="Last Name"required value={employeeInfo.lastName} 
                                                        disabled={!editable} name="lastName" onChange={onChangeEmployeeInfo}/>
                                                </div>
                                                <div className="col-md-4 mb-3">
                                                    <label>Birth Date</label>
                                                    <div>
                                                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                            <DatePicker value={dayjs(employeeInfo.birthDate)} 
                                                                readOnly
                                                                className="form-control"
                                                                name="birthDate"
                                                                slotProps={{
                                                                    textField: { size: 'small' },  // Set the size here
                                                                }}
                                                            />
                                                        </LocalizationProvider>
                                                    </div>
                                                    <div className="invalid-feedback">
                                                        Looks good!
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="form-row row">
                                                <div className="col-md-7 mb-3">
                                                    <label  htmlFor="address-employee">Address</label>
                                                    <input type="text" className="form-control" id="address-employee" placeholder="Address" required
                                                        value={employeeInfo.address} readOnly
                                                        name="address"/>
                                                    <div className="invalid-feedback">
                                                        Please provide a valid address.
                                                    </div>
                                                </div>
                                                <div className="col-md-3 mb-3">
                                                    <label  htmlFor="city-employee">City</label>
                                                    <input type="text" className="form-control" id="city-employee" placeholder="City" required
                                                        value={employeeInfo.city} readOnly
                                                        name="city"/>
                                                    <div className="invalid-feedback">
                                                        Please provide a valid city.
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <div className="form-row row">
                                                <div className="col-md-4 mb-3">
                                                    <label  htmlFor="email-employee">Email</label>
                                                    <input type="email" className="form-control" id="email-employee" placeholder="Email" required
                                                        value={employeeInfo.userInfo.user.email} readOnly
                                                        name="email"/>
                                                    <div className="invalid-feedback">
                                                        Please provide a valid email.
                                                    </div>
                                                </div>
                                                <div className="col-md-3 mb-3">
                                                    <label  htmlFor="home-phone-employee">Phone</label>
                                                    <input type="text" className="form-control" id="home-phone-employee" placeholder="Phone" required
                                                        value={employeeInfo.homePhone} readOnly
                                                        name="homePhone"/>
                                                    <div className="invalid-feedback">
                                                        Please provide a valid phone.
                                                    </div>
                                                </div>

                                                <div className="col-md-3">
                                                    <label>Hire Date</label>
                                                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                        <DatePicker value={dayjs(employeeInfo.hireDate)} 
                                                            onChange={onChangeEmployeeInfoHireDate} 
                                                            className="form-control"
                                                            name="hireDate"
                                                            slotProps={{
                                                                textField: { size: 'small' },  // Set the size here
                                                                }}
                                                            disabled={!editable}
                                                        />
                                                    </LocalizationProvider>
                                                </div>
                                            </div>

                                            <div className="form-group col-md-10 my-3">
                                                <label htmlFor="employee-description-admin">Description</label>
                                                <textarea className="form-control" id="employee-description-admin" rows={3} 
                                                    onChange={onChangeEmployeeInfo} value={employeeInfo.notes} name='notes'
                                                    required disabled={!editable}/>
                                            </div>
                                            <div className="row">
                                                <div className="col-md-5 mt-2">
                                                    <button className="btn btn-primary" type="submit" onClick={onClickUpdate}
                                                        disabled={!editable}>
                                                        Update{`\u00A0`}Infomation
                                                    </button>
                                                </div>
                                                <ModalComponent modal={updateInfoModal} 
                                                    handleCloseButton={onClickCloseUpdateInfoModal}
                                                    handleConfirmButton={onClickConfirmUpdateInfoModal}/>
                                            </div>
                                        </form>
                                    </div>:
                                selectedTab===1 &&
                                    <div className={`row fade ${selectedTab===1?'show':''}`}>
                                        <div className="row">
                                            <div className="col-md-6 mb-3">
                                                <label  htmlFor="userID-employee">UserID</label>
                                                <input type="text" className="form-control" id="userID-employee" 
                                                    placeholder="UserID" value={employeeInfo.userInfo.user.userID} 
                                                    readOnly
                                                    name="firstName"/>
                                                <div className="valid-feedback">
                                                    Looks good!
                                                </div>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col-md-3 mb-3">
                                                <label  htmlFor="username-employee">Username</label>
                                                <input type="text" className="form-control" id="username-employee" 
                                                    placeholder="Username" value={employeeInfo.userInfo.user.username} required
                                                    readOnly name="username"/>
                                                <div className="valid-feedback">
                                                    Looks good!
                                                </div>
                                            </div>
                                            <div className="col-md-5 mb-3">
                                                <label  htmlFor="email-employee">Email</label>
                                                <input type="email" className="form-control" id="email-employee" placeholder="Email" required
                                                    value={employeeInfo.userInfo.user.email}  readOnly name="email"/>
                                                <div className="invalid-feedback">
                                                    Please provide a valid email.
                                                </div>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col-3">
                                                <div className="btn-group w-100" ref={roleDropDownRef}>
                                                    <span className="btn btn-info text-white">Roles</span>
                                                    <button type="button" className="btn btn-info dropdown-toggle dropdown-toggle-split" 
                                                        data-toggle="dropdown" aria-haspopup="true" aria-expanded={isRolesDropdownShowed} 
                                                        onClick={onClickRoleDropdown} disabled={!editable}> 
                                                        <span className="sr-only">Toggle Dropdown</span>
                                                    </button>
                                                    <div className={`dropdown-menu role-dropdown-menu-pos ${isRolesDropdownShowed?'show':''}`}>
                                                        {roles.map((role)=>(
                                                            <div key={role.roleID} className="dropdown-item">
                                                                <div className="form-check">
                                                                    <input className="form-check-input" type="checkbox" value={role.roleID} 
                                                                        id={`${role.roleName}-checkbox-role-admin`} checked={
                                                                            employeeRoles.findIndex((employeeRole)=> employeeRole.roleID===role.roleID)>=0
                                                                        } onChange={onChangeSelectCheckBox}/>
                                                                    <label className="form-check-label" htmlFor={`${role.roleName}-checkbox-role-admin`}>
                                                                        {role.roleName}
                                                                    </label>
                                                                </div>
                                                            </div>
                                                        ))}
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="col-9">
                                                <ul className="list-group d-flex flex-row gap-2">
                                                    {employeeRoles.map((role)=>(
                                                        <li key={role.roleID} className="cursor-default border-0 rounded-2 list-group-item bg-light text-dark">
                                                            {role.roleName}
                                                        </li>
                                                    ))}
                                                </ul>
                                            </div>
                                        </div>
                                        <div className="row btn-group">
                                            <div className="col-md-3 mt-2">
                                                <button className="btn btn-primary" type="submit" onClick={onClickResetPassword}
                                                    disabled={!editable}>
                                                    Reset{`\u00A0`}Password
                                                </button>
                                                <ModalComponent modal={resetPasswordModal} 
                                                    handleCloseButton={onClickCloseModal} 
                                                    handleConfirmButton={onClickConfirmResetPassModal}/>
                                            </div>
                                            <div className="col-md-3 mt-2">
                                                <button className="btn btn-primary" type="submit" onClick={onClickAssignRolesModal}
                                                    disabled={!editable}>
                                                    Assign{`\u00A0`}Roles
                                                </button>
                                                <ModalComponent modal={assignRolesModal} 
                                                    handleCloseButton={onClickCloseAssignRolesModal} 
                                                    handleConfirmButton={onClickConfirmAssignRolesModal}/>
                                            </div>
                                        </div>
                                    </div>
                                }
                            </div>
                        </div>
                    </div>
                </>
            }
        </div>
    );
}