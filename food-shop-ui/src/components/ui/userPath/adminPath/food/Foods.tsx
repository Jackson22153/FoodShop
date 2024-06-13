import FoodSection from '../../../../shared/website/sections/foodSection/FoodSection';
import { getProducts } from '../../../../../api/SearchApi';
import { useEffect, useState } from 'react';
import { CurrentProduct, Pageable } from '../../../../../model/Type';
import PaginationSection from '../../../../shared/website/sections/paginationSection/PaginationSection';
import { getPageNumber } from '../../../../../service/Pageable';
import { PathProvider } from '../../../../contexts/PathContext';
import { adminProducts } from '../../../../../constant/FoodShoppingURL';

export default function AdminFoodsComponent(){
    const [foodlist, setFoodlist] = useState<CurrentProduct[]>([]);
    const [pageable, setPageable] = useState<Pageable>({
        first: false,
        last: false,
        number: 0,
        totalPages: 0
    })

    async function fetchFoods(pageNumber: number){
        const response = await getProducts(pageNumber);
        if(response.status===200){
            const data = response.data;
            setFoodlist(data.content);
            setPageable({
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages
            });
        }
    }



    useEffect(()=>{
        initial();
    }, [])

    function initial(){
        const pageNumber = getPageNumber();
        fetchFoods(pageNumber)
    }


    return(
        <div className="container-fluid container">
            {/* <!-- Page Heading --> */}
            {/* <div className="d-sm-flex align-items-center justify-content-between mb-4">
                <h1 className="h3 mb-0 text-gray-800">Dashboard</h1>
                <a href="#" className="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm"><i
                    className="fas fa-download fa-sm text-white-50"></i> Generate Report</a>
            </div> */}

            <div className="row">
                <PathProvider value={adminProducts}>
                    <FoodSection lstFoodProducts={foodlist} sectionTitle='Foods'/>
                </PathProvider>
            </div>
            <div className="row">
                <PaginationSection pageable={pageable}/>
            </div>
        </div>
    );
}