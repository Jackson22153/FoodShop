import { Product } from "../../../../../model/Type";
import FoodCard from "../../../functions/foodCard/FoodCard";


interface Props{
    lstFoodProducts: Product[]
}
export default function FoodSection(prop:Props){
    var lstFoodProducts = prop.lstFoodProducts;

    return(
        <section className="service_section layout_padding ">
            <div className="container">
                <h2 className="custom_heading">Recommended Foods</h2>
                <p className="custom_heading-text">
                    There are many variations of passages of Lorem Ipsum available, but
                    the majority have
                </p>
                <div className=" layout_padding2">
                    <div className="card-deck">
                        {lstFoodProducts.length>0 && lstFoodProducts.map((productInfo:Product) =>(
                            <FoodCard foodName={productInfo.productName} foodID={productInfo.productID} 
                                foodImageSrc={productInfo.picture} key={productInfo.productID}
                            />
                        ))}
                    </div>
                </div>
                <div className="d-flex justify-content-center">
                    <a href="/foods" className="custom_dark-btn">
                        Read More
                    </a>
                </div>
            </div>
        </section>
    );
}