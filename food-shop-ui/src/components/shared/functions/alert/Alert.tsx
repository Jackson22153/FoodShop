
interface Props{
    message: string,
    type: string,
}
export default function AlertComponent(props: Props){
    const message = props.message;
    const type = props.type;

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

    return(
        <div className={`alert  ${messageType(type)} alert-position`} role="alert">
            <p className="h5">{transform(message)}</p>
            {/* <a className="close">&times;</a> */}
        </div>
    );
}