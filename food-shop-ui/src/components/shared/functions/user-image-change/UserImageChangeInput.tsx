import { ChangeEventHandler } from "react";
import { displayUserImage } from "../../../../service/Image";
import { uploadUserImage } from "../../../../api/UserApi";

interface Props{
    imageSrc: string,
    disable: boolean,
    onChangePicture: any
    
}
export const UserImageChangeInput = (prop: Props)=>{
    const imageSrc = prop.imageSrc;
    const disable= prop.disable;

    const onChange: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const file = event.target.files[0];
        uploadImage(file);
    }

    const uploadImage = async (file: File)=>{
        const res = await uploadUserImage(file);
        if(res.status){
            const data = res.data;
            // console.log(data)
            prop.onChangePicture(data.imageUrl);
        }
    }

    return(
        <div className="profile-img">
            <img src={displayUserImage(imageSrc)} alt=""/>
            <div className="file btn btn-lg btn-primary" style={{opacity: disable?0:1}}>
                Change Photo
                <input type="file" name="picture" disabled={disable} className="w-100 h-100" 
                    accept="image/x-png,image/gif,image/jpeg" onChange={onChange}/>
            </div>
        </div>
    )
}
