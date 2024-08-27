import { Link } from "react-router-dom";
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
                </div>
            </div>
            <div className="col-md-4 category-img img d-flex justify-content-center align-items-center">
                <img src={displayProductImage(categoryImageSrc)} alt={categoryTitle} className="rounded-4" width="100%" />
                {/* <div className="fruit_img-box d-flex justify-content-center align-items-center img-medium">
                </div> */}
            </div>

            <div className="btn-category-showmore">
                <Link to={CATEGORIES_PATH+"/"+ convertNameForUrl(categoryTitle)}>
                    <div className="custom_dark-btn btn text-bg-dark">
                        Show
                    </div>
                </Link>
            </div>
        </>
    );
}