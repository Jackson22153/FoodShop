import { useContext } from "react";
import { convertNameForUrl } from "../../../../service/convertStr";
import foodPathContext from "../../../contexts/PathContext";
import { getDefaulImage } from "../../../../service/image";

interface Props{
    foodID: number,
    foodName: string,
    // foodDescription: string,
    foodImageSrc: string|undefined
}
export default function FoodCard(prop: Props){
    const foodID = prop.foodID
    const foodName = prop.foodName;
    const foodPath = useContext(foodPathContext);
    console.log(foodPath);
    const name = convertNameForUrl(foodName);
    const url = `${foodPath}/${name}?sp=${foodID}`;
    // const foodDescription = prop.foodDescription;
    const foodImageSrc = prop.foodImageSrc ? prop.foodImageSrc: getDefaulImage();

    return(
        <div className="card" style={{height:"100%"}}>
            <a href={url}>
                <img className="card-img-top w-100" src={foodImageSrc} alt="Card image cap" />
                <div className="card-body">
                    <h5 className="card-title">{foodName}</h5>
                    <p className="card-text">
                        {/* {foodDescription} */}
                        $44
                    </p>
                    {/* <div>
                        <a href="" className="custom_dark-btn">
                            Buy Now
                        </a>
                    </div> */}
                </div>
            </a>
        </div>
    )
}