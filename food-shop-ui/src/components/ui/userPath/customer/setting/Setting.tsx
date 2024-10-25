import { useContext, useEffect, useState } from "react"
import { Alert, Modal } from "../../../../../model/WebType";
import ModalComponent from "../../../../shared/functions/modal/Modal";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import { UpdateUserPassword } from "../../../../../api/UserApi";
import { ALERT_TYPE, ALERT_TIMEOUT } from "../../../../../constant/WebConstant";
import userInfoContext from "../../../../contexts/UserInfoContext";
import { UserInfo } from "../../../../../model/Type";

export default function SettingComponent(){
    const [passwordChange, setPasswordChange] = useState({
        password: "",
        newPassword: "",
        confirmNewPassword: ""
    })
    const [modal, setModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: ALERT_TYPE.INFO,
        isShowed: false
    })
    const userInfo = useContext<UserInfo>(userInfoContext)
    const [isShowed, setIsShowed] = useState(false)

    useEffect(()=>{

    }, [])

    const onClickCloseModal = ()=>{
        setModal({...modal, isShowed: false})
    }
    const onClickChangePassword = ()=>{
        setModal({...modal, isShowed: true})
    }
    const onClickConfirmModal = async ()=>{
        try {
            if(userInfo){
                const data = {
                    userID: userInfo.user.userID,
                    password: passwordChange.password,
                    newPassword: passwordChange.newPassword,
                    confirmNewPassword: passwordChange.confirmNewPassword,
                    email: userInfo.user.email
                }
                const res = await UpdateUserPassword(data);
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
        } 
        catch (error) {
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

    const onChangePassword = (e)=>{
        e.preventDefault();
        const name = e.target.name;
        const value = e.target.value;
        setPasswordChange({...passwordChange, [name]:value})
    }

    return(
        <div className="container d-flex justify-content-center">
            <AlertComponent alert={alert}/>
            <div className="col-xl-4">
                <div className="card mb-4">
                    <div className="card-header">Change Password</div>
                    <div className="card-body">
                        <form>
                            <div className="mb-3">
                                <label className="small mb-1" htmlFor="inputPassword">Password</label>
                                <input className="form-control" id="inputPassword" type="password" name="password" placeholder="Enter your password" value={passwordChange.password} onChange={onChangePassword}/>
                            </div>
                            <div className="mb-3">
                                <label className="small mb-1" htmlFor="inputNewPassword">New password</label>
                                <input className="form-control" id="inputNewPassword" name="newPassword" type="password" placeholder="Enter your new password" value={passwordChange.newPassword} onChange={onChangePassword}/>
                            </div>
                            <div className="mb-3">
                                <label className="small mb-1" htmlFor="inputConfirmPassword">Confirm your new password</label>
                                <input className="form-control" id="inputConfirmPassword" name="confirmNewPassword" type="password" placeholder="Confirm your new password" value={passwordChange.confirmNewPassword} onChange={onChangePassword}/>
                            </div>
                            
                            <button className="btn btn-primary me-2" type="button" onClick={onClickChangePassword}>Change password</button>
                        </form>
                    </div>
                </div>
            </div>
            <ModalComponent modal={modal} handleCloseButton={onClickCloseModal}
                handleConfirmButton={onClickConfirmModal}/>

            {/* {isShowed &&
                <div className="modal bg-blur d-block" tabIndex={-1} id="verification-email-modal" role="dialog">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-body">
                                <div className="w-100 d-flex justify-content-center">
                                    <header>
                                        <img className="w-100" src={getEmailSentIcon()} alt="" />
                                    </header>
                                </div>
                                <h4 className="text-align-center mt-4"><b>Verify your email</b></h4>
                                <p className="text-align-center mt-4">A confirmation email has been sent to your email at <b>{user.email}</b></p>
                                <p className="text-align-center">Please verify this email address</p> 
                            </div>
                        </div>
                    </div>
                </div>
            } */}
        </div>
    )
}