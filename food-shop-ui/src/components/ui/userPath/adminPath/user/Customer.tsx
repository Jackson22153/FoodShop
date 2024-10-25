import { useEffect, useRef, useState } from "react";
import { CustomerAdminDetail, District, Province, Ward } from "../../../../../model/Type";
import { getCustomer, resetUserPassword, updateCustomer } from "../../../../../api/AdminApi";
import { useParams } from "react-router-dom";
import { displayUserImage } from "../../../../../service/Image";
import ModalComponent from "../../../../shared/functions/modal/Modal";
import { Alert, Modal } from "../../../../../model/WebType";
import { ALERT_TYPE, ALERT_TIMEOUT } from "../../../../../constant/WebConstant";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import { getProvinces, getDistricts, getWards } from "../../../../../api/ShippingApi";

export default function AdminCustomerComponent(){
    const { userID } = useParams();
    const [customerInfo, setCustomerInfo] = useState<CustomerAdminDetail>();
    const [verificationInfo, setVerificationInfo] = useState();
    const [editable, setEditable] = useState(false)

    const [provinces, setProvinces] = useState<Province[]>([]);
    const [districts, setDistricts] = useState<District[]>([]);
    const [wards, setWards] = useState<Ward[]>([]);

    const imageinputRef = useRef<HTMLInputElement>(null)
    const [modal, setModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    })
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: "",
        isShowed: false
    })
    const [updateInfoModal, setUpdateInfoModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    });


    useEffect(()=>{
        if(userID){
            fetUserCustomerInfo(userID)
        }
    }, [])
    // get customer info
    const fetUserCustomerInfo = async (userID: string)=>{
        const res = await getCustomer(userID);
        if(res.status){
            const data = res.data;
            setVerificationInfo(verificationInfo);
            setCustomerInfo(data);
            fetchProvinces(data);
        }
    }

    const onClickResetpassword = (e)=>{
        e.preventDefault();
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
            const res = await resetUserPassword(userID);
            if(res.status){
                const data = res.data;
                const status = data.status;
                setAlert({
                    message: status?"User's password has been sent to email":"User's password can not be reset",
                    type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                    isShowed: true
                })   
            }
        } 
        catch (error) {
            setAlert({
                message: "Something wrong happened!",
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

    // togle edit
    const onClickEditInfo = (event)=>{
        event.preventDefault();
        setEditable(editable => !editable);
    }


    // on change status
    const onChangeStatus = (event: any)=>{
        const name = event.target.name;
        const value = event.target.value;
        setCustomerInfo({...customerInfo, [name]:value})
    }

    // update customer profile
    // update info modal
    const toggleUpdateInfoModal = ()=>{
        setUpdateInfoModal(modal => ({...modal, isShowed:!modal.isShowed}))
    }
    const onClickUpdate = (event)=>{
        event.preventDefault();
        toggleUpdateInfoModal();
    }
    const onClickCloseUpdateInfoModal = ()=>{
        toggleUpdateInfoModal();
    }
    const onClickConfirmUpdateInfoModal = async ()=>{
        try {
            if(customerInfo){
                const res = await updateCustomer(customerInfo)
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?"Customer's information has been updated successfully":
                                        "Customer's information can not be updated",
                        type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                        isShowed: true
                    })   
                }
            }
        } catch (error) {
            setAlert({
                message: "Customer's information can not be updated",
                type: ALERT_TYPE.DANGER,
                isShowed: true
            }) 
        } finally{
            setTimeout(()=>{
                setAlert({...alert, isShowed: false});
            }, ALERT_TIMEOUT)
        }
    }

    const onChangeEmployeeInfo = (event)=>{
        if(customerInfo){
            const name = event.target.name;
            const value = event.target.value;
            setCustomerInfo({...customerInfo, [name]:value})
        }
    }

    // get address
    const fetchProvinces = async (customerInfo: CustomerAdminDetail)=>{
        try {
            const res = await getProvinces();
            if(res.status){
                const data = res.data as Province[];
                setProvinces(data)
                const provinceID = data.find(province => province.ProvinceName===customerInfo.city).ProvinceID;
                fetchDistricts(provinceID, customerInfo);

            }
        } catch (error) {

        }
    }

    const fetchDistricts = async (provinceID: string, customerInfo: CustomerAdminDetail)=>{
        try {
            const res = await getDistricts(provinceID);
            if(res.status){
                const data = res.data as District[];
                setDistricts(data);
                const districtID = data.find(district => district.DistrictName===customerInfo.district).DistrictID;
                fetchWards(districtID);
            }
        } catch (error) {

        }
    }
    const fetchWards = async (districtID: string)=>{
        try {
            const res = await getWards(districtID);
            if(res.status){
                const data = res.data;
                setWards(data);
            }
        } catch (error) {

        }
    }

    // on change select
    const onChangeSelectProvince = (e)=>{
        const value = e.target.value; 
        setCustomerInfo({...customerInfo, city: value, district: null, ward: null})
        const cityID = getCityID(value);
        fetchDistricts(cityID, customerInfo)
    }

    const onChangeSelectDistrict = (e)=>{
        const name = e.target.name;
        const value = e.target.value;
        setCustomerInfo({...customerInfo, [name]:value, ward: null})
        fetchWards(getDistricID(value))
    }

    const onChangeSelectWard = (e)=>{
        const name = e.target.name;
        const value = e.target.value; 
        setCustomerInfo({...customerInfo, [name]:value})
    }

    // get address id
    const getCityID = (cityName: string)=>{
        return provinces.find(province => province.ProvinceName === cityName)?.ProvinceID;
    }
    const getDistricID = (districtName: string)=>{
        return districts.find(district => district.DistrictName === districtName)?.DistrictID;
    }

    
    
    return(
        <div className="container emp-profile box-shadow-default mb-5">
            {customerInfo &&
                <div className="container-xl px-4 mt-4">
                    <AlertComponent alert={alert}/>
                    <div className="row">
                        <div className="col-xl-4">
                            <div className="card mb-4 mb-xl-0">
                                <div className="card-header">Profile Picture</div>
                                <div className="card-body text-center">
                                    <img className="img-account-profile rounded-circle mb-2" src={displayUserImage(customerInfo.picture)} alt=""/>
                                </div>
                            </div>
                        </div>
                        <div className="col-xl-8">
                            <div className="card mb-4">
                                <div className="card-header">Account Details</div>
                                <div className="card-body">
                                    <form>
                                        <div className="mb-3">
                                            <label className="small mb-1" htmlFor="inputUsername">Username (how your name will appear to other users on the site)</label>
                                            <input className="form-control" id="inputUsername" type="text" name="username" placeholder="Enter your username" value={customerInfo.username || ""} onChange={()=>{}}/>
                                        </div>
                                        <div className="row gx-3 mb-3">
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputFirstName">First name</label>
                                                <input className="form-control" id="inputFirstName" name="firstName" type="text" placeholder="Enter your first name" value={customerInfo.firstName || ""} onChange={()=>{}}/>
                                            </div>
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputLastName">Last name</label>
                                                <input className="form-control" id="inputLastName" name="lastName" type="text" placeholder="Enter your last name" value={customerInfo.lastName || ""} onChange={()=>{}}/>
                                            </div>
                                        </div>
                                        <div className="row gx-3 mb-3">
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputAddress">Address</label>
                                                {(!customerInfo.address || customerInfo.address.length==0) &&
                                                <span className="badge bg-danger ms-1">!</span> 
                                                }
                                                <input className="form-control" id="inputAddress" name="address" type="text" placeholder="Enter your Address" value={customerInfo.address || ""} disabled={!editable} onChange={onChangeEmployeeInfo}/>
                                            </div>
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputCity">City / Province</label>
                                                { (!customerInfo.city || !customerInfo.city.length) &&
                                                <span className="badge bg-danger ms-1">!</span> 
                                                }
                                                {(!editable)?
                                                    <input className="form-control" id="inputCity" name="city" type="text" placeholder="Enter your city" value={customerInfo.city || ""} onChange={()=>{}} disabled={!editable}/>:
                                                    <select className="form-select" name="city" value={customerInfo.city || ""} onChange={onChangeSelectProvince}>
                                                        <option>Select your city/province</option>
                                                        {provinces.map((province, index)=>(
                                                            <option key={index} value={`${province.ProvinceName}`}>
                                                                {province.ProvinceName}
                                                            </option>
                                                        ))}
                                                    </select>
                                                }
                                            </div>
                                        </div>

                                        <div className="row gx-3 mb-3">
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputDistrict">District</label>
                                                {(!customerInfo.district || !customerInfo.district.length) &&
                                                <span className="badge bg-danger ms-1">!</span> 
                                                }
                                                {!editable ?
                                                    <input className="form-control" id="inputDistrict" name="district" type="text" placeholder="Enter your district" value={customerInfo.district || ""} onChange={()=>{}} disabled={!editable}/>:
                                                    <select className="form-select" name="district" onChange={onChangeSelectDistrict} value={customerInfo.district || ""}>
                                                        {!districts.length ?
                                                            <option>Select your province/city first</option>:
                                                            <>
                                                                <option>Select your district</option>
                                                                {districts.map((district, index)=>(
                                                                    <option key={index} value={`${district.DistrictName}`}>
                                                                        {district.DistrictName}
                                                                    </option>
                                                                ))}
                                                            </>
                                                        }
                                                    </select>
                                                }
                                            </div>
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputWard">Ward</label>
                                                {(!customerInfo.ward || !customerInfo.ward.length) &&
                                                <span className="badge bg-danger ms-1">!</span> 
                                                }
                                                {!editable ?
                                                    <input className="form-control" id="inputWard" name="ward" type="text" placeholder="Enter your ward" value={customerInfo.ward || ""} onChange={()=>{}} disabled={!editable}/>:
                                                    <select className="form-select" name="ward" onChange={onChangeSelectWard} value={customerInfo.ward || ""}>
                                                        {!wards.length ?
                                                            <option>Select your district first</option>:
                                                            <>
                                                                <option>Select your ward</option>
                                                                {wards.map((ward, index)=>(
                                                                    <option key={index} value={`${ward.WardName}`}>
                                                                        {ward.WardName}
                                                                    </option>
                                                                ))}
                                                            </>
                                                        }
                                                    </select>
                                                }
                                            </div>
                                        </div>

                                        <div className="mb-3">
                                            <label className="small mb-1" htmlFor="inputEmailAddress">Email address</label>
                                            <input className="form-control" id="inputEmailAddress" type="email" placeholder="Enter your email address" value={customerInfo.email || ""} disabled={!editable} onChange={onChangeEmployeeInfo}/>
                                        </div>
                                        <div className="row gx-3 mb-3">
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputPhone">Phone number</label>
                                                {(!customerInfo.phone || customerInfo.phone.length)==0 &&
                                                <span className="badge bg-danger ms-1">!</span> 
                                                }
                                                <input className="form-control" id="inputPhone" name="phone" type="tel" placeholder="Enter your phone number" value={customerInfo.phone || ""} disabled={!editable} onChange={onChangeEmployeeInfo}/>
                                            </div>
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputContactName">Contact name</label>
                                                {(!customerInfo.contactName || customerInfo.contactName.length==0) &&
                                                <span className="badge bg-danger ms-1">!</span> 
                                                }
                                                <input className="form-control" id="inputContactName" type="text" name="contactName" placeholder="Enter your contact name" value={customerInfo.contactName || ""} disabled={!editable} onChange={onChangeEmployeeInfo}/>
                                            </div>
                                        </div>
                                        <div className="mb-3">
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputNotes">Account status</label>
                                                <select className="form-select" value={`${customerInfo.enabled}`} disabled={!editable} name="enabled" onChange={onChangeStatus}>
                                                    <option value={"true"}>Active</option>
                                                    <option value={"false"}>Deactive</option>
                                                </select>
                                            </div>
                                        </div>
                                        <button className="btn btn-primary me-2" type="button" onClick={onClickResetpassword}>Reset password</button>
                                        <button className="btn btn-primary me-2" type="button" onClick={onClickEditInfo}>{!editable?"Change profile": "Cancel change"}</button>
                                        {editable &&
                                            <button className="btn btn-primary" type="button" onClick={onClickUpdate}>Save changes</button>
                                        }
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <ModalComponent modal={modal} handleCloseButton={onClickCloseModal}
                        handleConfirmButton={onClickConfirmModal}/>
                    <ModalComponent modal={updateInfoModal} handleCloseButton={onClickCloseUpdateInfoModal}
                        handleConfirmButton={onClickConfirmUpdateInfoModal}/>
                </div>
            }
        </div>
    );
}