import { Route, Routes } from 'react-router-dom';
import CustomerComponent from '../customer/Customer';
import './User.css';
import AdminComponent from '../adminPath/admin/Admin';

export default function UserComponent(){
    return(
        <Routes>
            <Route path='*' element={<CustomerComponent/>}/>
            <Route path='customer' element={<CustomerComponent/>}/>
            <Route path='admin/*' element={<AdminComponent/>}/>
        </Routes>
    );
}