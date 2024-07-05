import { ChangeEventHandler, useEffect, useRef, useState } from 'react';
import { getCustomers, getCustomersBySearchParam, getEmployees, 
    getEmployeesBySearchParam, getUsers, getUsersBySearchParam 
} from '../../../../../api/AdminApi';
import { getPageNumber } from '../../../../../service/Pageable';
import { CustomerAccount, EmployeeAccount, Pageable, UserRole } from '../../../../../model/Type';
import UserTable from '../../../../shared/functions/table/UserTable';
import EmployeeTable from '../../../../shared/functions/table/EmployeeTable';
import CustomerTable from '../../../../shared/functions/table/CustomerTable';
import { ADMIN_CUSTOMER, ADMIN_EMPLOYEE } from '../../../../../constant/FoodShoppingURL';

export default function AdminUsersComponent(){
    const USER = "User";
    const CUSTOMER = "Customer";
    const EMPLOYEE = "Employee";
    const [customers, setCustomers] = useState<CustomerAccount[]>([]);
    const [employees, setEmployees] = useState<EmployeeAccount[]>([])
    const [users, setUsers] = useState<UserRole[]>([])
    const [pageable, setPageable] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    });
    const pageNumber = getPageNumber()
    const [searchDropDown, setSearchDropdown] = useState(false);
    const [selectedUserTab, setSelectedUserTab] = useState(0);
    const filterRef = useRef<HTMLDivElement|null>(null);
    const [searchValue, setSearchValue] = useState('');
    const [searchParamArgs, setSearchParamArgs] = useState<string[]>([])

    useEffect(()=>{
        initial();
    }, [pageNumber])

    const initial = ()=>{
        document.addEventListener('click', onClickOutside)
        fetchCustomers(pageNumber);
    }
    // fetch data
    const fetchCustomers = async (pageNumber: number)=>{
        const res = await getCustomers(pageNumber);
        if(res.status){
            const data = res.data;
            console.log(data)
            setCustomers(data.content);
            setPageable({
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            });
        }
    }
    const fetchEmployees = async (pageNumber: number)=>{
        const res = await getEmployees(pageNumber);
        if(res.status){
            const data = res.data;
            setEmployees(data.content);
            setPageable({
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            });
        }
    }

    const fetchSearchUsers = async (pageNumber: number, searchParam: string, searchValue: string)=>{
        const res = await getUsersBySearchParam(pageNumber, searchParam, searchValue);
        if(res.status){
            const data = res.data;
            setUsers(data.content);
            setPageable({
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            });
        }
    }
    const fetchSearchCustomers = async (pageNumber: number, searchParam: string, searchValue: string)=>{
        const res = await getCustomersBySearchParam(pageNumber, searchParam, searchValue);
        if(res.status){
            const data = res.data;
            setCustomers(data.content);
            setPageable({
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            });
        }
    }
    const fetchSearchEmployees = async (pageNumber: number, searchParam: string, searchValue: string)=>{
        const res = await getEmployeesBySearchParam(pageNumber, searchParam, searchValue);
        if(res.status){
            const data = res.data;
            setEmployees(data.content);
            setPageable({
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            });
        }
    }

    // handle event
    const onClickOutside = (event: any)=>{
        if(filterRef.current && !filterRef.current.contains(event.target as Node)){
            setSearchDropdown(false)
        }
    }
    const onClickSearchDropown = ()=>{
        setSearchDropdown(searchDropDown => !searchDropDown);
    }
    const onClickSelectUserTab = (tab: number)=>{
        const pageNumber = getPageNumber();
        setSelectedUserTab(tab)
        switch(tab){
            case 0: {
                fetchCustomers(pageNumber);
                break;
            }
            case 1: {
                fetchEmployees(pageNumber);
                break;
            }
            case 2:{

                break;
            }
        }
    }

    // search purpose
    // pass attribute arguments from left to right
    const onClickSelectAttribute = ([...args])=>{
        // setSelectedUserGroup(args[0]);
        setSearchParamArgs(args)
        setSearchDropdown(false)
    }

    const onKeyUpSearch = ()=>{
        const searchParam = searchParamArgs[1].charAt(0).toLowerCase() + searchParamArgs[1].slice(1);
        switch (searchParamArgs[0]){
            case USER:{
                fetchSearchUsers(pageable.number, searchParam, searchValue);
                break;
            }
            case CUSTOMER:{
                fetchSearchCustomers(pageable.number, searchParam, searchValue);
                break;
            }
            case EMPLOYEE:{
                fetchSearchEmployees(pageable.number, searchParam, searchValue);
                break;
            }
        }
    }

    const onChangeSearchValue: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const value = event.target.value;
        setSearchValue(value);
    }

    return(
        <>
            <div className="container mb-5">
                <nav className="navbar navbar-expand-lg navbar-light p-0">
                    <ul className="navbar-nav mb-2 mb-lg-0 h-100 w-100 nav-fill">
                        {/* <li onClick={(_e)=>onClickSelectUserTab(0)} className={`nav-item border border-bottom-0 d-flex align-items-center cursor-pointer ${selectedUserTab===0?'bg-white':'bg-light'}`}>
                            <span className="nav-link active" aria-current="page">All Users</span>
                        </li> */}
                        <li onClick={(_e)=>onClickSelectUserTab(0)} className={`nav-item border border-bottom-0 d-flex align-items-center cursor-pointer ${selectedUserTab===0?'bg-white':'bg-light'}`}>
                            <span className="nav-link" aria-current="page">Customers</span>
                        </li>
                        <li onClick={(_e)=>onClickSelectUserTab(1)} className={`nav-item border border-bottom-0 d-flex align-items-center cursor-pointer ${selectedUserTab===1?'bg-white':'bg-light'}`}>
                            <span className="nav-link">Employees</span>
                        </li>
                        <li onClick={(_e)=>onClickSelectUserTab(2)} className={`nav-item border border-bottom-0 d-flex align-items-center cursor-pointer ${selectedUserTab===2?'bg-white':'bg-light'}`}>
                            <div className="d-flex mx-auto">
                                <div className="input-group">
                                    <div className="input-group-prepend dropdown" ref={filterRef}>
                                        <button type="button" className="btn btn-outline-secondary rounded-end-0">
                                            {searchParamArgs.length>0?
                                                searchParamArgs[1]:
                                                'Filter'
                                            }
                                        </button>
                                        <button type="button" className="btn btn-outline-secondary dropdown-toggle dropdown-toggle-split rounded-0" 
                                            data-toggle="dropdown" aria-haspopup="true" aria-expanded={searchDropDown} onClick={onClickSearchDropown}>
                                            <span className="sr-only">Toggle Dropdown</span>
                                        </button>
                                        <ul className={`dropdown-menu ${searchDropDown?'show':''}`} aria-labelledby="search-filter-users">
                                            {/* <li>
                                                <div className="btn-group dropright w-100 user-dropdown">
                                                    <span className="dropdown-item">
                                                        User
                                                    </span>
                                                    <div className="dropdown-menu user-dropdown-menu">
                                                        <button type="button" className='dropdown-item' 
                                                            onClick={(_e)=>onClickSelectAttribute([USER, 'UserID'])}>UserID</button>
                                                        <button type="button" className='dropdown-item'
                                                            onClick={(_e)=>onClickSelectAttribute([USER, 'Username'])}>Username</button>
                                                        <button type="button" className='dropdown-item'
                                                            onClick={(_e)=>onClickSelectAttribute([USER, 'Email'])}>Email</button>
                                                        <button type="button" className='dropdown-item'
                                                            onClick={(_e)=>onClickSelectAttribute([USER, 'RoleName'])}>Role Name</button>
                                                    </div>
                                                </div>
                                            </li> */}
                                            <li>
                                                <div className="btn-group dropright w-100 user-dropdown">
                                                    <span className="dropdown-item">
                                                        Customer
                                                    </span>
                                                    <div className="dropdown-menu user-dropdown-menu">
                                                        <button type="button" className='dropdown-item'
                                                            onClick={(_e)=>onClickSelectAttribute([CUSTOMER, 'CustomerID'])}>CustomerID</button>
                                                        <button type="button" className='dropdown-item'
                                                            onClick={(_e)=>onClickSelectAttribute([CUSTOMER, 'ContactName'])}>Contact Name</button>
                                                        <button type="button" className='dropdown-item'
                                                            onClick={(_e)=>onClickSelectAttribute([CUSTOMER, 'Username'])}>Username</button>
                                                        <button type="button" className='dropdown-item'
                                                            onClick={(_e)=>onClickSelectAttribute([CUSTOMER, 'Email'])}>Email</button>
                                                    </div>
                                                </div>
                                            </li>
                                            <li>
                                                <div className="btn-group dropright w-100 user-dropdown">
                                                    <span className="dropdown-item">
                                                        Employee
                                                    </span>
                                                    <div className="dropdown-menu user-dropdown-menu">
                                                        <button type="button" className='dropdown-item'
                                                            onClick={(_e)=>onClickSelectAttribute([EMPLOYEE, 'EmployeeID'])}>EmployeeID</button>
                                                        <button type="button" className='dropdown-item'
                                                            onClick={(_e)=>onClickSelectAttribute([EMPLOYEE, 'FirstName'])}>First Name</button>
                                                        <button type="button" className='dropdown-item'
                                                            onClick={(_e)=>onClickSelectAttribute([EMPLOYEE, 'LastName'])}>Last Name</button>
                                                        <button type="button" className='dropdown-item'
                                                            onClick={(_e)=>onClickSelectAttribute([EMPLOYEE, 'Email'])}>Email</button>
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                    <input type="text" className="form-control" onKeyUp={onKeyUpSearch} 
                                        onChange={onChangeSearchValue} value={searchValue}/>
                                </div>
                            </div>
                        </li>
                    </ul>
                </nav>

                {selectedUserTab===0?
                    <CustomerTable path={ADMIN_CUSTOMER} customers={customers} pageable={pageable}/>:
                selectedUserTab===1?                        
                    <EmployeeTable path={ADMIN_EMPLOYEE} employees={employees} pageable={pageable}/>: 
                selectedUserTab===2&&                       
                    searchParamArgs[0]===CUSTOMER?
                    <CustomerTable path={ADMIN_CUSTOMER} customers={customers} pageable={pageable}/>:
                    searchParamArgs[0]===EMPLOYEE?
                    <EmployeeTable path={ADMIN_EMPLOYEE} employees={employees} pageable={pageable}/>:
                    <div className='border bg-white empty-table'>
    
                    </div>
                }
            </div>
        </>
    );
}