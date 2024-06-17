import { ChangeEventHandler, FormEvent, useEffect, useState } from "react";
import { Customer } from "../../../../../model/Type";
import { getCustomerInfo, updateUserInfo } from "../../../../../api/UserApi";
import { Alert, Modal } from "../../../../../model/WebType";
import { ALERT_TYPE, ALERT_TIMEOUT } from "../../../../../constant/WebConstant";
import ModalComponent from "../../../../shared/functions/modal/Modal";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import { UserImageChangeInput } from "../../../../shared/functions/user-image-change/UserImageChangeInput";

export default function UserInformationComponent(){

    const [customerInfo, setCustomerInfo] = useState<Customer>();
    const [modal, setModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })
    const [editable, setEditable] = useState(true)
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: "",
        isShowed: false
    })

    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        fetchCustomerInfo()
    }
    // get customerinfo
    const fetchCustomerInfo = async ()=>{
        const res = await getCustomerInfo();
        if(200<=res.status&&res.status<300){
            const data = res.data;
            const customer = {
                customerID: data.customerID,
                contactName: data.contactName || '',
                address: data.address || '',
                city: data.city || '',
                phone: data.phone || '',
                picture: data.picture || '',
                email: data.email || '',
                username: data.username || ''
            };
            setCustomerInfo(customer)
        }
    }
    // change customer's info
    const onChangeCustomerInfo :ChangeEventHandler<HTMLInputElement> = (event)=>{
        if(customerInfo){
            const name = event.target.name;
            const value = event.target.value;
            setCustomerInfo({...customerInfo, [name]:value})
        }
    }
    // change customer's picture
    const onChangePicture = (imageSrc: string)=>{
        setCustomerInfo({...customerInfo, ['picture']:imageSrc})
    }
    // enable edit information
    const onClickEditInfo = ()=>{
        setEditable((edit) => !edit)
    }
    // update customer's information
    const onClickUpdate = async (event: FormEvent)=>{
        event.preventDefault();
        toggleModal();
    }
    // confirm modal
    const toggleModal = ()=>{
        setModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }

    const onClickCloseModal = ()=>{
        toggleModal()
    }
    const onClickConfirmModal = async ()=>{
        try {
            if(customerInfo){
                const customer = {
                    customerID: customerInfo.customerID,
                    contactName: customerInfo.contactName || null,
                    address: customerInfo.address || null,
                    city: customerInfo.city || null,
                    phone: customerInfo.phone || null,
                    picture: customerInfo.picture || null,
                    email: customerInfo.email || null,
                    username: customerInfo.username || null
                };
                const res = await updateUserInfo(customer);
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?"Information has been updated successfully":"Information can not be updated",
                        type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                        isShowed: true
                    })   
                }
            }
        } 
        catch (error) {
            setAlert({
                message: "Information can not be updated",
                type: ALERT_TYPE.DANGER,
                isShowed: true
            }) 
        }
        finally{
            setTimeout(()=>{
                setAlert({...alert, isShowed: false});
            }, ALERT_TIMEOUT)
        }
    }

    return(
        <div className="container emp-profile box-shadow-default">
            <AlertComponent alert={alert}/>
            {customerInfo &&
                <div className="row">
                    <div className="col-md-4">
                        <UserImageChangeInput imageSrc={customerInfo.picture} disable={editable}
                            onChangePicture={onChangePicture} />
                    </div>
                    <div className="col-md 6">
                        <div className="profile-head">
                            <h5>Username: {customerInfo.username}</h5>
                        </div>
                        <div className="profile-about">
                            <div className="row my-3 justify-content-end">
                                <div className="col-md-3 col-sm-4">
                                    <button className="profile-edit-btn btn btn-secondary w-100 rounded-2 p-1" onClick={onClickEditInfo}>
                                        Edit{`\u00A0`}Profile
                                    </button>
                                </div>
                            </div>

                            <div className="row ">
                                <form action="post">
                                    <div className="form-row row">
                                        <div className="col-md-4 mb-3">
                                            <label  htmlFor="contact-name-customer">Contact Name</label>
                                            <input type="text" className="form-control" id="contact-name-customer" 
                                                placeholder="Contact Name" value={customerInfo.contactName} required
                                                onChange={onChangeCustomerInfo} disabled={editable}
                                                name="contactName"/>
                                            <div className="valid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                        <div className="col-md-6 mb-3">
                                            <label  htmlFor="email-customer">Email</label>
                                            <input type="email" className="form-control" id="email-customer" 
                                                placeholder="Email" required 
                                                value={customerInfo.email} onChange={onChangeCustomerInfo} disabled={editable}
                                                name="email"/>
                                            <div className="invalid-feedback">
                                                Please choose a email.
                                            </div>
                                        </div>
                                    </div>
                                    <div className="form-row row">
                                        <div className="col-md-6 mb-3">
                                            <label  htmlFor="address-customer">Address</label>
                                            <input type="text" className="form-control" id="address-customer" placeholder="Address" required
                                                value={customerInfo.address} onChange={onChangeCustomerInfo} disabled={editable}
                                                name="address"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid address.
                                            </div>
                                        </div>
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="city-customer">City</label>
                                            <input type="text" className="form-control" id="city-customer" placeholder="City" required
                                                value={customerInfo.city} onChange={onChangeCustomerInfo} disabled={editable}
                                                name="city"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid city.
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div className="form-row row">
                                        <div className="col-md-6 mb-3">
                                            <label  htmlFor="phone-customer">Phone</label>
                                            <input type="text" className="form-control" id="phone-customer" placeholder="Phone" required
                                                value={customerInfo.phone} onChange={onChangeCustomerInfo} disabled={editable}
                                                name="phone"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid phone.
                                            </div>
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col-md-5 mt-2">
                                            <button className="btn btn-primary" type="submit" onClick={onClickUpdate}
                                                disabled={editable}>
                                                Update{`\u00A0`}Information
                                            </button>
                                            <ModalComponent modal={modal} handleCloseButton={onClickCloseModal}
                                                handleConfirmButton={onClickConfirmModal}/>
                                        </div>
                                    </div>
                                </form>
                            </div>       
                        </div>
                    </div>
                </div>
            }
        </div>
    );
}