import { useEffect, useRef, useState } from "react";
import { CustomerUserInfo } from "../../../../../model/Type";
import { Alert, Modal } from "../../../../../model/WebType";
import { getCustomer, resetPassword } from "../../../../../api/AdminApi";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import ModalComponent from "../../../../shared/functions/modal/Modal";
import { ALERT_TIMEOUT, ALERT_TYPE } from "../../../../../constant/WebConstant";
import { useParams } from "react-router-dom";
import { displayUserImage } from "../../../../../service/Image";

export default function AdminCustomerComponent(){
    const { customerID } = useParams();
    const [customerUserInfo, setCustomerUserInfo] = useState<CustomerUserInfo>();
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
            // console.log(data)
            setCustomerUserInfo({   
                customerID: data.customerID,
                contactName: data.contactName || '',
                picture: data.picture || '',
                userInfo:{
                    user:{
                        userID: data.userInfo.user.userID,
                        username: data.userInfo.user.username,
                        email: data.userInfo.user.email,
                    },
                    roles: data.userInfo.roles || []
                }
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
            const userID = customerUserInfo?.userInfo.user.userID;
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
                                Username: {customerUserInfo.userInfo.user.username}
                            </h5>
                        </div>

                        <ul className="nav nav-tabs emp-profile p-0 mb-3 cursor-pointer" 
                            role="tablist" ref={navHeaderRef}>
                            <li className="nav-item" role="presentation">
                                <span className={`nav-link text-dark active`}
                                    id="pending-order-tab" role="tab">Account</span>
                            </li>
                        </ul>


                        <div className="profile-about">
                            <div className="row ">
                                <div className={`row fade active show`}>
                                    <div className="row">
                                        <div className="col-md-7 mb-3">
                                            <label  htmlFor="userID-customer">UserID</label>
                                            <input type="text" className="form-control" id="userID-customer" 
                                                placeholder="UserID" value={customerUserInfo.userInfo.user.userID} 
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
                                                placeholder="Username" value={customerUserInfo.userInfo.user.username} required
                                                readOnly name="username"/>
                                            <div className="valid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                        <div className="col-md-4 mb-3">
                                            <label  htmlFor="email-customer">Email</label>
                                            <input type="email" className="form-control" id="email-customer" placeholder="Email" required
                                                value={customerUserInfo.userInfo.user.email}  readOnly name="email"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid email.
                                            </div>
                                        </div>
                                    </div>

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
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="contactName-customer">Contact Name</label>
                                            <input type="text" className="form-control" id="contactName-customer" 
                                                placeholder="contactName" value={customerUserInfo.contactName} required
                                                readOnly name="contactName"/>
                                            <div className="valid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                    </div>
                                    

                                    <div className="row">
                                        <div className="col-md-5 mt-2">
                                            <button className="btn btn-primary" type="submit" onClick={onClickResetPassword}>
                                                Reset{`\u00A0`}Password
                                            </button>
                                        </div>
                                    </div>
                                    <ModalComponent modal={resetPasswordModal} 
                                        handleCloseButton={onClickCloseResetModal} 
                                        handleConfirmButton={onClickConfirmResetPassModal}/>
                                </div>
                            </div>       
                        </div>
                    </div>
                </div>
            }
        </div>
    );
}