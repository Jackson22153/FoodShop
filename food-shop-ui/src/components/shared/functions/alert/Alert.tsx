import { Alert } from "../../../../model/WebType";

interface Props{
    alert: Alert
}
export default function AlertComponent(prop: Props){
    const alert = prop.alert;
    const message = alert.message;
    const type = alert.type;
    const isShowed = alert.isShowed;

    function transform(message: string){
        return message.charAt(0).toUpperCase() + message.slice(1);
    }

    function messageType(type: string){
        switch(type){
            case 'primary': {
                return 'alert-primary';
            }
            case 'info': {
                return 'alert-info';
            }
            case 'success': {
                return 'alert-success';
            }case 'danger': {
                return 'alert-danger';
            }
            default:{
                return 'alert-primary';
            }
        }
    }
    if(isShowed){
        return(
            <div className={`alert  ${messageType(type)} alert-position`} role="alert">
                <p className="h5">{transform(message)}</p>
                {/* <a className="close">&times;</a> */}
            </div>
        );
    }
    else return null;
}