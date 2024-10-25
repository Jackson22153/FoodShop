import { useEffect, useState } from "react";
import { Alert } from "../../../../../model/WebType";
import { RegisterInfo } from "../../../../../model/Type";
import { ALERT_TIMEOUT, ALERT_TYPE } from "../../../../../constant/WebConstant";
import { registerEmployee } from "../../../../../api/AdminApi";
import AlertComponent from "../../../../shared/functions/alert/Alert";

export default function AdminAddEmployeeComponent(){
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: "",
        isShowed: false
    })
    const [registerInfo, setRegisterInfo] = useState<RegisterInfo>({
        firstname: "",
        lastname: "",
        username: "",
        password: "",
        confirmPassword: "",
        email: ""
    })
    const [error, setError] = useState("")

    useEffect(()=>{

    }, [])

    const onClickRegister = async (e)=>{
        e.preventDefault();
        try {
            const res = await registerEmployee(registerInfo);
            if(200<=res.status&& res.status<300){
                const data = res.data
                const status = data.status
                setAlert({
                    message: status?"Employee has been created successfully":
                                    "Employee can not be created",
                    type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                    isShowed: true
                })   
            }
        } catch (error) {
            setError(error.response.data.error)
            setAlert({
                message: error.response.data.error,
                type: ALERT_TYPE.DANGER,
                isShowed: true
            }) 
        } finally{
            setTimeout(()=>{
                setAlert({...alert, isShowed: false});
            }, ALERT_TIMEOUT)
        }
    }

    const onChangeRegisterInfo = (e)=>{
        const name = e.target.name;
        const value = e.target.value;
        setRegisterInfo({...registerInfo, [name]:value})
    }

    return(
        <div className="container">
            <AlertComponent alert={alert}/>
            <div className="center">
                <h1>Register</h1>
                {error.length>0 &&
                    <div className="invalid-feedback d-block text-align-center">
                        {error}
                    </div>
                }
                <form method="POST" action="" onSubmit={onClickRegister}>
                    <div className="txt_field">
                        <input type="text" name="firstname" required onChange={onChangeRegisterInfo} value={registerInfo.firstname}/>
                        <span></span>
                        <label htmlFor="firstname">First name</label>
                    </div>
                    <div className="txt_field">
                        <input type="text" name="lastname" required onChange={onChangeRegisterInfo} value={registerInfo.lastname}/>
                        <span></span>
                        <label htmlFor="lastname">Last name</label>
                    </div>
                    <div className="txt_field">
                        <input type="email" name="email" required onChange={onChangeRegisterInfo} value={registerInfo.email}/>
                        <span></span>
                        <label htmlFor="email">Email</label>
                    </div>
                    <div className="txt_field">
                        <input type="text" name="username" required onChange={onChangeRegisterInfo} value={registerInfo.username}/>
                        <span></span>
                        <label htmlFor="username">Username</label>
                    </div>
                    <div className="txt_field">
                        <input type="password" name="password" required onChange={onChangeRegisterInfo} value={registerInfo.password}/>
                        <span></span>
                        <label htmlFor="password">Password</label>
                    </div>
                    <div className="txt_field">
                        <input type="password" name="confirmPassword" required onChange={onChangeRegisterInfo} value={registerInfo.confirmPassword}/>
                        <span></span>
                        <label htmlFor="confirmPassword">Confirm Password</label>
                    </div>
                    <input type="submit" className="mb-3" value="Sign Up" />
                </form>
            </div>
        </div>
    )
}