import { useContext } from "react";
import { ceilRound, convertNameForUrl } from "../../../../service/Convert";
import foodPathContext from "../../../contexts/PathContext";
import { displayProductImage } from "../../../../service/Image";
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
            {foodInfo.discountID!=null && foodInfo.discountPercent>0 &&
                <div className="position-absolute mt-5">
                    <span className="badge rounded-pill badge-discount bg-danger ">
                        -{foodInfo.discountPercent}%
                    </span>
                </div>
            }
            <div className="card-img-top product-card-image-container">
                <a href={url}>
                    <img className="w-100 h-100" src={displayProductImage(foodImageSrc)} alt="Card image cap" />
                </a>
            </div>
            <div className="card-body pt-0 product-card-body">
                <span className="w-100 d-block text-body-tertiary">
                    {foodInfo.categoryName}
                </span>
                <h5 className="card-title">
                    <a className="card-title" href={url}>{foodName}</a>
                </h5>
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
            {/* <a href={url}>
            </a> */}
        </div>
    )
}