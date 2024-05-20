import { ChangeEventHandler, FormEvent, useEffect, useState } from "react";
import { Employee } from "../../../../../model/Type";
import { getEmployeeInfo, updateEmployeeInfo } from "../../../../../api/EmployeeApi";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import dayjs, { Dayjs } from "dayjs";
import { Alert, Modal } from "../../../../../model/WebType";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import ModalComponent from "../../../../shared/functions/modal/Modal";
import { ALERT_TYPE, ALERT_TIMEOUT } from "../../../../../constant/config";
import { UserImageChangeInput } from "../../../../shared/functions/user-image-change-input/UserImageChangeInput";


export default function EmployeeInfomationComponent(){
    const [employeeInfo, setEmployeeInfo] = useState<Employee>();
    const [employeeInfoAlter, setEmployeeInfoAlter] = useState<Employee>()
    const [editable, setEditable] = useState(true)
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: "",
        isShowed: false
    })
    const [modal, setModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })

    useEffect(()=>{
        fetchEmployeeInfo();

    }, [])

    const fetchEmployeeInfo = async ()=>{
        const res = await getEmployeeInfo();
        if(res.status===200){
            const data = res.data;
            // console.log(data);
            const employee = {
                employeeID: data.employeeID,
                firstName: data.firstName || '',
                lastName: data.lastName || '',
                birthDate: data.birthDate || '',
                hireDate: data.hireDate || '',
                homePhone: data.homePhone || '',
                address: data.address || '',
                city: data.city || '',
                photo: data.photo || '',
                reportsTo: data.reportsTo || '',
                title: data.title || '',
                email: data.email || '',
                username: data.username || ''
            }
            // console.log(employee)
            setEmployeeInfo(employee);
            setEmployeeInfoAlter(employee);
        }
    }

    const onChangeEmployeeInfoBirthDate = (birthDate:Dayjs|null)=>{
        if(birthDate && employeeInfoAlter){
            // console.log(birthDate.format("YYYY-MM-DD"));
            const birthDateStr = birthDate.format("YYYY-MM-DD");
            setEmployeeInfoAlter({...employeeInfoAlter, birthDate: birthDateStr})
        }
    }
    const onChangeEmployeeInfo :ChangeEventHandler<HTMLInputElement> = (event)=>{
        if(employeeInfoAlter){
            const name = event.target.name;
            const value = event.target.value;
            // console.log(`name: ${name}, value:${value}`)
            setEmployeeInfoAlter({...employeeInfoAlter, [name]:value})
        }
    }

    const onClickEditInfo = ()=>{
        setEditable((edit) => !edit)
    }

    const onClickUpdate = async (event: FormEvent)=>{
        event.preventDefault();
        toggleModal()
    }

    const toggleModal = ()=>{
        setModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }

    const onClickCloseModal = ()=>{
        toggleModal()
    }
    const onClickConfirmModal = async ()=>{
        try {
            if(employeeInfoAlter && employeeInfo){
                // console.log(employeeInfo)
                const employee = {
                    employeeID: employeeInfoAlter.employeeID,
                    firstName: employeeInfoAlter.firstName || employeeInfo.firstName,
                    lastName: employeeInfoAlter.lastName || employeeInfo.lastName,
                    birthDate: employeeInfoAlter.birthDate || null,
                    homePhone: employeeInfoAlter.homePhone || null,
                    address: employeeInfoAlter.address || null,
                    city: employeeInfoAlter.city || null,
                    photo: employeeInfoAlter.photo || null,
                    email: employeeInfoAlter.email || employeeInfo.lastName,
                    username: employeeInfoAlter.username || employeeInfo.lastName
                };
                // console.log(employee)
                const res = await updateEmployeeInfo(employee);
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?"Information has been updated successfully":"Information can not be updated",
                        type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                        isShowed: true
                    })   
                }
            }
        } catch (error) {
            setAlert({
                message: "Information can not be updated",
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
    const onChangePicture = ()=>{

    }

    return(
        <div className="container emp-profile box-shadow-default">
            <AlertComponent alert={alert}/>
            {employeeInfo && employeeInfoAlter &&
                <div className="row">
                    <div className="col-md-4">
                        <UserImageChangeInput imageSrc={employeeInfoAlter.photo} disable={editable} 
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

                            <div className="row ">
                                <form action="">
                                    <div className="form-row row">
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="first-name-employee">First Name</label>
                                            <input type="text" className="form-control" id="first-name-employee" 
                                                placeholder="First Name" value={employeeInfoAlter.firstName} required
                                                onChange={onChangeEmployeeInfo} disabled={editable}
                                                name="firstName"/>
                                            <div className="valid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="last-name-employee">Last Name</label>
                                            <input type="text" className="form-control" id="last-name-employee" 
                                                placeholder="Last Name" required value={employeeInfoAlter.lastName} 
                                                onChange={onChangeEmployeeInfo} disabled={editable} name="lastName"/>
                                            <div className="invalid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                        <div className="col-md-3 mb-3">
                                            <label>Birth Date</label>
                                            <div>
                                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                    <DatePicker value={dayjs(employeeInfoAlter.birthDate)} 
                                                        onChange={onChangeEmployeeInfoBirthDate} 
                                                        className="form-control"
                                                        name="birthDate"
                                                        slotProps={{
                                                            textField: { size: 'small' },  // Set the size here
                                                          }}
                                                        disabled={editable}
                                                    />
                                                </LocalizationProvider>
                                            </div>
                                            <div className="invalid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                    </div>
                                    <div className="form-row row">
                                        <div className="col-md-6 mb-3">
                                            <label  htmlFor="address-employee">Address</label>
                                            <input type="text" className="form-control" id="address-employee" placeholder="Address" required
                                                value={employeeInfoAlter.address} onChange={onChangeEmployeeInfo} disabled={editable}
                                                name="address"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid address.
                                            </div>
                                        </div>
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="city-employee">City</label>
                                            <input type="text" className="form-control" id="city-employee" placeholder="City" required
                                                value={employeeInfoAlter.city} onChange={onChangeEmployeeInfo} disabled={editable}
                                                name="city"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid city.
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div className="form-row row">
                                        <div className="col-md-6 mb-3">
                                            <label  htmlFor="home-phone-employee">Home Phone</label>
                                            <input type="text" className="form-control" id="home-phone-employee" placeholder="Phone" required
                                                value={employeeInfoAlter.homePhone} onChange={onChangeEmployeeInfo} disabled={editable}
                                                name="homePhone"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid phone.
                                            </div>
                                        </div>

                                        <div className="col-md-3">
                                            <label>Hire Date</label>
                                            <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                <DatePicker value={dayjs(employeeInfoAlter.hireDate)} 
                                                    readOnly
                                                    className="form-control"
                                                    name="hireDate"
                                                    slotProps={{
                                                        textField: { size: 'small' },  // Set the size here
                                                    }}
                                                />
                                            </LocalizationProvider>
                                        </div>
                                    </div>

                                    <div className="form-row row">
                                        <div className="col-md-6 mb-3">
                                            <label  htmlFor="email-employee">Email</label>
                                            <input type="email" className="form-control" id="email-employee" placeholder="Email" required
                                                value={employeeInfoAlter.email} onChange={onChangeEmployeeInfo} disabled={editable}
                                                name="email"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid email.
                                            </div>
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col-md-5 mt-2">
                                            <button className="btn btn-primary" type="submit" onClick={onClickUpdate}
                                                disabled={editable}>
                                                Update{`\u00A0`}Infomation
                                            </button>
                                        </div>
                                        <ModalComponent modal={modal} handleCloseButton={onClickCloseModal}
                                            handleConfirmButton={onClickConfirmModal}/>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div>
                        
                    </div>
                </div>
            }
        </div>
    );
}