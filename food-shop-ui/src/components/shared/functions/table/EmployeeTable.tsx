import { EmployeeAccount, Pageable } from "../../../../model/Type";
import PaginationSection from "../../website/sections/paginationSection/PaginationSection";

interface Props{
    employees: EmployeeAccount[],
    pageable: Pageable
}
export default function EmployeeTable(prop: Props){
    const employees = prop.employees;
    const pageable = prop.pageable;

    return(
        <table className="table table-bordered">
            <thead>
                <tr>
                    <th scope="col" className="text-align-center">#</th>
                    <th scope="col">EmployeeID</th>
                    <th scope="col">First Name</th>
                    <th scope="col">Last Name</th>
                    <th scope="col">Username</th>
                    <th scope="col">Email</th>
                </tr>
            </thead>
            <tbody>
                {employees.map((employee, index)=>(
                    <tr key={index}>
                        <th scope="row" className="text-align-center">{index+1}</th>
                        <td>{employee.employeeID}</td>
                        <td>{employee.firstName}</td>
                        <td>{employee.lastName}</td>
                        <td>{employee.username}</td>
                        <td>{employee.email}</td>
                    </tr>
                ))}
                <tr>
                    <td colSpan={6}>
                        <div className='py-3'>
                            <PaginationSection pageable={pageable}/>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    )
}