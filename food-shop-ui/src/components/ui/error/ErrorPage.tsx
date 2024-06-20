import { useEffect } from 'react';
import { Routes, Route } from 'react-router-dom'
import ForbiddenErrorPageComponent from './ForbiddenErrorPage';

export default function ErrorPageComponent(){
    useEffect(()=>{

    }, []);


    return(
        <div id='error-page'>
            <Routes>
                <Route path='/403' Component={ForbiddenErrorPageComponent}/>
            </Routes>
        </div>
    );
}