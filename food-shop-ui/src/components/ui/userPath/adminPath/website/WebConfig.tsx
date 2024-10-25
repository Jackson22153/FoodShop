import { useEffect } from 'react';
import config from '../../../../../../config.json';
import { getLogo } from '../../../../../service/Image';


export default function WebConfigComponent(){
    const logo = getLogo();

    useEffect(()=>{
        
    }, [])

    const readFile = ()=>{

    }

    const onClick = ()=>{
        config.image.logo='error.png'
    }
    return(
        <div>
            <img src={logo} alt="" />
            check
            <button onClick={onClick}>Click to change</button>
        </div>
    )
}