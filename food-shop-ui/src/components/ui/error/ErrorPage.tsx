import { useEffect } from 'react';
import { Routes, Route } from 'react-router-dom'
import ForbiddenErrorPageComponent from './ForbiddenErrorPage';
import InternalErrorPageComponent from './InternalErrorPage';

export default function ErrorPageComponent(){
    useEffect(()=>{

    }, []);


    return(
        <div id='error-page'>
            <Routes>
                <Route path='/403' Component={ForbiddenErrorPageComponent}/>
                <Route path='/500' Component={InternalErrorPageComponent}/>
            </Routes>
        </div>
    );
}