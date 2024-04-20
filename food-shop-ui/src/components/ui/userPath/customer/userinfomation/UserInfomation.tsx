import { ChangeEventHandler, FormEvent, useEffect, useState } from "react";
import { displayUserImage } from "../../../../../service/image";
import { Customer } from "../../../../../model/Type";
import { getCustomerInfo, updateUserInfo } from "../../../../../api/UserApi";

export default function UserInfomationComponent(){

    const [customerInfo, setCustomerInfo] = useState<Customer>({
        customerID: '',
        contactName: '',
        address: '',
        city: '',
        phone: '',
        picture: '',
        email: '',
        username: ''
    });

    const [editable, setEditable] = useState(true)

    useEffect(()=>{
        fetchCustomerInfo()

    }, [])

    const fetchCustomerInfo = async ()=>{
        const res = await getCustomerInfo();
        if(res){
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
            // console.log(customer)
            setCustomerInfo(customer)
        }
    }

    const onChangeCustomerInfo :ChangeEventHandler<HTMLInputElement> = (event)=>{
        const name = event.target.name;
        const value = event.target.value;
        // console.log(`name: ${name}, value:${value}`)
        setCustomerInfo({...customerInfo, [name]:value})
    }

    const onClickEditInfo = ()=>{
        setEditable((edit) => !edit)
    }

    const onClickUpdate = async (event: FormEvent)=>{
        event.preventDefault();
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
        if(res.status===200){
            alert(res.data);
            window.location.reload();
        }
    }

    return(
        <div className="container emp-profile box-shadow-default">
            {customerInfo &&
                <>
                    <div className="row">
                        <div className="col-md-4">
                            <div className="profile-img">
                                <img src={displayUserImage(customerInfo.picture)} alt=""/>
                                <div className="file btn btn-lg btn-primary">
                                    Change Photo
                                    <input type="file" name="file"/>
                                </div>
                            </div>
                        </div>
                        <div className="col-md 6">
                            <div className="profile-head">
                                <h5>
                                    Username: {customerInfo.username}
                                </h5>
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
                                    <form action="">
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
                                                    Update{`\u00A0`}Infomation
                                                </button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                
                            </div>
                        </div>
                        <div>
                            
                        </div>
                    </div>
                </>
            }
        </div>
    );
}