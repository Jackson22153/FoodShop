import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";
interface Props{
    foodName: string,
    foodCategory: string,
    foodPrice: number,
    foodQuantity: number,
    foodImgSrc: string|undefined
}

export default function CartCard(prop: Props){
    const name = prop.foodName;
    const category = prop.foodCategory;
    const price = prop.foodPrice;
    const imgSrc = prop.foodImgSrc;
    var quantity = prop.foodQuantity;
    

    return(
        <>
            <div className="col-md-2 col-lg-2 col-xl-2">
                <img
                    src={imgSrc} className="img-fluid rounded-3" alt={name}/>
            </div>
            <div className="col-md-3 col-lg-3 col-xl-3">
                <h6 className="text-muted">{category}</h6>
                <h6 className="text-black mb-0">{name}</h6>
            </div>
            <div className="col-md-3 col-lg-3 col-xl-2 d-flex">
                <button className="btn btn-link px-2"
                // onclick="this.parentNode.querySelector('input[type=number]').stepDown()"
                >
                    <FontAwesomeIcon icon={faMinus}/>
                </button>

                <input id="form1" min="0" name="quantity" value={quantity} type="number"
                    className="form-control form-control-sm" />

                <button className="btn btn-link px-2"
                // onclick="this.parentNode.querySelector('input[type=number]').stepUp()"
                >
                    <FontAwesomeIcon icon={faPlus}/>
                </button>
            </div>
            <div className="col-md-3 col-lg-2 col-xl-2 offset-lg-1">
                <h6 className="mb-0">€ {price}</h6>
            </div>
            <div className="col-md-1 col-lg-1 col-xl-1 text-end">
                <a href="#!" className="text-muted"><i className="fas fa-times"></i></a>
            </div>
        </>
    );
}