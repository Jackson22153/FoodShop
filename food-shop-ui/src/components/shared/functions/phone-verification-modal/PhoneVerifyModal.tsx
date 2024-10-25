import { useState } from "react";
import { getMobileImage } from "../../../../service/Image";
import OtpInput from 'react-otp-input';
import { generateOTPPhoneForUser, verifyOTPPhoneForUser } from "../../../../api/UserApi";
import { Alert } from "../../../../model/WebType";
import { ALERT_TIMEOUT, ALERT_TYPE } from "../../../../constant/WebConstant";

interface Prop{
    phone: string,
    alert: Alert,
    setAlert: any,
    isShowed: boolean,
    onChangeIsShowed: any
    onChangeVerifingButton: any
}
export default function PhoneVerificationModal(prop:Prop){
    const mobile = getMobileImage();
    const [otpNumber, setOtpNumber] = useState("")
    const [isVerified, setIsVerified] = useState(false);


    function convertPhoneNumber(){
        const trimphone = prop.phone.substring(1);
        return `+84${trimphone}`;
    }

    function onChangeOTPNumber(value){
        setOtpNumber(value) 
    }

    function OnClickCloseModal(){
        prop.onChangeIsShowed();
    }

    async function onClickSendSMSWithCode(){
        // console.log(convertPhoneNumber())
        const res = await generateOTPPhoneForUser(convertPhoneNumber());
        if(200<=res.status && res.status<300){

        }
        setIsVerified(true)
    }

    async function onClickSendCode(){
        try {
            const res = await verifyOTPPhoneForUser(otpNumber, convertPhoneNumber());
            if(200<=res.status&&res.status<300){
                prop.onChangeVerifingButton();
                prop.onChangeIsShowed();
                const data = res.data
                const status = data.status
                prop.setAlert({
                    message: status?"Your phone number has been verified successfully":"Can not verify your phone number",
                    type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                    isShowed: true
                })   

            }
        } catch (error) {
            prop.setAlert({
                message: "Something is wrong",
                type: ALERT_TYPE.DANGER,
                isShowed: true
            })   
        }finally{
            setTimeout(()=>{
                prop.setAlert({...alert, isShowed: false});
            }, ALERT_TIMEOUT)
        }
    }


    if(!prop.isShowed) return
    else{
        return(
            <div className="bg-blur">
                <div className="modal d-block phone-verification-modal" tabIndex={-1} role="dialog">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-header justify-content-end">
                                <button type="button" className="close" data-dismiss="modal" aria-label="Close" onClick={OnClickCloseModal}>
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div className="modal-body d-flex flex-column align-items-center">
                                <img src={mobile} alt="" style={{width:"200px"}} />
                                {!isVerified ?
                                    <>
                                        <h5>Verify phone number</h5>
                                        <h5>{prop.phone}</h5>
                                    </>:
                                    <h5>Enter your code</h5>
                                }
                                {isVerified &&
                                    <OtpInput
                                        value={otpNumber}
                                        onChange={onChangeOTPNumber}
                                        numInputs={6}
                                        renderSeparator={<span>-</span>}
                                        renderInput={(props) => <input {...props} />}
                                    />
                                }
                                {!isVerified ?
                                    <button className="mt-2" onClick={onClickSendSMSWithCode}> Send SMS with code</button>:
                                    <button className="mt-2" disabled={otpNumber.length===6?false:true} onClick={onClickSendCode}> Send</button>
                                }
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}