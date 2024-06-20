import { CATEGORIES_PATH } from "../../../../constant/FoodShoppingURL";
import { convertNameForUrl } from "../../../../service/Convert";
import { displayProductImage } from "../../../../service/Image";

interface Props{
    categoryTitle: string,
    categoryDetail: string,
    categoryImageSrc: string|undefined
}
export default function CategoriesDetailbox(prop:Props){
    const categoryTitle = prop.categoryTitle;
    const categoryDetail = prop.categoryDetail;
    const categoryImageSrc = prop.categoryImageSrc;

    return(
        <>
            <div className="col-md-8">
                <div className="fruit_detail-box">
                    <h3>
                        {categoryTitle}
                    </h3>
                    <p className="mt-4 mb-5">
                        {categoryDetail}
                    </p>
                    <div>
                        <a href={CATEGORIES_PATH+"/"+ convertNameForUrl(categoryTitle)} 
                            className="custom_dark-btn">
                            Show
                        </a>
                    </div>
                </div>
            </div>
            <div className="col-md-4 d-flex justify-content-center align-items-center">
                <div className="fruit_img-box d-flex justify-content-center align-items-center img-medium">
                    <img src={displayProductImage(categoryImageSrc)} alt={categoryTitle} className="" width="100%" />
                </div>
            </div>
        </>
    );
}