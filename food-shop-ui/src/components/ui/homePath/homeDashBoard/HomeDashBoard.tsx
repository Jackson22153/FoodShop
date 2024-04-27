import { getSlide } from "../../../../service/image";
import { useContext, useEffect, useState } from "react";
import { getRecommendedProduct } from "../../../../api/SearchApi";
import CategoriesSection from "../../../shared/website/sections/categoriesSection/CategoriesSection";
import SlideSection from "../../../shared/website/sections/slideSection/SlideSection";
import FoodSection from "../../../shared/website/sections/foodSection/FoodSection";
import { PathProvider } from "../../../contexts/PathContext";
import { foodsPath } from "../../../../constant/FoodShoppingURL";
import CategoriesContext from "../../../contexts/CategoriesContext";
import { Category, CurrentProduct } from "../../../../model/Type";


function HomeDashBoardComponent(){
    const slide = getSlide();

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
        if(res.status===200){
            const data = res.data;
            setRecommendedProducts(data);
        }
    }

    return(
        <>
            <div className="hero_area">
                {/* <!-- slider section --> */}
                <SlideSection slide={slide}/>
                {/* <!-- end slider section --> */}
            </div>

            {/* <!-- service section --> */}
            {recommendedProducts &&
                <PathProvider value={foodsPath}>
                    <FoodSection lstFoodProducts={recommendedProducts} sectionTitle="Recommended Foods"/>
                </PathProvider>
            }
            {/* <!-- end service section --> */}

            {/* <!-- categories section --> */}
            {categories &&
                <CategoriesSection lstCategories={categories} 
                    expandedStatus={categoryExpandedStatus}
                    expandedStatusFunction={toggleCategoriesExpand}/>
            }
            {/* <!-- end categories section --> */}

            {/* <!-- tasty section --> */}
            <section className="tasty_section">
                <div className="container_fluid">
                    <h2>
                        Very tasty fruits
                    </h2>
                </div>
            </section>

            {/* <!-- end tasty section --> */}

            {/* <!-- client section --> */}

            {/* <section className="client_section layout_padding">
                <div className="container">
                <h2 className="custom_heading">Testimonial</h2>
                <p className="custom_heading-text">
                    There are many variations of passages of Lorem Ipsum available, but
                    the majority have
                </p>
                <div>
                    <div id="carouselExampleControls-2" className="carousel slide" data-ride="carousel">
                    <div className="carousel-inner">
                        <div className="carousel-item active">
                            <div className="client_container layout_padding2">
                                <div className="client_img-box">
                                    <img src={client} alt="" />
                                </div>
                                <div className="client_detail">
                                    <h3>
                                        Johnhex
                                    </h3>
                                    <p className="custom_heading-text">
                                        There are many variations of passages of Lorem Ipsum
                                        available, but the majority have suffered alteration in
                                        some form, by injected humour, or randomised words which
                                        don't look even slightly believable. If you are <br />
                                        going to use a passage of Lorem Ipsum, you need to be sure
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div className="carousel-item">
                            <div className="client_container layout_padding2">
                                <div className="client_img-box">
                                    <img src={client} alt="" />
                                </div>
                                <div className="client_detail">
                                    <h3>
                                        Johnhex
                                    </h3>
                                    <p className="custom_heading-text">
                                        There are many variations of passages of Lorem Ipsum
                                        available, but the majority have suffered alteration in
                                        some form, by injected humour, or randomised words which
                                        don't look even slightly believable. If you are <br />
                                        going to use a passage of Lorem Ipsum, you need to be sure
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div className="carousel-item">
                            <div className="client_container layout_padding2">
                                <div className="client_img-box">
                                    <img src={client} alt="" />
                                </div>
                                <div className="client_detail">
                                    <h3>
                                        Johnhex
                                    </h3>
                                    <p className="custom_heading-text">
                                        There are many variations of passages of Lorem Ipsum
                                        available, but the majority have suffered alteration in
                                        some form, by injected humour, or randomised words which
                                        don't look even slightly believable. If you are <br />
                                        going to use a passage of Lorem Ipsum, you need to be sure
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="custom_carousel-control">
                        <a className="carousel-control-prev" href="#carouselExampleControls-2" role="button" data-slide="prev">
                            <FontAwesomeIcon icon={faArrowLeft}/>                                    
                            <span className="" aria-hidden="true"></span>
                            <span className="sr-only">Previous</span>
                        </a>
                        <a className="carousel-control-next" href="#carouselExampleControls-2" role="button" data-slide="next">
                            <FontAwesomeIcon icon={faArrowRight}/>                                    
                            <span className="" aria-hidden="true"></span>
                            <span className="sr-only">Next</span>
                        </a>
                    </div>
                
                    </div>
                </div>
                </div>
            </section> */}

            {/* <!-- end client section --> */}

            {/* <!-- contact section --> */}
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