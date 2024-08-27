import { CurrentProduct } from "../../../../../model/Type";
import FoodCardDeck from "../../../functions/foodCardDeck/FoodCardDeck";


interface Props{
    sectionTitle: string,
    lstFoodProducts: CurrentProduct[]
}
export default function FoodSection(prop:Props){
    const lstFoodProducts = prop.lstFoodProducts;
    const title = prop.sectionTitle;

    return(
        <section className="foods-section layout_padding ">
            <div className="container">
                <h2 className="custom_heading">{title}</h2>

                <div className=" layout_padding2">
                    <FoodCardDeck foods={lstFoodProducts}/>
                </div>
            </div>
        </section>
    );
}