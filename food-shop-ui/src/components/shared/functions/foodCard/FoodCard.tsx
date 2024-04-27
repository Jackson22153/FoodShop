import { useContext } from "react";
import { ceilRound, convertNameForUrl } from "../../../../service/convert";
import foodPathContext from "../../../contexts/PathContext";
import { displayProductImage } from "../../../../service/image";
import { CurrentProduct } from "../../../../model/Type";

interface Props{
    foodInfo: CurrentProduct
}
export default function FoodCard(prop: Props){
    const foodInfo = prop.foodInfo;
    const foodID = foodInfo.productID
    const foodName = foodInfo.productName;
    const foodPrice = foodInfo.unitPrice;
    const foodPath = useContext(foodPathContext);
    const name = convertNameForUrl(foodName);
    const url = `${foodPath}/${name}?sp=${foodID}`;
    // const foodDescription = foodInfo.foodDescription;
    const foodImageSrc = foodInfo.picture;

    return(
        <div className="card position-relative" style={{height:"100%"}}>
            <a href={url}>
                {foodInfo.discountID!=null && foodInfo.discountPercent>0 &&
                    <div className="position-absolute mt-5">
                        <span className="badge rounded-pill badge-discount bg-danger ">
                            -{foodInfo.discountPercent}%
                        </span>
                    </div>
                }
                <img className="card-img-top w-100" src={displayProductImage(foodImageSrc)} alt="Card image cap" />
                <div className="card-body pt-0">
                    <span className="w-100 d-block text-body-tertiary">
                        {foodInfo.categoryName}
                    </span>
                    <h5 className="card-title">{foodName}</h5>
                    <span className="card-text w-100 d-block">
                        <ins className="mx-3">
                            <span>
                                <b>
                                    <span>$</span>
                                    {ceilRound(foodPrice*(1-foodInfo.discountPercent/100))}
                                </b> 
                            </span>
                        </ins>
                        {foodInfo.discountID!= null &&
                            <del className="text-body-secondary">
                                <span>
                                    <span>$</span>
                                    {foodPrice}
                                </span>
                            </del>
                        }
                    </span>
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