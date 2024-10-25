import { userInfo } from "os"
import { useState } from "react"
import { UpdateUserPassword } from "../../../../api/UserApi"
import { ALERT_TYPE, ALERT_TIMEOUT } from "../../../../constant/WebConstant"
import { Alert, Modal } from "../../../../model/WebType"
import ModalComponent from "../modal/Modal"

type Prop = {
    userID: string,
    email: string
}
export default function ChangePasswordComponent(prop: Prop){
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: "",
        isShowed: false
    })
    const [changePasswordmodal, setChangePasswordModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to change your password?',
        isShowed: false
    })
    const [passwordChange, setPasswordChange] = useState({
        password: "",
        newPassword: "",
        confirmNewPassword: ""
    })
    // update password
    const onClickCloseUpdatePasswordModal = ()=>{
        setChangePasswordModal({...changePasswordmodal, isShowed: false})
    }
    const onClickChangePassword = ()=>{
        setChangePasswordModal({...changePasswordmodal, isShowed: true})
    }
    const onClickConfirmUpdatePasswordModal = async ()=>{
        try {
            if(userInfo){
                const data = {
                    userID: prop.userID,
                    password: passwordChange.password,
                    newPassword: passwordChange.newPassword,
                    confirmNewPassword: passwordChange.confirmNewPassword,
                    email: prop.email
                }
                const res = await UpdateUserPassword(data);
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?"Information has been updated successfully":
                                        "Information can not be updated",
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
    // onchange
    const onChangePassword = (e)=>{
        e.preventDefault();
        const name = e.target.name;
        const value = e.target.value;
        setPasswordChange({...passwordChange, [name]:value})
    }


    return(
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
            <ModalComponent modal={changePasswordmodal} handleCloseButton={onClickCloseUpdatePasswordModal}
                handleConfirmButton={onClickConfirmUpdatePasswordModal}/>
        </div>
    )
}