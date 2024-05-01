import { useState } from "react";
import { Employee } from "../../../../../model/Type";
import { displayUserImage } from "../../../../../service/image";

export default function AdminEmployeeComponent(){
    const [employeeInfo, setEmployeeInfo] = useState<Employee>();

    

    return(
        <div className="container emp-profile box-shadow-default">
            {employeeInfo &&
                <div className="row">
                    <div className="col-md-4">
                        <div className="profile-img">
                            <img src={displayUserImage(employeeInfo.photo)} alt=""/>
                            <div className="file btn btn-lg btn-primary">
                                Change Photo
                                <input type="file" name="file"/>
                            </div>
                        </div>
                    </div>
                    <div className="col-md 6">
                        <div className="profile-head">
                            <h5>
                                Username: {employeeInfo.username}
                            </h5>
                        </div>

                        {/* <ul className="nav nav-tabs" id="myTab" role="tablist">
                            <li className="nav-item">
                                <a className="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">About</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">Timeline</a>
                            </li>
                        </ul> */}


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
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="first-name-employee">First Name</label>
                                            <input type="text" className="form-control" id="first-name-employee" 
                                                placeholder="First Name" value={employeeInfoAlter.firstName} required
                                                onChange={onChangeEmployeeInfo} disabled={editable}
                                                name="firstName"/>
                                            <div className="valid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="last-name-employee">Last Name</label>
                                            <input type="text" className="form-control" id="last-name-employee" 
                                                placeholder="Last Name"required value={employeeInfoAlter.lastName} 
                                                onChange={onChangeEmployeeInfo} disabled={editable} name="lastName"/>
                                            <div className="invalid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                        <div className="col-md-3 mb-3">
                                            <label>Birth Date</label>
                                            <div>
                                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                    <DatePicker value={dayjs(employeeInfoAlter.birthDate)} 
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
                                            <div className="invalid-feedback">
                                                Looks good!
                                            </div>
                                        </div>
                                    </div>
                                    <div className="form-row row">
                                        <div className="col-md-6 mb-3">
                                            <label  htmlFor="address-employee">Address</label>
                                            <input type="text" className="form-control" id="address-employee" placeholder="Address" required
                                                value={employeeInfoAlter.address} onChange={onChangeEmployeeInfo} disabled={editable}
                                                name="address"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid address.
                                            </div>
                                        </div>
                                        <div className="col-md-3 mb-3">
                                            <label  htmlFor="city-employee">City</label>
                                            <input type="text" className="form-control" id="city-employee" placeholder="City" required
                                                value={employeeInfoAlter.city} onChange={onChangeEmployeeInfo} disabled={editable}
                                                name="city"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid city.
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div className="form-row row">
                                        <div className="col-md-6 mb-3">
                                            <label  htmlFor="home-phone-employee">Home Phone</label>
                                            <input type="text" className="form-control" id="home-phone-employee" placeholder="Phone" required
                                                value={employeeInfoAlter.homePhone} onChange={onChangeEmployeeInfo} disabled={editable}
                                                name="homePhone"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid phone.
                                            </div>
                                        </div>

                                        <div className="col-md-3">
                                            <label>Hire Date</label>
                                            <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                <DatePicker value={dayjs(employeeInfoAlter.hireDate)} 
                                                    onChange={onChangeEmployeeInfoBirthDate} 
                                                    className="form-control"
                                                    name="hireDate"
                                                    slotProps={{
                                                        textField: { size: 'small' },  // Set the size here
                                                        }}
                                                    disabled
                                                />
                                            </LocalizationProvider>
                                        </div>
                                    </div>

                                    <div className="form-row row">
                                        <div className="col-md-6 mb-3">
                                            <label  htmlFor="email-employee">Email</label>
                                            <input type="email" className="form-control" id="email-employee" placeholder="Email" required
                                                value={employeeInfoAlter.email} onChange={onChangeEmployeeInfo} disabled={editable}
                                                name="email"/>
                                            <div className="invalid-feedback">
                                                Please provide a valid email.
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
            }
        </div>
    );
}