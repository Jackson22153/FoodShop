import { faArrowDown, faArrowUp } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

interface Props{
    expandedStaus: boolean,
    expandedStatusToggle: any
}
export default function ExpandedToggleBtn(prop:Props){
    const expandedStatus = prop.expandedStaus;

    function expandedStatusToggle(){
        prop.expandedStatusToggle();
    }

    return(
        <span onClick={expandedStatusToggle} className="custom_dark-btn text-bg-dark btn">
            {expandedStatus? 
                <>
                    Read More <FontAwesomeIcon icon={faArrowDown}/>
                </>:
                <>
                    Show Less <FontAwesomeIcon icon={faArrowUp}/>
                </>
            }
        </span>
    );
}