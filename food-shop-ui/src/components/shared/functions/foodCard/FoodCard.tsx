import { FoodPath } from "../../../../constant/FoodShoppingURL";

interface Props{
    foodID: number,
    foodName: string,
    // foodDescription: string,
    foodImageSrc: string|undefined
}
export default function FoodCard(prop: Props){
    const foodID = prop.foodID
    const foodName = prop.foodName;
    // const foodDescription = prop.foodDescription;
    const foodImageSrc = prop.foodImageSrc;

    return(
        <div className="card" style={{height:"100%"}}>
            <a href={FoodPath(foodName, foodID)}>
                <img className="card-img-top" src={foodImageSrc} alt="Card image cap" />
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