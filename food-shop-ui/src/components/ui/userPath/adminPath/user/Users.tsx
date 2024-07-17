import { ChangeEventHandler, useEffect, useRef, useState } from 'react';
import { getCustomers, getCustomersBySearchParam, getEmployees, 
    getEmployeesBySearchParam} from '../../../../../api/AdminApi';
import { UserAccount, Pageable } from '../../../../../model/Type';
import EmployeeTable from '../../../../shared/functions/table/EmployeeTable';
import CustomerTable from '../../../../shared/functions/table/CustomerTable';
import { ADMIN_CUSTOMER, ADMIN_EMPLOYEE, ADMIN_USERS } from '../../../../../constant/FoodShoppingURL';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';

export default function AdminUsersComponent(){
    const CUSTOMER = "Customer";
    const EMPLOYEE = "Employee";
    const [searchParam] = useSearchParams();
    const type = searchParam.get('type')!=null?searchParam.get('type'):CUSTOMER
    const [users, setUsers] = useState<UserAccount[]>([])
    const navigate = useNavigate()
    const [pageable, setPageable] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    });
    const [searchDropDown, setSearchDropdown] = useState(false);
    const [selectedUserTab, setSelectedUserTab] = useState(0);
    const filterRef = useRef<HTMLDivElement|null>(null);
    const [searchValue, setSearchValue] = useState('');
    const [searchParamArgs, setSearchParamArgs] = useState<string[]>([])

    useEffect(()=>{
        initial();
    }, [type, searchParamArgs[0]])

    const initial = ()=>{
        switch (type.toLowerCase()) {
            case CUSTOMER.toLocaleLowerCase():
                fetchCustomers(pageable.number);
                break;
            case EMPLOYEE.toLowerCase():
                fetchEmployees(pageable.number);
                break;
            default:
                break;
        }

        document.addEventListener('click', onClickOutside)

    }
    // fetch customers
    const fetchCustomers = async (pageNumber: number)=>{
        const res = await getCustomers(pageNumber);
        if(res.status){
            const data = res.data;
            setUsers(data);
        }
    }
    // fetch employees
    const fetchEmployees = async (pageNumber: number)=>{
        const res = await getEmployees(pageNumber);
        if(res.status){
            const data = res.data;
            setUsers(data);
        }
    }
    // search for customers
    const fetchSearchCustomers = async (pageNumber: number, searchParam: string, searchValue: string)=>{
        const res = await getCustomersBySearchParam(pageNumber, searchParam, searchValue);
        if(res.status){
            const data = res.data;
            setUsers(data);
        }
    }
    // search for employees
    const fetchSearchEmployees = async (pageNumber: number, searchParam: string, searchValue: string)=>{
        const res = await getEmployeesBySearchParam(pageNumber, searchParam, searchValue);
        if(res.status){
            const data = res.data;
            setUsers(data);
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

    // search purpose
    // pass attribute arguments from left to right
    const onClickSelectAttribute = ([...args])=>{
        setSearchParamArgs(args)
        setSearchDropdown(false)
        // navigate to appropriate tab
        switch (args[0].toLowerCase()) {
            case CUSTOMER.toLowerCase():
                navigate(`${ADMIN_USERS}?type=${CUSTOMER.toLowerCase()}`)
                break;
            case EMPLOYEE.toLowerCase():
                navigate(`${ADMIN_USERS}?type=${EMPLOYEE.toLowerCase()}`)
                break;
            default:
                break;
        }

    }
    
    const onKeyUpSearch = ()=>{
        const searchParam = searchParamArgs[1].charAt(0).toLowerCase() + searchParamArgs[1].slice(1);
        switch (searchParamArgs[0]){
            case CUSTOMER:{
                fetchSearchCustomers(0, searchParam, searchValue);
                break;
            }
            case EMPLOYEE:{
                fetchSearchEmployees(0, searchParam, searchValue);
                break;
            }
        }
    }

    const onChangeSearchValue: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const value = event.target.value;
        setSearchValue(value);
    }

    return(
        <div className="container mb-5">
            <nav className="navbar navbar-expand-lg navbar-light p-0">
                <ul className="navbar-nav mb-2 mb-lg-0 h-100 w-100 nav-fill">
                    <li className={`nav-item border border-bottom-0 d-flex align-items-center cursor-pointer ${type.toLocaleLowerCase()===CUSTOMER.toLocaleLowerCase()?'bg-white':'bg-light'}`}>
                        <Link to={`${ADMIN_USERS}?type=${CUSTOMER.toLocaleLowerCase()}`} className='w-100 h-100 d-flex align-items-center'>
                            <span className="nav-link" aria-current="page">Customers</span>
                        </Link>
                    </li>
                    <li className={`nav-item border border-bottom-0 d-flex align-items-center cursor-pointer ${type.toLocaleLowerCase()===EMPLOYEE.toLocaleLowerCase()?'bg-white':'bg-light'}`}>
                        <Link to={`${ADMIN_USERS}?type=${EMPLOYEE.toLocaleLowerCase()}`} className='w-100 h-100 d-flex align-items-center'>
                            <span className="nav-link">Employees</span>
                        </Link>
                    </li>
                    <li className={`nav-item border border-bottom-0 d-flex align-items-center cursor-pointer ${selectedUserTab===2?'bg-white':'bg-light'}`}>
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
                                        data-toggle="dropdown" aria-haspopup={searchDropDown} aria-expanded={searchDropDown} onClick={onClickSearchDropown}>
                                        <span className="sr-only">Toggle Dropdown</span>
                                    </button>
                                    <ul className={`dropdown-menu ${searchDropDown?'show':''}`} aria-labelledby="search-filter-users">
                                        <li>
                                            <div className="btn-group dropright w-100 user-dropdown">
                                                <Link to={`${ADMIN_USERS}?type=${CUSTOMER.toLowerCase()}`} className='dropdown-item'>
                                                    Customer
                                                </Link>
                                                <div className="dropdown-menu user-dropdown-menu">
                                                    <button type="button" className='dropdown-item'
                                                        onClick={(_e)=>onClickSelectAttribute([CUSTOMER, 'Username'])}>Username</button>
                                                    <button type="button" className='dropdown-item'
                                                        onClick={(_e)=>onClickSelectAttribute([CUSTOMER, 'FirstName'])}>First Name</button>
                                                    <button type="button" className='dropdown-item'
                                                        onClick={(_e)=>onClickSelectAttribute([CUSTOMER, 'LastName'])}>Last Name</button>
                                                    <button type="button" className='dropdown-item'
                                                        onClick={(_e)=>onClickSelectAttribute([CUSTOMER, 'Email'])}>Email</button>
                                                </div>
                                            </div>
                                        </li>
                                        <li>
                                            <div className="btn-group dropright w-100 user-dropdown">
                                                <Link to={`${ADMIN_USERS}?type=${EMPLOYEE.toLowerCase()}`} className='dropdown-item'>
                                                    Employee
                                                </Link>
                                                <div className="dropdown-menu user-dropdown-menu">
                                                    <button type="button" className='dropdown-item'
                                                        onClick={(_e)=>onClickSelectAttribute([EMPLOYEE, 'Username'])}>Username</button>
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

            {type.toLocaleLowerCase()==CUSTOMER.toLocaleLowerCase() || searchParamArgs[0]===CUSTOMER?
                <CustomerTable path={ADMIN_CUSTOMER} customers={users} pageable={pageable}/>:
            (type.toLocaleLowerCase()==EMPLOYEE.toLocaleLowerCase() || searchParamArgs[0]===EMPLOYEE) &&             
                <EmployeeTable path={ADMIN_EMPLOYEE} employees={users} pageable={pageable}/>
            }   
        </div>
    );
}