import { useState } from "react";
import { UsernamePasswordAuthentication } from "../../../../model/Type";
import { FORGET_PASSWORD_URL, REGISTER_AUTH } from "../../../../constant/FoodShoppingURL";
import { login } from "../../../../api/AuthorizationApi";
import { Link } from "react-router-dom";

export default function LoginComponent(){
    const [user, setUser] = useState<UsernamePasswordAuthentication>({
        username:"",
        password: ""
    })
    const [error, setError] = useState("")

    const onChange = (e)=>{
        const name = e.target.name;
        const value = e.target.value;
        setUser({
            ...user,
            [name]:value
        })
    }

    const onClickLogin = async (e)=>{
        e.preventDefault();
        try {
            const res = await login(user.username, user.password);
            if(res.status){
                window.location.href = "/"
            }
        } catch (error) {
            setError(error.response.data.message);
        }
    }

    return(
        <div className="center" id="login-page">
            <h1>Login</h1>
            {error && error.length>0 &&
                <div className="invalid-feedback d-block text-align-center">
                    {error}
                </div>
            }
            <form method="post" action="/check" onSubmit={onClickLogin}>
                <div className="txt_field">
                    <input type="text" name="username" required value={user.username} onChange={onChange} />
                    <span></span>
                    <label htmlFor="username">Username</label>
                </div>
                <div className="txt_field">
                    <input type="password" name="password" required value={user.password} onChange={onChange} />
                    <span></span>
                    <label htmlFor="password">Password</label>
                </div>
                <div className="pass"><Link to={FORGET_PASSWORD_URL}>Forgot Password?</Link></div>
                <input type="submit" value="Login" />
                <div className="signup_link">Not a member? <Link to={REGISTER_AUTH}>Signup</Link></div>
            </form>
        </div>
    );
}