import { Category } from "../../../../../model/Type";
import CategoriesDetailbox from "../../../functions/categoriesDetailBox/CategoriesDetailbox";
import ExpandedToggleBtn from "../../../functions/expandedToggleBtn/ExpandedToggleBtn";

interface Props{
    lstCategories: Category[],
    expandedStatus: boolean,
    expandedStatusFunction: any
}
export default function CategoriesSection(prop: Props){
    const expandedStatus = prop.expandedStatus;
    var lstCategories = prop.lstCategories;
    lstCategories = expandedStatus? lstCategories.slice(0,3): lstCategories;
    function expandedStatusToggle(){
        prop.expandedStatusFunction()
    }


    return(
        <section className="fruit_section">
            <div className="container">
                <h2 className="custom_heading">Categories</h2>

                <ul className="category-detail-boxes-ul">
                    {lstCategories.map((category)=>(
                        <li className="row layout_padding2 border-bottom category-detail-box-li" key={category.categoryName}>
                            <CategoriesDetailbox categoryTitle={category.categoryName}
                                categoryDetail={category.description}
                                categoryImageSrc={category.picture}
                            />
                        </li>
                    ))}
                </ul>

                <div className="d-flex justify-content-center layout_padding-bottom">
                    <ExpandedToggleBtn expandedStatusToggle={expandedStatusToggle} 
                        expandedStaus={expandedStatus}/>
                </div>
            </div>
        </section>
    );
}