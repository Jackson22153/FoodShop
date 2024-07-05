import { useEffect, useRef, useState } from "react";
import { CustomerDetail, CustomerUserInfo } from "../../../../../model/Type";
import { Alert, Modal } from "../../../../../model/WebType";
import { getCustomer, resetPassword } from "../../../../../api/AdminApi";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import ModalComponent from "../../../../shared/functions/modal/Modal";
import { ALERT_TIMEOUT, ALERT_TYPE } from "../../../../../constant/WebConstant";
import { useParams } from "react-router-dom";
import { displayUserImage } from "../../../../../service/Image";

export default function AdminCustomerComponent(){
    const { customerID } = useParams();
    const [selectedTab, setSelectedTab] = useState(0);
    const [customerUserInfo, setCustomerUserInfo] = useState<CustomerDetail>();
    const [resetPasswordModal, setResetPasswordModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: "",
        isShowed: false
    })
    const navHeaderRef = useRef(null);

    useEffect(()=>{
        if(customerID){
            fetUserCustomerInfo(customerID)
        }
    }, [])
    // get customer info
    const fetUserCustomerInfo = async (customerID: string)=>{
        const res = await getCustomer(customerID);
        if(res.status){
            const data = res.data;
            setCustomerUserInfo({   
                customerID: data.customerID,
                userID: data.userID,
                username: data.username,
                email: data.email,
                firstName: data.firstName,
                lastName: data.lastName,
                contactName: data.contactName || '',
                picture: data.picture || '',
                address: data.address,
                city: data.city,
                phone: data.phone
            })
        }
    }
    // reset password modal
    const onClickResetPassword = ()=>{
        setResetPasswordModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }
    const onClickCloseResetModal = ()=>{
        setResetPasswordModal({...resetPasswordModal, isShowed:false})
    }
    const onClickConfirmResetPassModal = async ()=>{
        try {
            const userID = customerUserInfo?.userID;
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

    const onClickSelectTab= (tab:number)=>{
        setSelectedTab(tab);
    }
    
    return(
        <div className="container emp-profile box-shadow-default mb-5">
            <AlertComponent alert={alert}/>
            {customerUserInfo &&
                <div className="row">
                    <div className="col-md-4">
                        <div className="profile-img">
                            <img src={displayUserImage(customerUserInfo.picture)} alt=""/>
                        </div>
                    </div>
                    <div className="col-md 6">
                        <div className="profile-head">
                            <h5>
                                Username: {customerUserInfo.username}
                            </h5>
                        </div>

                        <ul className="nav nav-tabs emp-profile p-0 mb-3 cursor-pointer" 
                            role="tablist" ref={navHeaderRef}>
                            
                            <li className="nav-item" role="presentation">
                                <span className={`nav-link text-dark ${selectedTab===0?'active':''}`}
                                    id="employee-info-tab" role="tab" aria-controls="employee-info-tab"
                                    onClick={(_e)=>onClickSelectTab(0)}>Information</span>
                            </li>

                            <li className="nav-item" role="presentation">
                                <span className={`nav-link text-dark ${selectedTab===1?'active':''}`}
                                    id="pending-order-tab" role="tab" 
                                    onClick={(_e)=>onClickSelectTab(1)}>Account</span>
                            </li>
                        </ul>


                        <div className="profile-about">
                            {selectedTab===0 ?
                                <div className={`row fade active show`}>
                                    <div className="row">
                                        <div className="col-md-7 mb-3">
                                            <label  htmlFor="customerID-customer">CustomerID</label>
                                            <input type="text" className="form-control" id="customerID-customer" 
                                                placeholder="customerID" value={customerUserInfo.customerID} 
                                                readOnly name="customerID"/>
                                            <div className="valid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                    </div>
                                    <div className="row"> 
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="contactName-customer">Contact Name</label>
                                            <input type="text" className="form-control" id="contactName-customer" 
                                                placeholder="contactName" value={customerUserInfo.contactName} required
                                                readOnly name="contactName"/>
                                            <div className="valid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="first-name-employee">First Name</label>
                                            <input type="text" className="form-control" id="first-name-employee" 
                                                placeholder="First Name" value={customerUserInfo.firstName} required
                                                readOnly name="firstName"/>
                                            <div className="valid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="last-name-employee">Last Name</label>
                                            <input type="text" className="form-control" id="last-name-employee" 
                                                placeholder="Last Name" required value={customerUserInfo.lastName} 
                                                readOnly name="lastName"/>
                                            <div className="invalid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col-md-6 mb-3">
                                            <label  htmlFor="address-customer">Address</label>
                                            <input type="text" className="form-control" id="address-customer" placeholder="Address" required
                                                value={customerUserInfo.address} readOnly
                                                name="address"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid address.
                                            </div>
                                        </div>
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="city-customer">City</label>
                                            <input type="text" className="form-control" id="city-customer" placeholder="City" required
                                                value={customerUserInfo.city} readOnly
                                                name="city"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid city.
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div className="row">
                                        <div className="col-md-6 mb-3">
                                            <label  htmlFor="phone-customer">Phone</label>
                                            <input type="text" className="form-control" id="phone-customer" placeholder="Phone" required
                                                value={customerUserInfo.phone} readOnly
                                                name="phone"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid phone.
                                            </div>
                                        </div>
                                    </div>
                                </div> :
                            selectedTab===1 &&
                                <div className={`row fade active show`}>
                                    <div className="row">
                                        <div className="col-md-7 mb-3">
                                            <label  htmlFor="userID-customer">UserID</label>
                                            <input type="text" className="form-control" id="userID-customer" 
                                                placeholder="UserID" value={customerUserInfo.userID} 
                                                readOnly name="userID"/>
                                            <div className="valid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="username-customer">Username</label>
                                            <input type="text" className="form-control" id="username-customer" 
                                                placeholder="Username" value={customerUserInfo.username} required
                                                readOnly name="username"/>
                                            <div className="valid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                        <div className="col-md-5 mb-3">
                                            <label  htmlFor="email-customer">Email</label>
                                            <input type="email" className="form-control" id="email-customer" placeholder="Email" required
                                                value={customerUserInfo.email}  readOnly name="email"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid email.
                                            </div>
                                        </div>
                                    </div>
                                    {/* <div className="row">
                                        <div className="col-md-5 mt-2">
                                            <button className="btn btn-primary" type="submit" onClick={onClickResetPassword}>
                                                Reset{`\u00A0`}Password
                                            </button>
                                        </div>
                                    </div> */}
                                </div>    
                            }
                        </div>
                    </div>
                </div>
            }
            <ModalComponent modal={resetPasswordModal} 
                handleCloseButton={onClickCloseResetModal} 
                handleConfirmButton={onClickConfirmResetPassModal}/>
        </div>
    );
}