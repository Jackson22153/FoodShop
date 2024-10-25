import { ChangeEventHandler, FormEventHandler, useEffect, useRef, useState } from 'react';
import { Category, ProductDetails, ProductSize } from '../../../../../model/Type';
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
import { displayProductImage } from '../../../../../service/Image';
import { uploadProductImage } from '../../../../../api/ProductApi';

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

        categoryID: 0,
        categoryName: ''
    });
    const [foodSize, setFoodSize] = useState<ProductSize>({
        id: "",
        height: 0,
        width: 0,
        length: 0,
        weight: 0,
        productid: 0,
    })
    const [description, setDescription] = useState("");
    const [alert, setAlert] = useState<Alert>({
        message: '',
        type: ALERT_TYPE.INFO,
        isShowed: false
    });
    const [createProductModal, setCreateProductModal] = useState<Modal>({
        title: "Confirm action",
        message: "Do you want to continue?",
        isShowed: false
    })
    const uploadImageBtnRef = useRef<HTMLInputElement>();
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

    const onChangeDiscontinuedStatus = (event)=>{
        const value = event.target.value;
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

    const handleCategoryChange = (event) => {
        if(foodInfo){
            const name = event.target.name;
            const value = event.target.value;
            setFoodInfo({
                ...foodInfo, 
                [name]:+value
            });
        }
    };

    const onClickAddProduct = async (event)=>{
        event.preventDefault();
        toggleCreateNewProductModal();
    }

    const onChangeNumber = (event)=>{
        setFoodInfo({
            ...foodInfo,
            [event.target.name]: +event.target.value
        })
    }

    // update info
    const toggleCreateNewProductModal = ()=>{
        setCreateProductModal(modal=>({
            ...modal,
            isShowed:!modal.isShowed
        }))
    }

    const handleClickCloseCreateProductModal = ()=>{
        setCreateProductModal(modal=>({
            ...modal,
            isShowed:!modal.isShowed
        }))
    }
    const handleClickConfirmCreateProductModal = async ()=>{
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
                height: foodSize.height,
                length: foodSize.length,
                weight: foodSize.weight,
                width: foodSize.width
            }
            try {
                const res = await addProduct(data);
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?`New Product ${foodInfo.productName} has been added successfully`: 
                                        `New Product ${foodInfo.productName} can not be added`,
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

    // image
    const onClickImage = (e)=>{
        e.preventDefault();
        if(uploadImageBtnRef.current){
            uploadImageBtnRef.current.click();
        }
    }

    const onChangeImage: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const file = event.target.files[0];
        uploadImage(file);
        
    }

    const uploadImage = async (file: File)=>{
        const res = await uploadProductImage(file);
        if(200<=res.status&&res.status<300){
            const data = res.data;
            onChangePicture(data.imageUrl);
        }
    }

    // food size
    const onChangeFoodSize= (e)=>{
        const name = e.target.name;
        const value = e.target.value;
        setFoodSize({...foodSize, [name]:value})
    }

    const onClickUpdateProductSize = (e)=>{

    }



    return(
        <div className="container-fluid container">
            <ScrollToTop/>
            <AlertComponent alert={alert}/>

            {foodInfo &&
            <>
                <div className="row">
                    <div className="col-xl-12 col-lg-12 col-md-12 col-sm-12 position-relative mx-auto">
                        <div className="border rounded-2 bg-white p-5 tm-block tm-block-h-auto">
                            <div className="row">
                                <div className="col-12">
                                    <h2 className="d-inline-block" style={{fontWeight: 700}}>
                                        Add new Product
                                    </h2>
                                </div>
                            </div>
                            <form action="" method='post' onSubmit={onClickAddProduct}>
                                <div className="row tm-edit-product-row">
                                    <div className="col-xl-6 col-lg-6 col-md-12">
                                        <div className="form-group mb-3">
                                            <label htmlFor="inputProductName">Product Name</label>
                                            <input
                                                id="inputProductName"
                                                name="productName"
                                                type="text"
                                                value={foodInfo.productName}
                                                onChange={onChange} required
                                                className="form-control validate"
                                            />
                                        </div>
                                        <div className="form-group mb-3">
                                            <label htmlFor="textareaDescription">Description</label>
                                            <textarea className="form-control validate tm-small" 
                                                rows={5}  name='description' value={foodInfo.description} 
                                                onChange={onChange}/>
                                        </div>

                                        <hr />

                                        <div className="form-group mb-3">
                                            <label htmlFor="category">Category</label>
                                            <select className="custom-select form-select tm-select-accounts w-100" 
                                                id="category" name='categoryID' value={foodInfo.categoryID}
                                                onChange={handleCategoryChange} required
                                            >
                                                <option>Select category</option>
                                                {categories.map((category, index)=>(
                                                    <option key={index} value={category.categoryID}>
                                                        {category.categoryName}
                                                    </option>
                                                ))}
                                            </select>
                                        </div>

                                        <hr />

                                        <div className="form-group mb-3">
                                            <label htmlFor="selectDiscontinued">Discontinued</label>
                                            <select className="custom-select form-select tm-select-accounts w-100" 
                                                id="selectDiscontinued" name='discontinued' 
                                                value={`${foodInfo.discontinued}`}
                                                onChange={onChangeDiscontinuedStatus} required
                                            >
                                                <option value={"true"}>True</option>
                                                <option value={"false"}>False</option>
                                            </select>
                                        </div>
                                        <div className="row">
                                            <div className="form-group mb-3 col-xs-12 col-sm-6">
                                                <label htmlFor="inputQuantityPerUnit">Quantity per Unit</label>
                                                <input id="inputQuantityPerUnit" name="quantityPerUnit"
                                                    type="text" onChange={onChange}
                                                    value={foodInfo.quantityPerUnit} required
                                                    className="form-control validate"
                                                />
                                            </div>
                                            <div className="form-group mb-3 col-xs-12 col-sm-6">
                                                <label htmlFor="inputUnitInStock">Units In Stock</label>
                                                <input id="inputUnitInStock" name="unitsInStock" 
                                                    type="number"value={foodInfo.unitsInStock}
                                                    onChange={onChangeNumber}
                                                    className="form-control validate" required
                                                />
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="form-group mb-3 col-xs-12 col-sm-6">
                                                <label htmlFor="inputUnitPrice">Unit Price</label>
                                                <input id="inputUnitPrice" name="unitPrice"
                                                    type="number" onChange={onChangeNumber}
                                                    value={foodInfo.unitPrice}
                                                    className="form-control validate" required
                                                />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-xl-6 col-lg-6 col-md-12 mx-auto mb-4">
                                        <div className="tm-product-img-edit mx-auto">
                                            <img src={displayProductImage(foodInfo.picture)} alt="Product image" 
                                                className="img-fluid d-block mx-auto"/>
                                            <i className="fas fa-cloud-upload-alt tm-upload-icon"></i>
                                        </div>
                                        <div className="custom-file mt-3 mb-3">
                                            <input id="fileInput" type="file" className='d-none' 
                                                accept="image/x-png,image/gif,image/jpeg" 
                                                ref={uploadImageBtnRef} onChange={onChangeImage}/>
                                            <input
                                                type="button"
                                                className="btn btn-primary btn-block mx-auto w-100"
                                                value="CHANGE IMAGE NOW"
                                                onClick={onClickImage}
                                            />
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <hr />
                <div className="row">
                    <div className="col-xl-12 col-lg-12 col-md-12 col-sm-12 position-relative mx-auto">
                        <div className="border rounded-2 bg-white p-5 tm-block tm-block-h-auto">
                            <div className="row">
                                <div className="col-12">
                                    <h2 className="d-inline-block" style={{fontWeight: 700}}>
                                        Product Size
                                    </h2>
                                </div>
                            </div>
                            <form action="" method='post' onSubmit={onClickUpdateProductSize}>
                                <div className="row tm-edit-product-row">
                                    <div className="form-group mb-3 col-xs-3 col-sm-3">
                                        <label htmlFor="inputHeight">Height (cm)</label>
                                        <input
                                            id="inputHeight"
                                            name="height"
                                            type="number"
                                            value={foodSize.height}
                                            onChange={onChangeFoodSize} required
                                            className="form-control validate"
                                        />
                                    </div>
                                    <div className="form-group mb-3 col-xs-3 col-sm-3">
                                        <label htmlFor="inputWidth">Width (cm)</label>
                                        <input
                                            id="inputWidth"
                                            name="width"
                                            type="number"
                                            value={foodSize.width}
                                            onChange={onChangeFoodSize} required
                                            className="form-control validate"
                                        />
                                    </div>
                                    <div className="form-group mb-3 col-xs-3 col-sm-3">
                                        <label htmlFor="inputLength">Length (cm)</label>
                                        <input
                                            id="inputLength"
                                            name="length"
                                            type="number"
                                            value={foodSize.length}
                                            onChange={onChangeFoodSize} required
                                            className="form-control validate"
                                        />
                                    </div>
                                    <div className="form-group mb-3 col-xs-3 col-sm-3">
                                        <label htmlFor="inputWeight">Weight (gram)</label>
                                        <input
                                            id="inputWeight"
                                            name="weight"
                                            type="number"
                                            value={foodSize.weight}
                                            onChange={onChangeFoodSize} required
                                            className="form-control validate"
                                        />
                                    </div>

                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-12">
                        <button type="submit" onClick={onClickAddProduct}
                            className="btn btn-primary btn-block text-uppercase w-100">
                            Add New Product
                        </button>
                    </div>
                </div>
                <ModalComponent modal={createProductModal} 
                    handleConfirmButton={handleClickConfirmCreateProductModal} 
                    handleCloseButton={handleClickCloseCreateProductModal}/>
            </>
            }



            {/* <section className="py-5">
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
                                <div className="tab-content" id="product-content">
                                    <div id="editor">
                                        <TextEditor content={description} 
                                            onChangeContent={onChangeDescription} 
                                            editable={true} height='100%'/>
                                    </div>   

                                </div>
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
            </div> */}
            <ModalComponent modal={createProductModal} 
                handleConfirmButton={handleClickConfirmCreateProductModal} 
                handleCloseButton={handleClickCloseCreateProductModal}/>   
        </div>
    );
}