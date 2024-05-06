import { ChangeEventHandler, useEffect, useState } from 'react';
import { CustomerAccount, EmployeeAccount } from '../../../../../model/Type';
import AlertComponent from '../../../../shared/functions/alert/Alert';
import ModalComponent from '../../../../shared/functions/modal/Modal';
import { Alert, Modal } from '../../../../../model/WebType';
import { ALERT_TIMEOUT, ALERT_TYPE } from '../../../../../constant/config';
import { FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import { addNewCustomer, addNewEmployee } from '../../../../../api/AdminApi';

export default function AdminAddUserComponent(){
    const CUSTOMER = "Customer";
    const EMPLOYEE = "Employee";
    const [customer, setCustomer] = useState<CustomerAccount>({
        userID: '',
        customerID: '',
        username: '',
        contactName: '',
        email: '',
        picture: ''
    });
    const [employee, setEmployee] = useState<EmployeeAccount>({
        userID: '',
        employeeID: '',
        username: '',
        firstName: '',
        lastName: '',
        email: '',
        photo: ''
    })
    const userRoles = [CUSTOMER, EMPLOYEE];
    const [selectedUserRole, setSelectedUserRole] = useState(CUSTOMER);
    const [customerModal, setCustomerModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })
    const [employeeModal, setEmployeeModal] = useState<Modal>({
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

    }, [])


    // create customer
    // reset password modal
    const onChangeCustomer: ChangeEventHandler<any> = (event)=>{
        const name = event.target.name;
        const value = event.target.value;
        setCustomer({...customer, [name]: value})
    }
    const onClickAddCustomer = ()=>{
        setCustomerModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }
    const onClickCloseCustomerModal = ()=>{
        setCustomerModal({...customerModal, isShowed:false})
    }
    const onClickConfirmCusotmerModal = async ()=>{
        try {
            console.log(customer)
            const res = await addNewCustomer(customer);
            if(res.status){
                const data = res.data
                const status = data.status
                setAlert({
                    message: status?`New customer ${customer.username} has been added successfully`:`New customer ${customer.username} can not be added`,
                    type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                    isShowed: true
                })   
            }
        } 
        catch (error) {
            setAlert({
                message: `New customer ${customer.username} can not be added`,
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

    // employee
    const onChangeEmployee: ChangeEventHandler<any> = (event)=>{
        const name = event.target.name;
        const value = event.target.value;
        setEmployee({...employee, [name]: value})
    } 
    const onClickAddEmployee = ()=>{
        setEmployeeModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }
    const onClickCloseEmployeeModal = ()=>{
        setEmployeeModal({...customerModal, isShowed:false})
    }
    const onClickConfirmEmployeeModal = async ()=>{
        try {
            const res = await addNewEmployee(employee);
            if(res.status){
                const data = res.data
                const status = data.status
                setAlert({
                    message: status?`New employee ${employee.username} has been added successfully`:`New employee ${employee.username} can not be added`,
                    type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                    isShowed: true
                })   
            }
        } 
        catch (error) {
            setAlert({
                message: `New employee ${employee.username} can not be added`,
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
    // role
    const onChangeRole = (event: any)=>{
        const value = event.target.value;
        setSelectedUserRole(value)
    }

    return(
        <div className="container">
            <div className="row justify-content-center">
                <div className="col-md-6 emp-profile box-shadow-default px-5 py-3">
                    <div className="mb-5 ">
                        <div className="row mb-3 ">
                            <div className="col-md-12">
                                <div className="profile-about">
                                    <div className="row">
                                        <div className="col-md-6">
                                            <FormControl sx={{ m: 1, minWidth: "100%" }} size="small">
                                                <InputLabel id="user-role-admin">User</InputLabel>
                                                <Select
                                                    labelId="user-role-admin"
                                                    id="select-user-role-admin"
                                                    value={selectedUserRole}
                                                    label="User"
                                                    name='userrole'
                                                    onChange={onChangeRole}
                                                    required
                                                >
                                                    {userRoles.map((role, index)=>(
                                                        <MenuItem key={index} value={role}>
                                                            {role}
                                                        </MenuItem>
                                                    ))}
                                                </Select>
                                            </FormControl>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <AlertComponent alert={alert}/>
                        {selectedUserRole===CUSTOMER ?
                            <div className="row">
                                <div className="col-md-12">
                                    <div className="profile-about">
                                        <div className="row">
                                            <div className="col-md-12 mb-3">
                                                <label  htmlFor="username-customer">Username</label>
                                                <input type="text" className="form-control" id="username-customer" 
                                                    placeholder="Username" value={customer.username} required
                                                    name="username" onChange={onChangeCustomer}/>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col-md-12 mb-3">
                                                <label  htmlFor="email-customer">Email</label>
                                                <input type="email" className="form-control" id="email-customer" placeholder="Email" required
                                                    value={customer.email}  name="email" onChange={onChangeCustomer}/>
                                            </div>
                                        </div>

                                        <div className="row">
                                            <div className="col-md-12 mb-3">
                                                <label  htmlFor="contactName-customer">Contact Name</label>
                                                <input type="text" className="form-control" id="contactName-customer" 
                                                    placeholder="Contact Name" value={customer.contactName} required
                                                    name="contactName" onChange={onChangeCustomer}/>
                                            </div>
                                        </div>
                                        

                                        <div className="row">
                                            <div className="col-md-5 mt-2">
                                                <button className="btn btn-primary" type="submit" onClick={onClickAddCustomer}>
                                                    Add{`\u00A0`}Customer
                                                </button>
                                            </div>
                                        </div>
                                        <ModalComponent modal={customerModal} 
                                            handleCloseButton={onClickCloseCustomerModal} 
                                            handleConfirmButton={onClickConfirmCusotmerModal}/>      
                                    </div>
                                </div>
                            </div>:
                        selectedUserRole===EMPLOYEE &&
                            <div className={`row`}>
                                <div className="col-md-12">
                                    <div className="profile-about">
                                        <div className="row">
                                            <div className="col-md-12 mb-3">
                                                <label  htmlFor="username-customer">Username</label>
                                                <input type="text" className="form-control" id="username-customer" 
                                                    placeholder="Username" value={employee.username} required
                                                    name="username" onChange={onChangeEmployee}/>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col-md-12 mb-3">
                                                <label  htmlFor="email-customer">Email</label>
                                                <input type="email" className="form-control" id="email-customer" placeholder="Email" required
                                                    value={employee.email}  name="email" onChange={onChangeEmployee}/>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col-md-6 mb-3">
                                                <label  htmlFor="first-name-employee">First Name</label>
                                                <input type="text" className="form-control" id="first-name-employee" 
                                                    placeholder="First Name" value={employee.firstName} required
                                                    name="firstName" onChange={onChangeEmployee}/>
                                            </div>
                                            <div className="col-md-6 mb-3">
                                                <label  htmlFor="last-name-employee">Last Name</label>
                                                <input type="text" className="form-control" id="last-name-employee" 
                                                    placeholder="Last Name"required value={employee.lastName} 
                                                    name="lastName" onChange={onChangeEmployee}/>
                                            </div>
                                        </div>

                                        <div className="row">
                                            <div className="col-md-5 mt-2">
                                                <button className="btn btn-primary" type="submit" onClick={onClickAddEmployee}>
                                                    
                                                    Add{`\u00A0`}Employee
                                                </button>
                                            </div>
                                            <ModalComponent modal={employeeModal} 
                                                handleCloseButton={onClickCloseEmployeeModal}
                                                handleConfirmButton={onClickConfirmEmployeeModal}/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        }

                    </div>
                </div>
            </div>
        </div>
    );
}