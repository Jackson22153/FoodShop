import { useContext, useEffect, useState } from "react";
import { getRecommendedProduct } from "../../../../api/SearchApi";
import CategoriesSection from "../../../shared/website/sections/categoriesSection/CategoriesSection";
import SlideSection from "../../../shared/website/sections/slideSection/SlideSection";
import FoodSection from "../../../shared/website/sections/foodSection/FoodSection";
import { PathProvider } from "../../../contexts/PathContext";
import { FOODS_PATH } from "../../../../constant/FoodShoppingURL";
import CategoriesContext from "../../../contexts/CategoriesContext";
import { Category, CurrentProduct } from "../../../../model/Type";


function HomeDashBoardComponent(){
    // const slide = getSlide();

    const categories = useContext<Category[]>(CategoriesContext);
    const [categoryExpandedStatus, setCategoryExpandedStatus] = useState<boolean>(true);
    //products
    const [recommendedProducts, setRecommendedProducts] = useState<CurrentProduct[]>([]);

    useEffect(()=>{
        initial()
    }, [])

    function toggleCategoriesExpand(){
        setCategoryExpandedStatus((status)=> !status);
    }

    function initial(){
        // recommended products
        fetchRecommendedProducts();
    }

    const fetchRecommendedProducts = async ()=>{ 
        const res = await getRecommendedProduct();
        if(200<=res.status && res.status<300){
            const data = res.data;
            setRecommendedProducts(data);
        }
    }

    return(
        <>
            <div className="hero_area">
                <SlideSection/>
            </div>

            {/* <!-- service section --> */}
            {recommendedProducts &&
                <PathProvider value={FOODS_PATH}>
                    <FoodSection lstFoodProducts={recommendedProducts} sectionTitle="Recommended Foods"/>
                </PathProvider>
            }

            {categories &&
                <CategoriesSection lstCategories={categories} 
                    expandedStatus={categoryExpandedStatus}
                    expandedStatusFunction={toggleCategoriesExpand}/>
            }
  
            <section className="tasty_section">
                <div className="container_fluid">
                    <h2>
                        Very tasty fruits
                    </h2>
                </div>
            </section>

            <section className="contact_section layout_padding">
                <div className="container">
                    <h2 className="font-weight-bold">
                        Contact Us
                    </h2>
                    <div className="row">
                        <div className="col-md-8 mr-auto">
                            <form className="contact_form-container" action="">
                                <div className="form-group">
                                    <input type="text" className="form-control" id="name-contact"placeholder="Name"/>
                                </div>

                                <div className="form-group">
                                    <input type="tel" className="form-control" id="phone-contact" placeholder="Phone number"/>
                                </div>

                                <div className="form-group">
                                    <input type="email" className="form-control" id="email-contact" placeholder="Email"/>
                                </div>
                                <div className="mt-5"></div>
                                <div className="form-group">
                                    <input type="email" className="form-control" id="message-contact" placeholder="Message"/>
                                </div>
                                <div className="mt-5"></div>

                                <button type="submit" className="btn">
                                    Send
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </section>
            {/* <!-- end contact section --> */}


            {/* <!-- map section --> */}
            <section className="map_section">
                <div id="map" className="h-100 w-100 ">
                </div>
            </section>
        </>
    );
}
export default HomeDashBoardComponent;