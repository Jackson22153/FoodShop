import { useEffect, useState } from "react"
import { Link, useSearchParams } from "react-router-dom"
import { REGISTER_AUTH } from "../../../../constant/FoodShoppingURL"
import { resetPassword, verifyResetToken } from "../../../../api/AuthorizationApi"
import { Modal } from "../../../../model/WebType"
import ModalComponent from "../../../shared/functions/modal/Modal"

export default function ResetPasswordComponent(){
    const [resetPasswordError, setResetPasswordError] = useState("")
    const [searchParams] = useSearchParams();
    const token = searchParams.get("token")
    const [isRender, setIsRender] = useState(false)
    const [isShowResultModal, setIsShowResultModal] = useState(false)
    const [modal, setModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to update your password?',
        isShowed: false
    })
    const [userInfo, setUserInfo] = useState({
        userID: "",
        username: "",
        email: ""
    })
    const [passwordChange, setPasswordChange] = useState({
        newPassword: "",
        confirmNewPassword: ""
    })

    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        verifyToken(token);
    }

    const verifyToken = async (token: string)=>{
        try {
            const res = await verifyResetToken(token);
            if(res.status){
                const data = res.data;
                setIsRender(true);
                setUserInfo({
                    userID: data.userID,
                    username: data.username,
                    email: data.email
                })
            }
        } catch (error) {
            setIsRender(false)
        }
    }
    // onchange password
    const onChangePassword = (e)=>{
        const name = e.target.name;
        const value = e.target.value;
        setPasswordChange(passwordInfo =>({...passwordInfo, [name]:value}))
    }
    // on click update password
    const onClickUpdatePassword = (e)=>{
        e.preventDefault();
        toggleModal();
    }

    // modal
    const toggleModal = ()=>{
        setModal(modal => ({...modal, isShowed:!modal.isShowed}))
    }
    const onClickCloseModal = ()=>{
        toggleModal()
    }
    const onClickConfirmModal = async ()=>{
        try {
            const data = {
                userID: userInfo.userID,
                username: userInfo.username,
                email: userInfo.email,
                newPassword: passwordChange.newPassword,
                confirmNewPassword: passwordChange.confirmNewPassword,
                token: token
            }
            const res = await resetPassword(data);
            if(res.status){
                const data = res.data;
                setIsShowResultModal(true);
                console.log(data)
            }

        } 
        catch (error) {
            setResetPasswordError(error.response.data.error)
        }
    }

    if(isRender){
        return(
            <>
                <div className="center" id="reset-password-page">
                    <h2>Reset password</h2>
                    {resetPasswordError && resetPasswordError.length>0 &&
                        <div className="invalid-feedback d-block text-align-center">
                            {resetPasswordError}
                        </div>
                    }
                    <form method="post" onSubmit={onClickUpdatePassword}>

                        <div className="txt_field">
                            <input type="password" name="newPassword" required value={passwordChange.newPassword} onChange={onChangePassword} />
                            <span></span>
                            <label htmlFor="newPassword">New password</label>
                        </div>
                        <div className="txt_field">
                            <input type="password" name="confirmNewPassword" required value={passwordChange.confirmNewPassword} onChange={onChangePassword} />
                            <span></span>
                            <label htmlFor="confirmNewPassword">Confirm new password</label>
                        </div>
                        {/* <div className="pass"><Link to={FORGET_PASSWORD_URL}>Forgot Password?</Link></div> */}
                        <input type="submit" value="Submit" />
                        <div className="signup_link">Not a member? <Link to={REGISTER_AUTH}>Signup</Link></div>
                    </form>
                </div>
                {isShowResultModal &&
                    <div className="modal bg-blur d-block box-shadow-default" tabIndex={-1} id="verification-email-modal" role="dialog">
                        <div className="modal-dialog" role="document">
                            <div className="modal-content">
                                <div className="modal-body">
                                    <div className="w-100 d-flex justify-content-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" className="text-success bi bi-check-circle" width="75" height="75"
                                            fill="currentColor" viewBox="0 0 16 16">
                                            <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z" />
                                            <path
                                                d="M10.97 4.97a.235.235 0 0 0-.02.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-1.071-1.05z" />
                                        </svg>
                                    </div>
                                    <h4 className="text-align-center mt-4"><b>Successfully</b></h4>
                                    <p className="text-align-center">Your password has been reset!</p>
                                    <p className="text-align-center">
                                        <Link to={"/"} className="btn btn-primary px-4">Home</Link>
                                    </p> 
                                    
                                </div>
                            </div>
                        </div>
                    </div>
                }
                <ModalComponent modal={modal} handleCloseButton={onClickCloseModal}
                    handleConfirmButton={onClickConfirmModal}/>
            </>
        )
    }
}