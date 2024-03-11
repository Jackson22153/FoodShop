import { faArrowLeft, faArrowRight } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

interface Props{
    slide: any
}
export default function SlideSection(prop: Props){
    const slide = prop.slide;
    return(
        <section className=" slider_section position-relative">
            <div id="carouselExampleControls" className="carousel slide" data-ride="carousel">
                <div className="carousel-inner">
                    <div className="carousel-item active">
                        <div className="slider_item-box">
                            <div className="slider_item-container">
                                <div className="container">
                                    <div className="row">
                                        <div className="col-md-6">
                                        <div className="slider_item-detail">
                                            <div>
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
                                                <a href="" className="text-uppercase custom_orange-btn mr-3">
                                                Shop Now
                                                </a>
                                                <a href="" className="text-uppercase custom_dark-btn">
                                                Contact Us
                                                </a>
                                            </div>
                                            </div>
                                        </div>
                                        </div>
                                        <div className="col-md-6">
                                        <div className="slider_img-box">
                                            <div>
                                            <img src={slide} alt="" className="" />
                                            </div>
                                        </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="carousel-item">
                        <div className="slider_item-box">
                            <div className="slider_item-container">
                                <div className="container">
                                <div className="row">
                                    <div className="col-md-6">
                                    <div className="slider_item-detail">
                                        <div>
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
                                            <a href="" className="text-uppercase custom_orange-btn mr-3">
                                            Shop Now
                                            </a>
                                            <a href="" className="text-uppercase custom_dark-btn">
                                            Contact Us
                                            </a>
                                        </div>
                                        </div>
                                    </div>
                                    </div>
                                    <div className="col-md-6">
                                    <div className="slider_img-box">
                                        <div>
                                        <img src={slide} alt="" className="" />
                                        </div>
                                    </div>
                                    </div>
                                </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="carousel-item">
                        <div className="slider_item-box">
                            <div className="slider_item-container">
                                <div className="container">
                                <div className="row">
                                    <div className="col-md-6">
                                    <div className="slider_item-detail">
                                        <div>
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
                                            <a href="" className="text-uppercase custom_orange-btn mr-3">
                                            Shop Now
                                            </a>
                                            <a href="" className="text-uppercase custom_dark-btn">
                                            Contact Us
                                            </a>
                                        </div>
                                        </div>
                                    </div>
                                    </div>
                                    <div className="col-md-6">
                                    <div className="slider_img-box">
                                        <div>
                                        <img src={slide} alt="" className="" />
                                        </div>
                                    </div>
                                    </div>
                                </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="custom_carousel-control">
                    <a className="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
                        <FontAwesomeIcon icon={faArrowLeft}/>
                        <span className="sr-only">Previous</span>
                    </a>
                    <a className="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
                        <FontAwesomeIcon icon={faArrowRight}/>                                    
                        <span className="sr-only">Next</span>
                    </a>
                </div>
            </div>
        </section>
    );
}