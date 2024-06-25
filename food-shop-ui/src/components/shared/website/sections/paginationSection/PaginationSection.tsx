import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { Pageable } from "../../../../../model/Type";
import ScrollToTop from "../../../functions/scroll-to-top/ScrollToTop";

interface Props{
    pageable: Pageable
}
export default function PaginationSection(prop: Props){
    const navigate = useNavigate();
    const url = new URL(window.location.href);
    // const searchParam = new URLSearchParams(url.search);
    const [searchParam] = useSearchParams()

    const totalPages = prop.pageable.totalPages;
    var currentPage = prop.pageable.number;

    function navigatePage(page: number){
        searchParam.set('page', page.toString());
        
        const navigateUrl = url.pathname+'?' + searchParam;
        return navigateUrl;
    }
    function previousPage(){
        const page = currentPage - 1 >= 0?currentPage-1: 0;
        return navigatePage(page)    
    }
    function nextPage(){
        const page = currentPage + 1 < totalPages?currentPage+1: totalPages-1;
        return navigatePage(page);
    }

    function pageNumberScr(){
        const pageNumbersArr = [] as number[];

        if(currentPage === 0){
            const temp = currentPage+2<totalPages?currentPage+2:totalPages-1;
            for(var i = 0;i<=temp;i++)
                pageNumbersArr.push(i);
        }
        else if(currentPage === totalPages-1){
            const temp = currentPage-2>=0?currentPage-2:0;
            for(var i = temp;i<totalPages;i++){
                pageNumbersArr.push(i);
            }
        }else{
            const startedPage = currentPage -1 >= 0? currentPage -1: 0;
            const endedPage = currentPage +1< totalPages?currentPage +1:totalPages-1;
            for(var i = startedPage; i<=endedPage;i++)
                pageNumbersArr.push(i);
        }

        return pageNumbersArr;
    }


    return(
        <div aria-label="Page navigation example" className="d-flex justify-content-center">
            <ScrollToTop/>
            <ul className="pagination z-0">
                <li className="page-item">
                    <Link to={previousPage()}>
                        <div className="page-link" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </div>
                    </Link>
                </li>
                {currentPage >= 2 && totalPages > 3 &&
                    <>
                        <li className="page-item">
                            <Link to={navigatePage(0)}>
                                <div className="page-link">1</div>
                            </Link>
                        </li>
                        {currentPage > 2 &&
                            <li className="page-item"><a className="page-link">...</a></li>
                        }
                    </>
                }
                {pageNumberScr().map((page)=>(
                    <li key={page} className={`page-item ${page===currentPage?'active':''}`}>
                        <Link to={navigatePage(page)}>
                            <div className="page-link">{page+1}</div>
                        </Link>
                    </li>

                ))}
                {currentPage <= totalPages -3 && totalPages > 3 &&
                    <>
                        {currentPage < totalPages -3 &&
                            <li className="page-item"><a className="page-link">...</a></li>
                        }
                        <li className="page-item">
                            <Link to={navigatePage(totalPages-1)}>
                                <div className="page-link">{totalPages}</div>
                            </Link>
                        </li>
                    </>
                }

                <li className="page-item">
                    <Link to={nextPage()}>
                        <div className="page-link" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </div>
                    </Link>
                </li>
            </ul>
        </div>
    );
}