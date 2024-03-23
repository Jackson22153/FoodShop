interface Props{
    title: string,
    message: string,
    isShowed: boolean,
    handleCloseButton: any,
    handleConfirmButton: any
}
export default function ModalComponent(props: Props){
    const title = props.title;
    const message = props.message;
    const isShowed = props.isShowed;
    function handleCloseButton(){
        props.handleCloseButton();
    }
    function handleConfirmButton(){
        props.handleConfirmButton();
        handleCloseButton();
    }

    return(
        <div className={`modal ${isShowed?'d-block': 'fade'}`} id="confirm-modal" tabIndex={-1} role="dialog" 
            aria-labelledby="confirm-modal-label" aria-hidden="true">
            <div className="modal-dialog" role="document">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title" id="confirm-modal-label">{title}</h5>
                        <button type="button" onClick={handleCloseButton} className="close ml-auto" 
                            data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div className="modal-body">
                        <p>{message}</p>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-dismiss="modal"
                            onClick={handleCloseButton}>Close</button>
                        <button type="button" className="btn btn-primary"
                            onClick={handleConfirmButton}>Confirm</button>
                    </div>
                </div>
            </div>
        </div>
    );
}