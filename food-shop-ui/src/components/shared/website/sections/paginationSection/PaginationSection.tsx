import { Pageable } from "../../../../../model/Type";

interface Props{
    pageable: Pageable
}
export default function PaginationSection(prop: Props){
    const url = new URL(window.location.href);
    const searchParam = new URLSearchParams(url.search);

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
        <div aria-label="Page navigation example" className="d-flex justify-content-center mb-3">
            <ul className="pagination">
                <li className="page-item">
                    <a className="page-link" href={previousPage()} aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                {currentPage >= 2 &&
                    <>
                        <li className="page-item">
                            <a className="page-link" href={navigatePage(0)}>1</a>
                        </li>
                        {currentPage > 2 &&
                            <li className="page-item"><a className="page-link">...</a></li>
                        }
                    </>
                }
                {pageNumberScr().map((page)=>(
                    <li key={page} className={`page-item ${page===currentPage?'active':''}`}>
                        <a className="page-link" href={navigatePage(page)}>{page+1}</a>
                    </li>

                ))}
                {currentPage <= totalPages -3 &&
                    <>
                        {currentPage < totalPages -3 &&
                            <li className="page-item"><a className="page-link">...</a></li>
                        }
                        <li className="page-item">
                            <a className="page-link" href={navigatePage(totalPages-1)}>{totalPages}</a>
                        </li>
                    </>
                }

                <li className="page-item">
                    <a className="page-link" href={nextPage()} aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </div>
    );
}