import { useState } from "react"
import { LOGIN_URL } from "../../../../constant/FoodShoppingURL";
import { Link } from "react-router-dom";
import { getEmailSentIcon, getLockIcon } from "../../../../service/Image";
import { forgotPassword } from "../../../../api/AuthorizationApi";

export default function ForgetPasswordComponent(){
    const [error, setError] = useState("")
    const [email, setEmail] = useState("")
    const [emailIsShowed, setEmailIsShowed] = useState(false)

    const onClickForgetPassword = async (e)=>{
        e.preventDefault();
        try {
            const res = await forgotPassword(email);
            if(res.status){
                setEmailIsShowed(show => !show)
            }
        } catch (error) {
            setError(error.response.data.error)
        }

    }

    const onChange = (e)=>{
        const value = e.target.value;
        setEmail(value);
    }
    
    return(
        <>
            <div className="center" id="forgotpassword-page">
                <div className="row mt-4">
                    <div className="col-md-12 d-flex justify-content-center">
                        <img style={{height:"130px"}} src={getLockIcon()} alt="" />
                    </div>
                    <h3 className="text-align-center">Forgot Password?</h3>
                    <div className="col-md-12">
                        <p className="text-align-center">You can reset your password here</p>
                    </div>
                    {error && error.length>0 &&
                        <div className="invalid-feedback d-block text-align-center">
                            {error}
                        </div>
                    }
                </div>
                <form method="post" action="/check" onSubmit={onClickForgetPassword}>
                    <div className="txt_field">
                        <input type="email" name="email" required value={email} onChange={onChange} />
                        <span></span>
                        <label htmlFor="email">Email</label>
                    </div>
                    <input type="submit" value="Send email" />
                    <div className="signup_link">Login here? <Link to={LOGIN_URL}>Signin</Link></div>
                </form>

            </div>
            {emailIsShowed &&
                <div className="modal bg-blur d-block" tabIndex={-1} id="verification-email-modal" role="dialog">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-body">
                                <div className="w-100 d-flex justify-content-center">
                                    <header>
                                        <img className="w-100" src={getEmailSentIcon()} alt="" />
                                    </header>
                                </div>
                                <h4 className="text-align-center mt-4"><b>Reset your password</b></h4>
                                <p className="text-align-center mt-4">The reset password link has been sent to <b>{email}</b></p>
                                <p className="text-align-center">Please access this link</p> 
                            </div>
                        </div>
                    </div>
                </div>
            }
        </>
    )
}