import { ChangeEventHandler, FormEventHandler, useEffect, useState } from 'react';
import { Category, ProductDetails } from '../../../../../model/Type';
import { addProduct } from '../../../../../api/AdminApi';
import { getCategories } from '../../../../../api/SearchApi';
import { FormControl, InputLabel, Select, MenuItem, SelectChangeEvent } from '@mui/material';
import TextEditor from '../../../../shared/functions/editor/Editor';
import { ProductImageChangeInput } from '../../../../shared/functions/product-image-change-input/ProductImageChangeInput';
import { ALERT_TIMEOUT, ALERT_TYPE } from '../../../../../constant/WebConstant';
import { Alert, Modal } from '../../../../../model/WebType';
import AlertComponent from '../../../../shared/functions/alert/Alert';
import ModalComponent from '../../../../shared/functions/modal/Modal';
import ScrollToTop from '../../../../shared/functions/scroll-to-top/ScrollToTop';

export default function AdminAddFoodComponent(){
    const [foodInfo, setFoodInfo] = useState<ProductDetails>({
        productID: 0,
        productName: '',
        quantityPerUnit: 0,
        unitPrice: 0,
        unitsInStock: 0,
        discontinued: true,
        picture: '',
        description: '',
        discountPercent: 0,
        startDate: '',
        endDate: '',

        categoryID: 1,
        categoryName: ''
    });
    const [description, setDescription] = useState("");
    const [alert, setAlert] = useState<Alert>({
        message: '',
        type: ALERT_TYPE.INFO,
        isShowed: false
    });
    const [updateInfoModal, setUpdateInfoModal] = useState<Modal>({
        title: "Confirm action",
        message: "Do you want to continue?",
        isShowed: false
    })

    const [categories, setCategories] = useState<Category[]>([])

    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        fetchCategories(0);
    }

    const fetchCategories = async (pageNumber: number)=>{
        const res = await getCategories(pageNumber);
        if(res.status){
            const data = res.data;
            setCategories(data.content);
        }
    }

    const onChange: ChangeEventHandler<any> = (event)=>{
        const name = event.target.name;
        const value = event.target.value
        console.log(`${name}: ${value}`)
        setFoodInfo({...foodInfo, [name]: value})
    }

    const onChangeDiscontinuedStatus: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const value = event.target.checked;
        setFoodInfo({
            ...foodInfo,
            discontinued: value
        })
    }

    const onChangeDescription = (content: string)=>{
        setDescription(content)
    }
    const onChangePicture = (imageUrl: string)=>{
        if(foodInfo){
            setFoodInfo({...foodInfo, picture: imageUrl})
        }
    }

    const handleCategoryChange = (event: SelectChangeEvent) => {
        if(foodInfo){
            const name = event.target.name;
            const value = event.target.value;
            setFoodInfo({
                ...foodInfo, 
                [name]:+value
            });
        }
    };

    const onClickUpdateProduct:FormEventHandler<HTMLButtonElement> = async (event)=>{
        event.preventDefault();
        toggleUpdateInfoModal();
    }


    // update info
    const toggleUpdateInfoModal = ()=>{
        setUpdateInfoModal(modal=>({
            ...modal,
            isShowed:!modal.isShowed
        }))
    }

    const handleClickCloseUpdateInfoModal = ()=>{
        setUpdateInfoModal(modal=>({
            ...modal,
            isShowed:!modal.isShowed
        }))
    }
    const handleClickConfirmUpdateInfoModal = async ()=>{
        console.log(foodInfo)
        if(foodInfo){
            const data = {
                productName: foodInfo.productName,
                quantityPerUnit: foodInfo.quantityPerUnit,
                unitPrice: foodInfo.unitPrice,
                unitsInStock: foodInfo.unitsInStock ,
                discontinued: foodInfo.discontinued,
                picture: foodInfo.picture,
                description: description,
                categoryID: foodInfo.categoryID,
            }
            try {
                const res = await addProduct(data);
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?`New Product ${foodInfo.productName} has been added successfully`: `New Product ${foodInfo.productName} can not be added`,
                        type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                        isShowed: true
                    })  
                }
            } 
            catch (error) {
                setAlert({
                    message: `New Product ${foodInfo.productName} can not be added`,
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }
            finally{
                setTimeout(()=>{
                    setAlert({...alert, isShowed: false});
                }, ALERT_TIMEOUT)
            }
        }
    }

    return(
        <div className="container-fluid container">
            <ScrollToTop/>
            <AlertComponent alert={alert}/>
            <section className="py-5">
                <div className="container">
                    <div className="row">
                        <div className="col-lg-4 py-2">
                            <div className="h-100 img-large position-relative w-100 bg-white px-3 py-2 border rounded-2">
                                <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
                                    <li className="nav-item" role="presentation">
                                        <span className="nav-link text-dark active"
                                            id="product-img-tab" role="tab">Product's Img</span>
                                    </li>
                                </ul>
                                <ProductImageChangeInput imageSrc={foodInfo.picture} 
                                    disable={false} onChangePicture={onChangePicture}/>
                            </div>
                        </div>
                        <div className="col-lg-8 py-2">
                            <div className=" h-100 bg-white border rounded-2 px-3 py-2">
                                <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
                                    <li className="nav-item" role="presentation">
                                        <span className="nav-link text-dark active"
                                            id="product-info-tab" role="tab">Product's Info</span>
                                    </li>
                                </ul>
                                <div className="row">
                                    <div className="form-group col-md-4">
                                        <label htmlFor="product-name-admin"><strong>Product Name</strong></label>
                                        <input type="text" className="form-control text-dark" id="product-name-admin" required 
                                            placeholder="Product Name" onChange={onChange} value={foodInfo.productName} name='productName'/>
                                    </div>
                                    <div className="form-check form-switch col-md-7">
                                        <label className="form-check-label" htmlFor="product-discontinued-status"><strong>Discontinued</strong></label>
                                        <div className='ms-3'>
                                            <input className="form-check-input discount-switch-button m-0" type="checkbox" id="product-discontinued-status"
                                                checked={foodInfo.discontinued} onChange={onChangeDiscontinuedStatus} 
                                                name='discontinued' required/>
                                        </div>
                                    </div>
                                </div>

                                <div className="mb-3">
                                    <div className="row">
                                        <div className="form-group col-md-2">
                                            <label htmlFor="unit-price-admin"><strong>Price</strong></label>
                                            <input type="number" className="form-control text-dark text-align-center" id="unit-price-admin" required
                                                placeholder="Price" onChange={onChange} value={foodInfo.unitPrice} min={0}
                                                name='unitPrice'/>
                                        </div>
                                        <div className="form-group col-md-4">
                                            <label htmlFor="quantity-per-unit-admin"><strong>Quantity Per Unit</strong></label>
                                            <input type="text" className="form-control text-dark" id="quantity-per-unit-admin"
                                                placeholder="Quantity Per Unit" onChange={onChange} value={foodInfo.quantityPerUnit}
                                                name='quantityPerUnit' required/>
                                        </div>

                                        <div className="form-group col-md-4">
                                            <label htmlFor="units-in-stock-admin"><strong>Units in stock</strong> <span className={`${foodInfo.unitsInStock>0?"text-success": 'text-danger'} ms-2`}>
                                                {foodInfo.unitsInStock>0? "(In stock)": "(Out of stock)"}
                                            </span></label>
                                            <input type="number" className="form-control text-dark text-align-center" id="units-in-stock-admin" 
                                                placeholder="Units in stock" onChange={onChange} value={foodInfo.unitsInStock} 
                                             name='unitsInStock' min={0} required/>
                                        </div>
                                    </div>
                                </div>

                                <hr />

                                <div className="row mb-3">
                                    <div className="col-md-4">
                                        <FormControl sx={{ m: 1, minWidth: "100%" }} size="small">
                                            <InputLabel id="category-product-admin">Category</InputLabel>
                                            <Select
                                                labelId="category-product-admin"
                                                id="select-category-product-admin"
                                                value={`${foodInfo.categoryID}`}
                                                label="Category"
                                                name='categoryID'
                                                onChange={handleCategoryChange}
                                             required
                                            >
                                                {categories.map((category)=>(
                                                    <MenuItem value={`${category.categoryID}`} key={category.categoryID}>
                                                        {category.categoryName}
                                                    </MenuItem>
                                                ))}
                                            </Select>
                                        </FormControl>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>   

            <section className="bg-light border-top py-4">
                <div className="container">
                    <div className="row">
                        <div className="col-lg-12 mb-4">
                            <div className="border rounded-2 px-3 py-2 bg-white">
                                <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
                                    <li className="nav-item" role="presentation">
                                        <span className="nav-link text-dark active"
                                            id="description-tab" role="tab">Description</span>
                                    </li>
                                </ul>
                                {/* <!-- Pills navs --> */}

                                {/* <!-- Pills content --> */}
                                <div className="tab-content" id="product-content">
                                    {/* {foodInfo.description} */}
                                    <div id="editor">
                                        <TextEditor content={description} 
                                            onChangeContent={onChangeDescription} 
                                            editable={true} height='100%'/>
                                    </div>   

                                </div>
                                {/* <!-- Pills content --> */}
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <div className="row">
                <div className="col-md-5 my-2">
                    <button className="btn btn-primary" type="submit" onClick={onClickUpdateProduct}>
                        Add New Product
                    </button>
                </div>
            </div>
            <ModalComponent modal={updateInfoModal} handleConfirmButton={handleClickConfirmUpdateInfoModal} 
                handleCloseButton={handleClickCloseUpdateInfoModal}/>   
        </div>
    );
}