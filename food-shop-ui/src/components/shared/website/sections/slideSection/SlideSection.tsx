import { faArrowLeft, faArrowRight } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect, useState } from "react";
import { getSlide } from "../../../../../service/Image";
import { SLIDE_TIMEOUT } from "../../../../../constant/WebConstant";
import { Link } from "react-router-dom";
import { FOODS_PATH } from "../../../../../constant/FoodShoppingURL";

export default function SlideSection(){
    const [currentSlide, setCurrentSlide] = useState(0);
    const [slides]= useState(getSlide());
    let timerId: NodeJS.Timeout;

    useEffect(()=>{
        timerId = setTimeout(startNewTimer, SLIDE_TIMEOUT);

        return () => {
            clearTimeout(timerId);
        };
    }, [])
    
    const startNewTimer = () => {
        setCurrentSlide((preSlide) => (preSlide + 1) % slides.length);
        timerId = setTimeout(startNewTimer, SLIDE_TIMEOUT); // Continuously loop
    };


    const onClickNextSlide = ()=>{
        setCurrentSlide(current => {
            if(current==slides.length-1){
                return 0;
            }
            return current + 1;
        })
    }
    
    const onClickPreviousSlide = ()=>{
        setCurrentSlide(current => {
            if(current==0){
                return slides.length-1;
            }
            return current - 1;
        })
    }

    return(
        <section className="h-100 slider_section position-relative slideshow">
            <div id="carouselExampleControls" className="carousel" data-ride="carousel">
                <div className="carousel-inner">
                    {slides.map((image, index) =>(
                        <div key={index} className={`slide ${currentSlide==index?'active':''}`}>
                            <div className="slider_item-box">
                                <div className="slider_item-container">
                                    <div className="container">
                                        <div className="row">
                                            <div className="col-md-6">
                                                <div className="slider_item-detail">
                                                    <h1>
                                                        Welcome to <br />
                                                        Our Fruits Shop
                                                    </h1>
                                                    <p>
                                                        There are many variations of passages of Lorem
                                                        Ipsum available, but the majority have suffered
                                                        alteration in some form, by injected humour, or
                                                        randomised words which don't look even slightly
                                                        believable.
                                                    </p>
                                                    <div className="d-flex">
                                                        <Link to={FOODS_PATH}>
                                                            <div  className="text-uppercase custom_orange-btn mr-3">
                                                                Shop Now
                                                            </div>
                                                        </Link>
                                                        <Link to={""}>
                                                            <div className="text-uppercase custom_dark-btn">
                                                                Contact Us
                                                            </div>
                                                        </Link>
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="col-md-6">
                                                <div className="slider_img-box">
                                                    <img src={image} alt="" className="" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
                <div className="custom_carousel-control">
                    <button className="carousel-control-prev" role="button" data-slide="prev" onClick={onClickPreviousSlide}>
                        <FontAwesomeIcon icon={faArrowLeft}/>
                        <span className="sr-only">Previous</span>
                    </button>
                    <button className="carousel-control-next" role="button" data-slide="next" onClick={onClickNextSlide}>
                        <FontAwesomeIcon icon={faArrowRight}/>                                    
                        <span className="sr-only">Next</span>
                    </button>
                </div>
            </div>
        </section>
    );
}