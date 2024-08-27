import { getIcon } from "../../../../service/Image";

export default function FooterComponent(){

    const fbIcon = getIcon('facebook');
    const twitterIcon = getIcon('twitter');
    const linkedinIcon = getIcon('linkedin');
    const instagramIcon = getIcon('instagram');


    return(
        <footer>
            <section className="info_section layout_padding">
                <div className="container">
                    <div className="row">
                        <div className="col-md-3">
                            <h5>
                                Foods
                            </h5>
                            <ul>
                                <li>Home</li>
                                <li>Products</li>
                                <li>Categories</li>
                            </ul>
                            
                        </div>
                            <div className="col-md-3">
                            <h5>
                                Services
                            </h5>
                            <ul>
                                <li>My account</li>
                                <li>Contact us</li>
                            </ul>
                        </div>
                        <div className="col-md-3">
                            <h5>
                                About
                            </h5>
                            <ul>
                                <li>About us</li>
                                <li>Blog</li>
                            </ul>
                        </div>
                        <div className="col-md-3">
                            <div className="social_container">
                                <h5>
                                    Follow Us
                                </h5>
                                <div className="social-box">
                                <a href="">
                                    <img src={fbIcon} alt=""/>
                                </a>

                                <a href="">
                                    <img src={twitterIcon} alt=""/>
                                </a>
                                <a href="">
                                    <img src={linkedinIcon} alt=""/>
                                </a>
                                <a href="">
                                    <img src={instagramIcon} alt=""/>
                                </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>        
        </footer>
    );
}