export function getPageNumber(){
    const urlParams = new URLSearchParams(window.location.search);
    const pageParam = urlParams.get('page');
    const pageNumberStr = pageParam?pageParam:'0';
    const pageNumber = !isNaN(+pageNumberStr)?+pageNumberStr:0;
    return pageNumber;
}