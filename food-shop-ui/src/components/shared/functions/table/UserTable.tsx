import { Pageable, UserRole } from "../../../../model/Type";
import PaginationSection from "../../website/sections/paginationSection/PaginationSection";

interface Props{
    users: UserRole[],
    pageable: Pageable
}
export default function UserTable(prop: Props){
    const users = prop.users;
    const pageable = prop.pageable;

    return(
        <table className="table table-bordered">
            <thead>
                <tr>
                    <th scope="col" className="text-align-center">#</th>
                    <th scope="col">UserID</th>
                    <th scope="col">Username</th>
                    <th scope="col">Email</th>
                    <th scope="col">Role Name</th>
                </tr>
            </thead>
            <tbody>
                {users.map((user, index)=>(
                    <tr key={index}>
                        <th scope="row" className="text-align-center">{index+1}</th>
                        <td>{user.userID}</td>
                        <td>{user.username}</td>
                        <td>{user.email}</td>
                        <td>{user.roleName}</td>
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