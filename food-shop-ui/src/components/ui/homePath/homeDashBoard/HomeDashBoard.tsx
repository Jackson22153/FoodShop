import { getClient, getSlide, getCardCategory, getFood } from "../../../../service/image";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import { faArrowRight } from "@fortawesome/free-solid-svg-icons";
import { useEffect, useState } from "react";
import { getCategories, getRecommendedProduct } from "../../../../api/SearchApi";
import CategoriesSection from "../../../stateless/section/categoriesSection/CategoriesSection";
import SlideSection from "../../../stateless/section/slideSection/SlideSection";
import FoodSection from "../../../stateless/section/foodSection/FoodSection";
import { onUserInput } from "../../../../service/search";


function HomeDashBoardComponent(){
    const slide = getSlide();
    // category
    const category1 = getCardCategory('cardItem1');
    const category2 = getCardCategory('cardItem2');
    const category3 = getCardCategory('cardItem3');
    // food
    const orange = getFood('orange');
    const grapes = getFood('grapes');
    const gauva = getFood('gauva');
    // client
    const client = getClient();

    const [categories, setCategories] = useState([]);
    const [categoriesPageNumber, setCategoriesPageNumber] = useState(0);
    const [categoryExpandedStatus, setCategoryExpandedStatus] = useState<boolean>(true);

    //products
    const [recommendedProducts, setRecommendedProducts] = useState([]);

    useEffect(()=>{
        initial()
    }, [])

    function toggleCategoriesExpand(){
        setCategoryExpandedStatus((status)=> !status);
    }

    function initial(){
        // onUserInput('vailoz')
        // categories
        getCategories(0).then(res => {
            if(res.status===200){
                const data = res.data;
                setCategories(data.content);
                // console.log(data)
            }
        })

        // recommended products
        getRecommendedProduct().then(res =>{
            if(res.status===200){
                const data = res.data;
                setRecommendedProducts(data)
                // console.log(data)
            }
        })
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
                <FoodSection lstFoodProducts={recommendedProducts}/>
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

            <section className="client_section layout_padding">
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
            </section>

            {/* <!-- end client section --> */}

            {/* <!-- contact section --> */}
            <section className="contact_section layout_padding">
                <div className="container">
                <h2 className="font-weight-bold">
                    Contact Us
                </h2>
                <div className="row">
                    <div className="col-md-8 mr-auto">
                    <form action="">
                        <div className="contact_form-container">
                        <div>
                            <div>
                            <input type="text" placeholder="Name"/>
                            </div>
                            <div>
                            <input type="text" placeholder="Phone Number"/>
                            </div>
                            <div>
                            <input type="email" placeholder="Email"/>
                            </div>

                            <div className="mt-5">
                            <input type="text" placeholder="Message"/>
                            </div>
                            <div className="mt-5">
                            <button type="submit">
                                send
                            </button>
                            </div>
                        </div>

                        </div>

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