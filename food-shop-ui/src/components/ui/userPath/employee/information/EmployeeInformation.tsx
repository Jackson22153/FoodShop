import { ChangeEventHandler, FormEvent, useContext, useEffect, useRef, useState } from "react";
import { District, EmployeeDetail, Province, UserInfo, Ward } from "../../../../../model/Type";
import { getEmployeeInfo, updateEmployeeInfo } from "../../../../../api/EmployeeApi";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import dayjs, { Dayjs } from "dayjs";
import { Alert, Modal, ModalContextType } from "../../../../../model/WebType";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import ModalComponent from "../../../../shared/functions/modal/Modal";
import { ALERT_TYPE, ALERT_TIMEOUT } from "../../../../../constant/WebConstant";
import modalContext from "../../../../contexts/ModalContext";
import userInfoContext from "../../../../contexts/UserInfoContext";
import { displayUserImage } from "../../../../../service/Image";
import { UpdateUserPassword, uploadEmployeeImage } from "../../../../../api/UserApi";
import { Link, useSearchParams } from "react-router-dom";
import { EMPLOYEE_INFO } from "../../../../../constant/FoodShoppingURL";
import { getProvinces, getDistricts, getWards } from "../../../../../api/ShippingApi";

export default function EmployeeInformationComponent(){
    const [employeeInfo, setEmployeeInfo] = useState<EmployeeDetail>();
    const userInfo = useContext<UserInfo>(userInfoContext)
    const {setModal: setErrorModal} = useContext<ModalContextType>(modalContext);
    const [editable, setEditable] = useState(true)
    const [provinces, setProvinces] = useState<Province[]>([]);
    const [districts, setDistricts] = useState<District[]>([]);
    const [wards, setWards] = useState<Ward[]>([]);
    const [passwordChange, setPasswordChange] = useState({
        password: "",
        newPassword: "",
        confirmNewPassword: ""
    })
    const [alert, setAlert] = useState<Alert>({
        message: "",
        type: "",
        isShowed: false
    })
    const [searchParam] = useSearchParams()
    const tag = searchParam.get('tag')
    const [profileModal, setProfileModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to update your profile?',
        isShowed: false
    })
    const [changePasswordmodal, setChangePasswordModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to change your password?',
        isShowed: false
    })
    const imageinputRef = useRef<HTMLInputElement>(null)

    useEffect(()=>{
        initial();
    }, [userInfo])

    const initial = ()=>{
        fetchEmployeeInfo();
    }
    // get employee's information
    const fetchEmployeeInfo = async ()=>{
        try {       
            const res = await getEmployeeInfo();
            if(200<=res.status&&res.status<300){
                const data = res.data;
                setEmployeeInfo(data);
                fetchProvinces(data)
            }
        } catch (error) {
            setErrorModal({
                title: "Error",
                isShowed: true,
                message: error.response?error.response.data.error:error.message
            })
        }
    }

    const onChangeEmployeeInfoBirthDate = (birthDate:Dayjs|null)=>{
        if(birthDate && employeeInfo){
            const birthDateStr = birthDate.format("YYYY-MM-DD");
            setEmployeeInfo({...employeeInfo, birthDate: birthDateStr})
        }
    }
    const onChangeEmployeeInfo :ChangeEventHandler<HTMLInputElement> = (event)=>{
        if(employeeInfo){
            const name = event.target.name;
            const value = event.target.value;
            setEmployeeInfo({...employeeInfo, [name]:value})
        }
    }

    const onClickEditInfo = ()=>{
        setEditable((edit) => !edit)
    }

    const onClickUpdate = async (event: FormEvent)=>{
        event.preventDefault();
        toggleProfileModal()
    }

    const toggleProfileModal = ()=>{
        setProfileModal(modal =>({...modal, isShowed:!modal.isShowed}))
    }

    const onClickCloseProfileModal = ()=>{
        toggleProfileModal()
    }
    const onClickConfirmProfileModal = async ()=>{
        try {
            if(!employeeInfo.address){
                setAlert({
                    message: "Your address is missing",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }else if(!employeeInfo.city){
                setAlert({
                    message: "Your city is missing",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }else if(!employeeInfo.district){
                setAlert({
                    message: "Your district is missing",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }else if(!employeeInfo.ward){
                setAlert({
                    message: "Your ward is missing",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }else if(!employeeInfo.phone){
                setAlert({
                    message: "Your phone is missing",
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                })
            }else{
                if(employeeInfo){
                    const employee = {
                        employeeID: employeeInfo.employeeID,
                        birthDate: employeeInfo.birthDate || null,
                        phone: employeeInfo.phone || null,
                        address: employeeInfo.address || null,
                        city: employeeInfo.city || null,
                        district: employeeInfo.district || null,
                        ward: employeeInfo.ward || null,
                        picture: employeeInfo.picture || null,
                        userID: employeeInfo.userID
                    };
                    const res = await updateEmployeeInfo(employee);
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
        } catch (error) {
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
    const onChangePicture = (imageSrc: string)=>{
        setEmployeeInfo({...employeeInfo, ['picture']:imageSrc})
    }

    // upload user image
    const onChange: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const file = event.target.files[0];
        uploadImage(file);
    }
    const uploadImage = async (file: File)=>{
        const res = await uploadEmployeeImage(file);
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

    // update password
    const onClickCloseUpdatePasswordModal = ()=>{
        setChangePasswordModal({...changePasswordmodal, isShowed: false})
    }
    const onClickChangePassword = ()=>{
        setChangePasswordModal({...changePasswordmodal, isShowed: true})
    }
    const onClickConfirmUpdatePasswordModal = async ()=>{
        try {
            if(userInfo){
                const data = {
                    userID: userInfo.user.userID,
                    password: passwordChange.password,
                    newPassword: passwordChange.newPassword,
                    confirmNewPassword: passwordChange.confirmNewPassword,
                    email: userInfo.user.email
                }
                const res = await UpdateUserPassword(data);
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

    const onChangePassword = (e)=>{
        e.preventDefault();
        const name = e.target.name;
        const value = e.target.value;
        setPasswordChange({...passwordChange, [name]:value})
    }

    // get address
    const fetchProvinces = async (employeeInfo: EmployeeDetail)=>{
        try {
            const res = await getProvinces();
            if(res.status){
                const data = res.data as Province[];
                setProvinces(data)
                const provinceID = data.find(province => province.ProvinceName===employeeInfo.city).ProvinceID;
                fetchDistricts(provinceID, employeeInfo);
            }
        } catch (error) {

        }
    }

    const fetchDistricts = async (provinceID: string, employeeInfo: EmployeeDetail)=>{
        try {
            const res = await getDistricts(provinceID);
            if(res.status){
                const data = res.data as District[];
                setDistricts(data);
                const districtID = data.find(district => district.DistrictName===employeeInfo.district).DistrictID;
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

    // get address id
    const getCityID = (cityName: string)=>{
        return provinces.find(province => province.ProvinceName === cityName)?.ProvinceID;
    }
    const getDistricID = (districtName: string)=>{
        return districts.find(district => district.DistrictName === districtName)?.DistrictID;
    }

    // on change select
    const onChangeSelectProvince = (e)=>{
        const value = e.target.value; 
        setEmployeeInfo({...employeeInfo, city: value, district: null, ward: null})
        const cityID = getCityID(value);
        fetchDistricts(cityID, employeeInfo)
    }

    const onChangeSelectDistrict = (e)=>{
        const name = e.target.name;
        const value = e.target.value;
        setEmployeeInfo({...employeeInfo, [name]:value, ward: null})
        fetchWards(getDistricID(value))
    }

    const onChangeSelectWard = (e)=>{
        const name = e.target.name;
        const value = e.target.value; 
        setEmployeeInfo({...employeeInfo, [name]:value})
    }

    return(
        <div className="container emp-profile box-shadow-default">
            <AlertComponent alert={alert}/>
            {employeeInfo && employeeInfo &&
            <div className="container-xl px-4">
                <nav className="nav nav-borders">
                    <Link className={`nav-link ms-0 ${(tag===null || tag==="profile")?"active":""}`} to={`${EMPLOYEE_INFO}?tag=profile`}>Profile</Link>
                </nav>
                <hr className="mt-0 mb-4"/>

                <div className="row">
                    <div className="col-xl-4">
                        <div className="card mb-4 mb-xl-0">
                            <div className="card-header">Profile Picture</div>
                            <div className="card-body text-center">
                                <img className="img-account-profile rounded-circle mb-2" src={displayUserImage(employeeInfo.picture)} alt=""/>
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
                                        <input className="form-control" id="inputUsername" type="text" name="username" placeholder="Enter your username" value={userInfo.user.username} onChange={()=>{}}/>
                                    </div>
                                    <div className="row gx-3 mb-3">
                                        <div className="col-md-6">
                                            <label className="small mb-1" htmlFor="inputFirstName">First name</label>
                                            <input className="form-control" id="inputFirstName" name="firstName" type="text" placeholder="Enter your first name" value={employeeInfo.firstName} onChange={()=>{}}/>
                                        </div>
                                        <div className="col-md-6">
                                            <label className="small mb-1" htmlFor="inputLastName">Last name</label>
                                            <input className="form-control" id="inputLastName" name="lastName" type="text" placeholder="Enter your last name" value={employeeInfo.lastName} onChange={()=>{}}/>
                                        </div>
                                    </div>
                                    <div className="row gx-3 mb-3">
                                        <div className="col-md-6">
                                            <label className="small mb-1" htmlFor="inputAddress">Address</label>
                                            {(!employeeInfo.address || !employeeInfo.address.length )&&
                                            <span className="badge bg-danger ms-1">!</span> 
                                            }
                                            <input className="form-control" id="inputAddress" name="address" type="text" placeholder="Enter your Address" value={employeeInfo.address} onChange={onChangeEmployeeInfo} disabled={editable}/>
                                        </div>
                                        <div className="col-md-6">
                                            <label className="small mb-1" htmlFor="inputCity">City / Province</label>
                                            { (!employeeInfo.city || !employeeInfo.city.length) &&
                                            <span className="badge bg-danger ms-1">!</span> 
                                            }
                                            {(editable)?
                                                <input className="form-control" id="inputCity" name="city" type="text" placeholder="Enter your city" value={employeeInfo.city || ""} onChange={()=>{}} disabled={editable}/>:
                                                <select className="form-select" name="city" value={employeeInfo.city || ""} onChange={onChangeSelectProvince}>
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
                                            {(!employeeInfo.district || !employeeInfo.district.length) &&
                                            <span className="badge bg-danger ms-1">!</span> 
                                            }
                                            {editable ?
                                                <input className="form-control" id="inputDistrict" name="district" type="text" placeholder="Enter your district" value={employeeInfo.district || ""} onChange={()=>{}} disabled={editable}/>:
                                                <select className="form-select" name="district" onChange={onChangeSelectDistrict} value={employeeInfo.district || ""}>
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
                                            {(!employeeInfo.ward || !employeeInfo.ward.length) &&
                                            <span className="badge bg-danger ms-1">!</span> 
                                            }
                                            {editable ?
                                                <input className="form-control" id="inputWard" name="ward" type="text" placeholder="Enter your ward" value={employeeInfo.ward || ""} onChange={()=>{}} disabled={editable}/>:
                                                <select className="form-select" name="ward" onChange={onChangeSelectWard} value={employeeInfo.ward || ""}>
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
                                            {(!employeeInfo.phone || !employeeInfo.phone.length) &&
                                            <span className="badge bg-danger ms-1">!</span> 
                                            }
                                            <input className="form-control" id="inputPhone" name="phone" type="tel" placeholder="Enter your phone number" value={employeeInfo.phone} onChange={onChangeEmployeeInfo} disabled={editable}/>
                                        </div>
                                        <div className="col-md-6">
                                            <label className="small mb-1" htmlFor="inputContactName">Brith date</label>
                                            <div>
                                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                    <DatePicker value={dayjs(employeeInfo.birthDate)} 
                                                        onChange={onChangeEmployeeInfoBirthDate} 
                                                        className="form-control"
                                                        name="birthDate"
                                                        slotProps={{
                                                            textField: { size: 'small' },  // Set the size here
                                                        }}
                                                        disabled={editable}
                                                    />
                                                </LocalizationProvider>
                                            </div>
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
                <ModalComponent modal={profileModal} handleCloseButton={onClickCloseProfileModal}
                    handleConfirmButton={onClickConfirmProfileModal}/>
                <ModalComponent modal={changePasswordmodal} handleCloseButton={onClickCloseUpdatePasswordModal}
                    handleConfirmButton={onClickConfirmUpdatePasswordModal}/>
            </div>
            }
        </div>
    );
}