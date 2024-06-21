import { Modal } from "../../../../model/WebType";
import { getError } from "../../../../service/Image"

interface Props{
    modal: Modal
}
export default function ErrorModal(prop: Props){
    if(prop.modal.isShowed){
        return(
            <div className="screen-center vh-100 d-flex justify-content-center align-items-center">
                <div className="card col-md-4 bg-white shadow-md p-5">
                    <div className="mb-4 text-center">
                        <img width={"200px"} src={getError()} alt="Error" />
                    </div>
                    <div className="text-center">
                        <h2>{prop.modal.title}</h2>
                        <p>{prop.modal.message}</p>
                        <a href="/" className="btn btn-outline-success">Back Home</a>
                    </div>
                </div>
            </div>
        )
    }else return null;

}