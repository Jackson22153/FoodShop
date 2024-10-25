import { useEffect, useRef, useState } from 'react';
import { ExistedProduct, Pageable } from '../../../../../model/Type';
import PaginationSection from '../../../../shared/website/sections/paginationSection/PaginationSection';
import { getPageNumber } from '../../../../../service/Pageable';
import { ADMIN_PRODUCTS } from '../../../../../constant/FoodShoppingURL';
import { getProducts } from '../../../../../api/AdminApi';
import { Link } from 'react-router-dom';
import { displayProductImage } from '../../../../../service/Image';
import { ceilRound } from '../../../../../service/Convert';
import FloatButton from '../../../../shared/functions/floatbutton/FloatButton';

export default function AdminFoodsComponent(){
    const [foodlist, setFoodlist] = useState<ExistedProduct[]>([]);
    const inputFileRef = useRef<HTMLInputElement>()
    const [pageable, setPageable] = useState<Pageable>({
        first: false,
        last: false,
        number: 0,
        totalPages: 0
    })
    // const [isShowed, setIsShowed] = useState(false)
    const pageNumber = getPageNumber()
    // const [data, setData] = useState([])

    // const toggleModal = ()=>{
    //     setIsShowed(show => !show)
    // }
    // const OnClickCloseInput = (e)=>{
    //     e.preventDefault()
    //     toggleModal()
    // }

    // const handleFileUpload = (event) => {
    //     const file = event.target.files[0];
    //     const reader = new FileReader();
        
    //     reader.onload = (e) => {
    //       const binaryStr = e.target.result;
    //       const wb = XLSX.read(binaryStr, { type: 'binary' });
    //       const sheetName = wb.SheetNames[0];
    //       const worksheet = wb.Sheets[sheetName];
    //       const jsonData = XLSX.utils.sheet_to_json(worksheet);
    //       setData(jsonData);
    //     };
    
    //     reader.readAsBinaryString(file);
    // }
    // async function updateProducSizesInfo(data){
    //     const res = await updateProductSizes(data)
    //     if(res.status){

    //     }
    // }
    // const onClickUpdateProductSizes = (e)=>{
    //     e.preventDefault()
    //     toggleModal()
    // }

    // const onClickConfirmInput = (e)=>{
    //     e.preventDefault();
    //     updateProducSizesInfo(JSON.parse(JSON.stringify(data, null, 2)))
    // }

    async function fetchFoods(pageNumber: number){
        const response = await getProducts(pageNumber);
        if(200<=response.status && response.status<300){
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
    }, [pageNumber])

    function initial(){
        fetchFoods(pageNumber)
    }

    const onClickFileInput = (e)=>{
        e.preventDefault();
        if(inputFileRef.current){
            inputFileRef.current.click();
        }
    }

    return(
        <div className="container-fluid container">
            <div className="row">
                <section className="foods-section pos-relative">
                    <div className="container">
                        <h2 className="custom_heading">Foods</h2>
                        <p className="custom_heading-text">
                            
                        </p>
                        <div className=" layout_padding2">
                            <div className="card-deck">
                                {foodlist.map((productInfo, index) =>(
                                    <div className="col-md-3 col-sm-6 mb-3" key={index}>
                                        <div className="card position-relative" style={{height:"100%"}}>
                                            {productInfo.discountID!=null && productInfo.discountPercent>0 &&
                                                <div className="position-absolute mt-5">
                                                    <span className="badge rounded-pill badge-discount bg-danger ">
                                                        -{productInfo.discountPercent}%
                                                    </span>
                                                </div>
                                            }
                                            <div className="card-img-top product-card-image-container">
                                                <Link to={`${ADMIN_PRODUCTS}/${productInfo.productName}?sp=${productInfo.productID}`}>
                                                    <img className="w-100 h-100" src={displayProductImage(productInfo.picture)} alt="Card image cap" />
                                                </Link>
                                            </div>
                                            <div className="card-body pt-0 product-card-body">
                                                <span className="w-100 d-block text-body-tertiary">
                                                    {productInfo.categoryName}
                                                </span>
                                                <h5 className="card-title">
                                                    <Link to={`${ADMIN_PRODUCTS}/${productInfo.productName}?sp=${productInfo.productID}`}>
                                                        <div className="card-title">{productInfo.productName}</div>   
                                                    </Link>
                                                </h5>
                                                <span className="card-text w-100 d-block">
                                                    {!productInfo.discontinued ?
                                                        <>
                                                            <ins className="mx-3">
                                                                <span>
                                                                    <b>
                                                                        <span>$</span>
                                                                        {ceilRound(productInfo.unitPrice*(1-productInfo.discountPercent/100))}
                                                                    </b> 
                                                                </span>
                                                            </ins>
                                                            {productInfo.discountID!= null &&
                                                                <del className="text-body-secondary">
                                                                    <span>
                                                                        <span>$</span>
                                                                        {productInfo.unitPrice}
                                                                    </span>
                                                                </del>
                                                            }
                                                        </>:
                                                        <b className='m-0'>Discontinued</b>
                                                    }
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                    {/* <FloatButton toggleProductSizesModal={toggleModal}/>   */}
                </section>
            </div>

            {/* <div className={`modal ${isShowed?'d-block':''}`} tabIndex={-1} role="dialog">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">File input</h5>
                            <button type="button" className="close ml-auto" data-dismiss="modal" onClick={OnClickCloseInput} aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <div className='bg-white p-3 d-flex row'>
                                <div className='col-md-4'>
                                    <p>Choose your xlsx file.</p>   
                                    <button className='btn btn-primary' onClick={onClickFileInput}>
                                        Choose your file
                                    </button>
                                    <input type="file" accept='.xlsx' onChange={handleFileUpload} hidden ref={inputFileRef} />
                                </div>
                                <div className='col-md-8'>
                                    <div className='px-3 overflow-auto' style={{maxHeight: "300px"}}>
                                        <pre>{JSON.stringify(data, null, 2)}</pre>  
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-dismiss="modal" 
                                onClick={OnClickCloseInput}>Close</button>
                            <button type="button" className="btn btn-secondary" data-dismiss="modal" 
                                onClick={onClickConfirmInput}>Update</button>
                        </div>
                    </div>
                </div>
            </div> */}

            <div className="row">
                <PaginationSection pageable={pageable}/>
            </div>
        </div>
    );
}