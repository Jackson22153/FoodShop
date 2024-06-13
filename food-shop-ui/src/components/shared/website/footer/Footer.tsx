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
                        Fruits
                    </h5>
                    <ul>
                        <li>
                        randomised
                        </li>
                        <li>
                        words which
                        </li>
                        <li>
                        don't look even
                        </li>
                        <li>
                        slightly
                        </li>
                        <li>
                        believable. If you
                        </li>
                        <li>
                        are going to use
                        </li>
                        <li>
                        a passage of
                        </li>
                        <li>
                        Lorem Ipsum,
                        </li>
                    </ul>
                    </div>
                    <div className="col-md-3">
                    <h5>
                        Services
                    </h5>
                    <ul>
                        <li>
                        randomised
                        </li>
                        <li>
                        words which
                        </li>
                        <li>
                        don't look even
                        </li>
                        <li>
                        slightly
                        </li>
                        <li>
                        believable. If you
                        </li>
                        <li>
                        are going to use
                        </li>
                        <li>
                        a passage of
                        </li>
                        <li>
                        Lorem Ipsum,
                        </li>
                    </ul>
                    </div>
                    <div className="col-md-3">
                    <h5>
                        List
                    </h5>
                    <ul>
                        <li>
                        randomised
                        </li>
                        <li>
                        words which
                        </li>
                        <li>
                        don't look even
                        </li>
                        <li>
                        slightly
                        </li>
                        <li>
                        believable. If you
                        </li>
                        <li>
                        are going to use
                        </li>
                        <li>
                        a passage of
                        </li>
                        <li>
                        Lorem Ipsum,
                        </li>
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
                    <div className="subscribe_container">
                        <h5>
                        Subscribe Now
                        </h5>
                        <div className="form_container">
                        <form action="">
                            <input type="email"/>
                            <button type="submit">
                            Subscribe
                            </button>
                        </form>
                        </div>
                    </div>
                    </div>
                </div>
                </div>
            </section>

            <section className="container-fluid footer_section">
                <p>
                Copyright &copy; 2019 All Rights Reserved By
                <a href="https://html.design/">Free Html Templates</a>
                </p>
            </section>            
        </footer>
    );
}