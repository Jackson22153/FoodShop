import { ChangeEventHandler, FormEvent, useContext, useEffect, useRef, useState } from "react";
import { CustomerDetail, District, Province, UserInfo, VerificationInfo, Ward } from "../../../../../model/Type";
import { 
    getCustomerInfo, sendCustomerEmailVerification, 
    updateUserInfo, UpdateUserPassword, uploadUserImage 
} from "../../../../../api/UserApi";
import { Alert, Modal, ModalContextType } from "../../../../../model/WebType";
import { ALERT_TYPE, ALERT_TIMEOUT } from "../../../../../constant/WebConstant";
import ModalComponent from "../../../../shared/functions/modal/Modal";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import modalContext from "../../../../contexts/ModalContext";
import userInfoContext from "../../../../contexts/UserInfoContext";
import { displayUserImage } from "../../../../../service/Image";
import { Link, useSearchParams } from "react-router-dom";
import { CUSTOMER_INFO } from "../../../../../constant/FoodShoppingURL";
import { getProvinces, getDistricts, getWards } from "../../../../../api/ShippingApi";
import CreditCard from "../../../../shared/functions/creditcard/CreditCard";
import EmailModal from "../../../../shared/functions/emailmodal/EmailModal";

export default function UserInformationComponent(){
    const {setModal: setErrorModal} = useContext<ModalContextType>(modalContext)
    const [customerInfo, setCustomerInfo] = useState<CustomerDetail>();
    const userInfo = useContext<UserInfo>(userInfoContext);
    const [verificationInfo, setVerificationInfo] = useState<VerificationInfo>();
    const imageinputRef = useRef<HTMLInputElement>(null)
    const [searchParam] = useSearchParams()
    const tag = searchParam.get('tag')
    
    const [provinces, setProvinces] = useState<Province[]>([]);
    const [districts, setDistricts] = useState<District[]>([]);
    const [wards, setWards] = useState<Ward[]>([]);

    const [isShowedEmail, setIsShowedEmail] = useState(false)
    const [isShowedPhoneVerification, setIsShowedPhoneVerification] = useState(false)

    const [profileModal, setProfileModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to update your profile?',
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
    }, [userInfo, tag])

    const initial = ()=>{
        fetchCustomerInfo()
    }

    // get customerinfo
    const fetchCustomerInfo = async ()=>{
        try {            
            const res = await getCustomerInfo();
            if(200<=res.status&&res.status<300){
                const data = res.data;
                const customerDetails = data;
                setCustomerInfo(customerDetails)

                // const verification = {
                //     phoneVerified: data.verificationInfo.phoneVerified,
                //     profileVerified: data.verificationInfo.profileVerified,
                //     emailVerified: data.verificationInfo.emailVerified
                // }
                // setVerificationInfo(verification)
                fetchProvinces(customerDetails)
            }
        } catch (error) {
            setErrorModal({
                title: "Error", 
                isShowed: true, 
                message: error.response?error.response.data.error:error.message
            })
        }
    }
    // get address id
    const getCityID = (cityName: string)=>{
        return provinces.find(province => province.ProvinceName === cityName)?.ProvinceID;
    }
    const getDistricID = (districtName: string)=>{
        return districts.find(district => district.DistrictName === districtName)?.DistrictID;
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

    // change isShowed phone verification modal
    const onChangeIsShowedPhoneVerification = ()=>{
        setIsShowedPhoneVerification(value => !value)
    }
    const onChangeVerifyingPhoneButton = ()=>{
        setVerificationInfo({...verificationInfo, phoneVerified:true})
    }
    // enable edit information
    const onClickEditInfo = ()=>{
        setEditable((edit) => !edit)

    }
    // profile modal
    // update customer's information
    const onClickUpdate = async (event: FormEvent)=>{
        event.preventDefault();
        toggleProfileModal();
    }
    const toggleProfileModal = ()=>{
        setProfileModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }
    const onClickCloseProfileModal = ()=>{
        toggleProfileModal()
    }
    const onClickConfirmProfileModal = async ()=>{
        try {
            if(!customerInfo.address || !customerInfo.address.length){
                setAlert({
                    message: "Your address is missing",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }
            else if(!customerInfo.city || !customerInfo.city.length){
                setAlert({
                    message: "Your city is not selected",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }else if(!customerInfo.district || !customerInfo.district.length){
                setAlert({
                    message: "Your district is not selected",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }else if(!customerInfo.ward || !customerInfo.ward.length){
                setAlert({
                    message: "Your ward is not selected",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }
            else if(!customerInfo.contactName || !customerInfo.contactName.length){
                setAlert({
                    message: "Your contact name is missing",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                })
            }else if(!customerInfo.phone || !customerInfo.phone.length){
                setAlert({
                    message: "Your phone is missing",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                })
            }else{
                if(customerInfo){
                    const customer = {
                        customerID: customerInfo.customerID,
                        contactName: customerInfo.contactName || null,
                        address: customerInfo.address || null,
                        city: customerInfo.city || null,
                        district: customerInfo.district || null,
                        ward: customerInfo.ward || null,
                        phone: customerInfo.phone || null,
                        picture: customerInfo.picture || null,
                        userID: customerInfo.userID
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

    // upload user image
    const onChange: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const file = event.target.files[0];
        uploadImage(file);
    }
    const uploadImage = async (file: File)=>{
        const res = await uploadUserImage(file);
        if(res.status){
            const data = res.data;
            onChangePicture(data.imageUrl);
        }
    }
    const onClickUploadImage = ()=>{
        if(imageinputRef.current){
            imageinputRef.current.click();
        }
        
    }
    

    // get address
    const fetchProvinces = async (customerInfo: CustomerDetail)=>{
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

    const fetchDistricts = async (provinceID: string, customerInfo: CustomerDetail)=>{
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

    const onClickVerifyEmail = (e)=>{
        e.preventDefault();
        setIsShowedEmail(isShowed => !isShowed)
        sendEmailVerification();
    }
    // send email verification
    const sendEmailVerification = async ()=>{
        try {
            const res = await sendCustomerEmailVerification();
            if(res.status){
                const data = res.data;
            }
        } catch (error) {
            
        }
    }

    return(
        <div className="container emp-profile box-shadow-default">
            <AlertComponent alert={alert}/>
            {customerInfo &&
                <div className="container-xl px-4">
                    <nav className="nav nav-borders">
                        <Link className={`nav-link ms-0 ${(tag===null || tag==="profile")?"active":""}`} to={`${CUSTOMER_INFO}?tag=profile`}>Profile</Link>
                        {/* <Link className={`nav-link ${(tag==="security")?"active":""}`} to={`${CUSTOMER_INFO}?tag=security`}>Security</Link> */}
                        <Link className={`nav-link ${(tag==="credit")?"active":""}`} to={`${CUSTOMER_INFO}?tag=credit`}>Credit Card</Link>
                    </nav>
                    <hr className="mt-0 mb-4"/>

                    {tag==="credit"?
                        <CreditCard/>:
                        <div className="row">
                            <div className="col-xl-4">
                                <div className="card mb-4 mb-xl-0">
                                    <div className="card-header">Profile Picture</div>
                                    <div className="card-body text-center">
                                        <img className="img-account-profile rounded-circle mb-2" src={displayUserImage(customerInfo.picture)} alt=""/>
                                        <div className="small font-italic text-muted mb-4">JPG or PNG no larger than 5 MB</div>
                                        <button className="btn btn-primary" type="button" disabled={editable} onClick={onClickUploadImage}>Upload new image</button>
                                        <input type="file" name="picture" id="customer-image-input" hidden={true} disabled={editable} className="w-100 h-100" ref={imageinputRef}
                                            accept="image/x-png,image/gif,image/jpeg" onChange={onChange}/>
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
                                                <input className="form-control" id="inputUsername" type="text" name="username" placeholder="Enter your username" value={userInfo.user.username || ""} onChange={()=>{}}/>
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
                                                    {(!customerInfo.address || !customerInfo.address.length) &&
                                                    <span className="badge bg-danger ms-1">!</span> 
                                                    }
                                                    <input className="form-control" id="inputAddress" name="address" type="text" placeholder="Enter your Address" value={customerInfo.address || ""} onChange={onChangeCustomerInfo} disabled={editable}/>
                                                </div>
                                                <div className="col-md-6">
                                                    <label className="small mb-1" htmlFor="inputCity">City / Province</label>
                                                    { (!customerInfo.city || !customerInfo.city.length) &&
                                                    <span className="badge bg-danger ms-1">!</span> 
                                                    }
                                                    {(editable)?
                                                        <input className="form-control" id="inputCity" name="city" type="text" placeholder="Enter your city" value={customerInfo.city || ""} onChange={()=>{}} disabled={editable}/>:
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
                                                    {editable ?
                                                        <input className="form-control" id="inputDistrict" name="district" type="text" placeholder="Enter your district" value={customerInfo.district || ""} onChange={()=>{}} disabled={editable}/>:
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
                                                    {editable ?
                                                        <input className="form-control" id="inputWard" name="ward" type="text" placeholder="Enter your ward" value={customerInfo.ward || ""} onChange={()=>{}} disabled={editable}/>:
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
                                                <input className="form-control" id="inputEmailAddress" type="email" placeholder="Enter your email address" value={userInfo.user.email} onChange={()=>{}}/>
                                            </div>
                                            <div className="row gx-3 mb-3">
                                                <div className="col-md-6">
                                                    <label className="small mb-1" htmlFor="inputPhone">Phone number</label>
                                                    {((!customerInfo.phone || !customerInfo.phone.length)) &&
                                                    <span className="badge bg-danger ms-1">!</span> 
                                                    }
                                                    <input className="form-control" id="inputPhone" name="phone" type="tel" placeholder="Enter your phone number" value={customerInfo.phone} onChange={onChangeCustomerInfo} disabled={editable}/>
                                                </div>
                                                <div className="col-md-6">
                                                    <label className="small mb-1" htmlFor="inputContactName">Contact name</label>
                                                    {!customerInfo.contactName || !customerInfo.contactName.length&&
                                                    <span className="badge bg-danger ms-1">!</span> 
                                                    }
                                                    <input className="form-control" id="inputContactName" type="text" name="contactName" placeholder="Enter your contact name" value={customerInfo.contactName} onChange={onChangeCustomerInfo} disabled={editable}/>
                                                </div>
                                            </div>
                                            <button className="btn btn-primary me-2" type="button" onClick={onClickEditInfo}>{editable?"Change profile": "Cancel change"}</button>
                                            {!editable &&
                                                <button className="btn btn-primary" type="button" onClick={onClickUpdate}>Save changes</button>
                                            }
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    }
                    <ModalComponent modal={profileModal} handleCloseButton={onClickCloseProfileModal}
                        handleConfirmButton={onClickConfirmProfileModal}/>
                    {isShowedEmail &&
                    <EmailModal email={customerInfo.email}/>
                    }
                </div>
            }
        </div>
    );
}