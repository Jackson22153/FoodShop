import { getEmailSentIcon } from "../../../../service/Image";
type Prop = {
    email: string
}
export default function EmailModal(prop: Prop){
    const emailicon = getEmailSentIcon();
    return(
        <div className="modal bg-blur d-block" tabIndex={-1} id="verification-email-modal" role="dialog">
            <div className="modal-dialog" role="document">
                <div className="modal-content">
                    <div className="modal-body">
                        <div className="w-100 d-flex justify-content-center">
                            <header>
                                <img className="w-100" src={emailicon} alt="" />
                            </header>
                        </div>
                        <h4 className="text-align-center mt-4"><b>Verify your email</b></h4>
                        <p className="text-align-center mt-4">You have entered <b>{prop.email}</b> as the email address for your account</p>
                        <p className="text-align-center">Please verify this email address</p> 
                    </div>
                </div>
            </div>
        </div>
    )
}