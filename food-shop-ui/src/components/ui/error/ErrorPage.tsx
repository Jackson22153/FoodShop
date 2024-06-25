import { useEffect } from 'react';
import { Routes, Route } from 'react-router-dom'
import ForbiddenErrorPageComponent from './ForbiddenErrorPage';
import InternalErrorPageComponent from './InternalErrorPage';
import NotFoundErrorPageComponent from './NotFoundErrorPage';

export default function ErrorPageComponent(){
    useEffect(()=>{

    }, []);


    return(
        <div id='error-page'>
            <Routes>
                <Route path='/403' Component={ForbiddenErrorPageComponent}/>
                <Route path='/404' Component={NotFoundErrorPageComponent}/>
                <Route path='/500' Component={InternalErrorPageComponent}/>
            </Routes>
        </div>
    );
}