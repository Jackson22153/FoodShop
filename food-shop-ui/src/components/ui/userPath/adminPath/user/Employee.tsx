import { ChangeEventHandler, MouseEventHandler, useEffect, useRef, useState } from "react";
import { District, EmployeeAdminDetails, Province, Ward } from "../../../../../model/Type";
import { getEmployee, resetUserPassword, updateEmployee } from "../../../../../api/AdminApi";
import { useParams } from "react-router-dom";
import { PickerChangeHandlerContext, DateValidationError } from "@mui/x-date-pickers/models";
import ModalComponent from "../../../../shared/functions/modal/Modal";
import { Alert, Modal } from "../../../../../model/WebType";
import AlertComponent from "../../../../shared/functions/alert/Alert";
import { ALERT_TIMEOUT, ALERT_TYPE } from "../../../../../constant/WebConstant";
import { displayUserImage } from "../../../../../service/Image";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import dayjs from "dayjs";
import { uploadEmployeeImage } from "../../../../../api/UserApi";
import { getProvinces, getDistricts, getWards } from "../../../../../api/ShippingApi";

export default function AdminEmployeeComponent(){
    const {userID} = useParams();
    const [employeeInfo, setEmployeeInfo] = useState<EmployeeAdminDetails>();
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
    const [provinces, setProvinces] = useState<Province[]>([]);
    const [districts, setDistricts] = useState<District[]>([]);
    const [wards, setWards] = useState<Ward[]>([]);
    const imageinputRef = useRef<HTMLInputElement>(null)
    const [editable, setEditable] = useState(false);
    const [updateInfoModal, setUpdateInfoModal] = useState<Modal>({
        title: 'Confirm action',
        message: 'Do you want to continute?',
        isShowed: false
    });

    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        if(userID){
            fetchEmployee(userID);
        }
    }

    const fetchEmployee = async (userID: string)=>{
        const res = await getEmployee(userID);
        if(res.status){
            const data = res.data;
            setEmployeeInfo(data);
            fetchProvinces(data)
        }
    }

    const onClickEditInfo:MouseEventHandler<HTMLButtonElement> = (event)=>{
        event.preventDefault()
        setEditable(editable => !editable);
    }

    const onChangeEmployeeInfo: ChangeEventHandler<any> = (event)=>{
        if(employeeInfo){
            const name = event.target.name;
            const value = event.target.value;
            setEmployeeInfo({...employeeInfo, [name]:value})
        }
    }
    const onChangeEmployeeInfoHireDate = (value: any, _context: PickerChangeHandlerContext<DateValidationError>)=>{
        if(employeeInfo){
            const date = value.format('YYYY-MM-DD');
            setEmployeeInfo({...employeeInfo, hireDate: date})
        }
    }

    // change employee picture
    const onChangePicture = (imageSrc: string)=>{
        setEmployeeInfo({...employeeInfo, ['picture']:imageSrc})
    }

    // update info modal
    const toggleUpdateInfoModal = ()=>{
        setUpdateInfoModal(modal => ({...modal, isShowed:!modal.isShowed}))
    }
    const onClickUpdate:MouseEventHandler<HTMLButtonElement> = (event)=>{
        event.preventDefault();
        toggleUpdateInfoModal();
    }
    const onClickCloseUpdateInfoModal = ()=>{
        toggleUpdateInfoModal();
    }
    const onClickConfirmUpdateInfoModal = async ()=>{
        try {
            if(employeeInfo){
                const res = await updateEmployee(employeeInfo);
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?"Employee's information has been updated successfully":
                                        "Employee's information can not be updated",
                        type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                        isShowed: true
                    })   
                }
            }
        } catch (error) {
            setAlert({
                message: "Employee's information can not be updated",
                type: ALERT_TYPE.DANGER,
                isShowed: true
            }) 
        } finally{
            setTimeout(()=>{
                setAlert({...alert, isShowed: false});
            }, ALERT_TIMEOUT)
        }
    }

    // reset password
    const onClickResetpassword = (e)=>{
        e.preventDefault();
        toggleModal();
    }

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
    // on change status
    const onChangeStatus = (event: any)=>{
        const name = event.target.name;
        const value = event.target.value;
        setEmployeeInfo({...employeeInfo, [name]:value})
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

    // get address
    const fetchProvinces = async (employeeInfo: EmployeeAdminDetails)=>{
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

    const fetchDistricts = async (provinceID: string, employeeInfo: EmployeeAdminDetails)=>{
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
        <div className="container">
            <AlertComponent alert={alert}/>
            {employeeInfo &&
                <div className="container-xl px-4 mt-4">
                    <AlertComponent alert={alert}/>
                    <div className="row">
                        <div className="col-xl-4">
                            <div className="card mb-4 mb-xl-0">
                                <div className="card-header">Profile Picture</div>
                                <div className="card-body text-center">
                                    <img className="img-account-profile rounded-circle mb-2" src={displayUserImage(employeeInfo.picture)} alt=""/>
                                    <div className="small font-italic text-muted mb-4">JPG or PNG no larger than 5 MB</div>
                                    <button className="btn btn-primary" type="button" disabled={!editable} onClick={onClickUploadImage}>Upload new image</button>
                                    <input type="file" name="picture" id="customer-image-input" hidden={true} disabled={!editable} className="w-100 h-100" ref={imageinputRef}
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
                                            <input className="form-control" id="inputUsername" type="text" name="username" placeholder="Enter your username" value={employeeInfo.username || ""} onChange={()=>{}}/>
                                        </div>
                                        <div className="row gx-3 mb-3">
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputFirstName">First name</label>
                                                <input className="form-control" id="inputFirstName" name="firstName" type="text" placeholder="Enter your first name" value={employeeInfo.firstName || ""} onChange={()=>{}}/>
                                            </div>
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputLastName">Last name</label>
                                                <input className="form-control" id="inputLastName" name="lastName" type="text" placeholder="Enter your last name" value={employeeInfo.lastName || ""} onChange={()=>{}}/>
                                            </div>
                                        </div>
                                        <div className="row gx-3 mb-3">
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputAddress">Address</label>
                                                {(!employeeInfo.address || employeeInfo.address.length==0) &&
                                                <span className="badge bg-danger ms-1">!</span> 
                                                }
                                                <input className="form-control" id="inputAddress" name="address" disabled={!editable} type="text" placeholder="Enter your Address" value={employeeInfo.address || ""} onChange={onChangeEmployeeInfo}/>
                                            </div>
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputCity">City / Province</label>
                                                { (!employeeInfo.city || !employeeInfo.city.length) &&
                                                <span className="badge bg-danger ms-1">!</span> 
                                                }
                                                {(!editable)?
                                                    <input className="form-control" id="inputCity" name="city" type="text" placeholder="Enter your city" value={employeeInfo.city || ""} onChange={()=>{}} disabled={!editable}/>:
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
                                                {!editable ?
                                                    <input className="form-control" id="inputDistrict" name="district" type="text" placeholder="Enter your district" value={employeeInfo.district || ""} onChange={()=>{}} disabled={!editable}/>:
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
                                                {!editable ?
                                                    <input className="form-control" id="inputWard" name="ward" type="text" placeholder="Enter your ward" value={employeeInfo.ward || ""} onChange={()=>{}} disabled={!editable}/>:
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

                                        <div className="row gx-3 mb-3">
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputEmailAddress">Email address</label>
                                                <input className="form-control" id="inputEmailAddress" type="email" placeholder="Enter your email address" value={employeeInfo.email || ""} onChange={onChangeEmployeeInfo}/>
                                            </div>
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputContactName">Hire date</label>
                                                <div>
                                                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                        <DatePicker value={dayjs(employeeInfo.hireDate)} 
                                                            onChange={onChangeEmployeeInfoHireDate} 
                                                            className="form-control"
                                                            name="birthDate"
                                                            slotProps={{
                                                                textField: { size: 'small' },  // Set the size here
                                                            }}
                                                            disabled={!editable}
                                                        />
                                                    </LocalizationProvider>
                                                </div>
                                            </div>
                                        </div>


                                        <div className="row gx-3 mb-3">
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputPhone">Phone number</label>
                                                {(!employeeInfo.phone || employeeInfo.phone.length==0) &&
                                                <span className="badge bg-danger ms-1">!</span> 
                                                }
                                                <input className="form-control" id="inputPhone" disabled={!editable} name="phone" type="tel" placeholder="Enter your phone number" value={employeeInfo.phone || ""} onChange={onChangeEmployeeInfo}/>
                                            </div>
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputContactName">Birth date</label>
                                                <div>
                                                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                        <DatePicker value={dayjs(employeeInfo.birthDate)} 
                                                            onChange={()=>{}} 
                                                            className="form-control"
                                                            name="birthDate"
                                                            slotProps={{
                                                                textField: { size: 'small' },  // Set the size here
                                                            }}
                                                            disabled={!editable}
                                                        />
                                                    </LocalizationProvider>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="mb-3">
                                            <label className="small mb-1" htmlFor="inputNotes">Notes</label>
                                            <textarea className="form-control" name="notes" disabled={!editable} id="inputNotes" placeholder="Enter your notes" value={employeeInfo.notes || ""} onChange={onChangeEmployeeInfo}/>
                                        </div>
                                        
                                        <div className="mb-3">
                                            <div className="col-md-6">
                                                <label className="small mb-1" htmlFor="inputNotes">Account status</label>
                                                <select className="form-select" value={`${employeeInfo.enabled}`} disabled={!editable} name="enabled" onChange={onChangeStatus}>
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