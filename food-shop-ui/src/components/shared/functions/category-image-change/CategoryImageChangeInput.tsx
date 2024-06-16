import { ChangeEventHandler } from "react";
import { displayProductImage } from "../../../../service/Image";
import { uploadCategoryImage } from "../../../../api/ProductApi";

interface Props{
    imageSrc: string,
    disable: boolean,
    onChangePicture: any
    
}
export const CategoryImageChangeInput = (prop: Props)=>{
    const imageSrc = prop.imageSrc;
    const disable= prop.disable;

    const onChange: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const file = event.target.files[0];
        uploadImage(file);
        
    }

    const uploadImage = async (file: File)=>{
        const res = await uploadCategoryImage(file);
        if(200<=res.status&&res.status<300){
            const data = res.data;
            prop.onChangePicture(data.imageUrl);
        }
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
