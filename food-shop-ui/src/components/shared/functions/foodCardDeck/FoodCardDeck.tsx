import { CurrentProduct } from "../../../../model/Type";
import FoodCard from "../foodCard/FoodCard";

interface Props{
    foods: CurrentProduct[]
}
export default function FoodCardDeck(prop: Props){
    const foods = prop.foods;

    return(
        <div className="card-deck">
            {foods.map((productInfo, index) =>(
                <div className="col-md-3 col-sm-6 mb-3" key={index}>
                    <FoodCard foodInfo={productInfo}/>
                </div>
            ))}
        </div>
    )
}