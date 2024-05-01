import { CustomerAccount, Pageable } from "../../../../model/Type";
import PaginationSection from "../../website/sections/paginationSection/PaginationSection";

interface Props{
    customers: CustomerAccount[],
    pageable: Pageable
}
export default function CustomerTable(prop: Props){
    const customers = prop.customers;
    const pageable = prop.pageable;

    return(
        <table className="table table-bordered">
            <thead>
                <tr>
                    <th scope="col" className="text-align-center">#</th>
                    <th scope="col">CustomerID</th>
                    <th scope="col">Contact Name</th>
                    <th scope="col">Username</th>
                    <th scope="col">Email</th>
                </tr>
            </thead>
            <tbody>
                {customers.map((customer, index)=>(
                    <tr key={index}>
                        <th scope="row" className="text-align-center">{index+1}</th>
                        <td>{customer.customerID}</td>
                        <td>{customer.contactName}</td>
                        <td>{customer.username}</td>
                        <td>{customer.email}</td>
                    </tr>
                ))}
                <tr>
                    <td colSpan={5}>
                        <div className='py-3'>
                            <PaginationSection pageable={pageable}/>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    )
}