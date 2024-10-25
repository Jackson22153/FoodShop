import { Link } from "react-router-dom";
import { UserAccount, Pageable } from "../../../../model/Type";
import { displayUserImage } from "../../../../service/Image";

interface Props{
    employees: UserAccount[],
    pageable: Pageable,
    path: string
}
export default function EmployeeTable(prop: Props){
    const employees = prop.employees;
    const pageable = prop.pageable;
    const path = prop.path;

    return(
        <div className="container bootstrap snippets bootdey border">
            <div className="row">
                <div className="col-lg-12 p-0">
                    <div className="main-box no-header clearfix mb-0">
                        <div className="main-box-body clearfix">
                            <div className="table-responsive">
                                <table className="table user-list">
                                    <thead>
                                        <tr>
                                            <th><span>User</span></th>
                                            {/* <th><span>Created</span></th> */}
                                            <th><span>Email</span></th>
                                            <th><span>First Name</span></th>
                                            <th><span>Last Name</span></th>
                                            <th className="text-center"><span>Email verified</span></th>
                                            <th className="text-center"><span>Status</span></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {employees.map((employee)=>(
                                            <tr key={employee.userID}>
                                                <td>
                                                    <img src={displayUserImage(employee.picture)} alt=""/>
                                                    <Link to={`${path}/${employee.userID}`} className="user-link">
                                                        {employee.username}
                                                    </Link>
                                                    <span className="user-subhead">Employee</span>
                                                </td>
                                                {/* <td>2013/08/12</td>
                                                <td className="text-center">
                                                    <span className="label label-default">pending</span>
                                                </td> */}
                                                <td>
                                                    <Link to={`${path}/${employee.userID}`}>
                                                        {employee.email}
                                                    </Link>
                                                </td>
                                                <td>
                                                    <Link to={`${path}/${employee.userID}`}>
                                                        {employee.firstName}
                                                    </Link>
                                                </td>
                                                <td>
                                                    <Link to={`${path}/${employee.userID}`}>
                                                        {employee.lastName}
                                                    </Link>
                                                </td>
                                                <td className="text-align-center">
                                                    <Link to={`${path}/${employee.userID}`}>
                                                        {employee.emailVerified?"true": "false"}
                                                    </Link>
                                                </td>
                                                <td className="text-align-center">
                                                    <Link to={`${path}/${employee.userID}`}>
                                                        {employee.enabled?"true": "false"}
                                                    </Link>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}