import { ChangeEventHandler, MouseEventHandler, useEffect, useRef, useState } from "react";
import { EmployeeDetail } from "../../../../../model/Type";
import { getEmployee, updateEmployee } from "../../../../../api/AdminApi";
import { useParams } from "react-router-dom";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import dayjs from "dayjs";
import { PickerChangeHandlerContext, DateValidationError } from "@mui/x-date-pickers/models";
import ModalComponent from "../../../../shared/functions/modal/Modal";
import { Alert, Modal } from "../../../../../model/WebType";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import { ALERT_TIMEOUT, ALERT_TYPE } from "../../../../../constant/WebConstant";
import { EmployeeImageChangeInput } from "../../../../shared/functions/employee-image-change/EmployeeImageChangeInput";

export default function AdminEmployeeComponent(){
    const {employeeID} = useParams();
    const [employeeInfo, setEmployeeInfo] = useState<EmployeeDetail>({
        employeeID: "",
        userID: "",
        username: "",
        email: "",
        lastName: "",
        firstName: "",
        birthDate: "",
        hireDate: "",
        address: "",
        city: "",
        phone: "",
        picture: "",
        title: "",
        notes: ""
    });
    const [editable, setEditable] = useState(false);
    const navHeaderRef = useRef(null);
    const [selectedTab, setSelectedTab] = useState(0);
    const [updateInfoModal, setUpdateInfoModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: "",
        isShowed: false
    })

    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        if(employeeID){
            fetchEmployee(employeeID);
        }
    }

    const fetchEmployee = async (employeeID: string)=>{
        const res = await getEmployee(employeeID);
        if(res.status){
            const data = res.data;
            setEmployeeInfo({
                employeeID: data.employeeID,
                userID: data.userID,
                username: data.username,
                email: data.email,
                lastName: data.lastName || '',
                firstName: data.firstName || '',
                birthDate: data.birthDate||'',
                hireDate: data.hireDate||'',
                address: data.address||'',
                city: data.city||'',
                phone: data.phone||'',
                picture: data.picture||'',
                notes: data.notes||'',
                title: data.title,

            });
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

    // change employee picture
    const onChangePicture = (imageSrc: string)=>{
        setEmployeeInfo({...employeeInfo, ['picture']:imageSrc})
    }

    const onClickSelectTab= (tab:number)=>{
        setSelectedTab(tab);
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
                const updateEmployeeData = {
                    employeeID: employeeInfo.employeeID,
                    userID: employeeInfo.userID,
                    birthDate: employeeInfo.birthDate,
                    hireDate: employeeInfo.hireDate,
                    phone: employeeInfo.phone,
                    picture: employeeInfo.picture,
                    title: employeeInfo.title,
                    address: employeeInfo.address,
                    city: employeeInfo.city,
                    notes: employeeInfo.notes
                }
                const res = await updateEmployee(updateEmployeeData);
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

    return(
        <div className="container">
            <AlertComponent alert={alert}/>
            {employeeInfo &&
                <>
                    <div className="row emp-profile box-shadow-default mb-5">
                        <div className="col-md-4">
                            <EmployeeImageChangeInput imageSrc={employeeInfo.picture} disable={!editable}
                                onChangePicture={onChangePicture}/>
                        </div>
                        <div className="col-md 6">
                            <div className="profile-head">
                                <h5>
                                    Username: {employeeInfo.username}
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
                                                        placeholder="First Name" value={employeeInfo.firstName}
                                                        readOnly name="firstName"/>
                                                </div>
                                                <div className="col-md-3 mb-3">
                                                    <label  htmlFor="last-name-employee">Last Name</label>
                                                    <input type="text" className="form-control" id="last-name-employee" 
                                                        placeholder="Last Name" value={employeeInfo.lastName} 
                                                        readOnly name="lastName"/>
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
                                                </div>
                                            </div>
                                            
                                            <div className="form-row row">
                                                <div className="col-md-4 mb-3">
                                                    <label  htmlFor="email-employee">Email</label>
                                                    <input type="email" className="form-control" id="email-employee" placeholder="Email" required
                                                        value={employeeInfo.email} readOnly
                                                        name="email"/>
                                                </div>
                                                <div className="col-md-3 mb-3">
                                                    <label  htmlFor="home-phone-employee">Phone</label>
                                                    <input type="text" className="form-control" id="home-phone-employee" placeholder="Phone" required
                                                        value={employeeInfo.phone} readOnly
                                                        name="phone"/>
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
                                                    placeholder="UserID" value={employeeInfo.userID} 
                                                    readOnly name="firstName"/>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col-md-3 mb-3">
                                                <label  htmlFor="username-employee">Username</label>
                                                <input type="text" className="form-control" id="username-employee" 
                                                    placeholder="Username" value={employeeInfo.username} required
                                                    readOnly name="username"/>
                                            </div>
                                            <div className="col-md-5 mb-3">
                                                <label  htmlFor="email-employee">Email</label>
                                                <input type="email" className="form-control" id="email-employee" placeholder="Email" required
                                                    value={employeeInfo.email}  readOnly name="email"/>
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