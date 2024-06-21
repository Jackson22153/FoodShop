import { Route, Routes } from 'react-router-dom';
import CustomerComponent from '../customer/Customer';
import AdminComponent from '../adminPath/admin/Admin';
import EmployeeComponent from '../employee/Employee';
import { useState } from 'react';
import { Modal } from '../../../../model/WebType';
import { ModalProvider } from '../../../contexts/ModalContext';
import ErrorModal from '../../../shared/functions/error-modal/ErrorModal';

export default function UserComponent(){
    const [errorModal, setErrorModal] = useState<Modal>({
        title: "Error",
        message: "string",
        isShowed: false
    })
    

    return(
        <ModalProvider value={{modal: errorModal, setModal: setErrorModal}}>
            <Routes>
                <Route path='*' element={<CustomerComponent/>}/>
                <Route path='customer/*' element={<CustomerComponent/>}/>
                <Route path='employee/*' element={<EmployeeComponent/>}/>
                <Route path='admin/*' element={<AdminComponent/>}/>
            </Routes>
            <ErrorModal modal={errorModal}/>
        </ModalProvider>
    );
}

