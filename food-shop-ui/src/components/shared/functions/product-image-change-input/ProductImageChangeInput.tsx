import { ChangeEventHandler } from "react";
import { displayProductImage } from "../../../../service/image";

interface Props{
    imageSrc: string,
    disable: boolean,
    onChangePicture: any
    
}
export const ProductImageChangeInput = (prop: Props)=>{
    const imageSrc = prop.imageSrc;
    const disable= prop.disable;

    const onChange: ChangeEventHandler<HTMLInputElement> = (event)=>{
        console.log(event.target.files)
        // prop.onChangePicture(event);
    }

    return(
        <div className="profile-img">
            <img src={displayProductImage(imageSrc)} alt=""/>
            <div className="file btn btn-lg btn-primary" style={{opacity: disable?0:1}}>
                Change Photo
                <input type="file" name="picture" disabled={disable} className="w-100 h-100" 
                    accept="image/x-png,image/gif,image/jpeg" onChange={onChange}/>
            </div>
        </div>
    )
}
