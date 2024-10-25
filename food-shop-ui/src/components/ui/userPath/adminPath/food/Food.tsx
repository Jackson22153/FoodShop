import { ChangeEventHandler, FormEventHandler, MouseEventHandler, useEffect, useRef, useState } from 'react';
import { Category, DiscountDetail, DiscountType, ProductDetails, ProductSize } from '../../../../../model/Type';
import { 
    getDiscountDetail, getDiscountTypes, getDiscountsByProduct, 
    getProductDetail, insertDiscount, updateDiscount, updateProduct, 
    updateProductSize
} from '../../../../../api/AdminApi';
import { useLocation } from 'react-router-dom';
import { getCategories } from '../../../../../api/SearchApi';
import { FormControl, InputLabel, Select, MenuItem, SelectChangeEvent } from '@mui/material';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { PickerChangeHandlerContext, DateTimeValidationError } from '@mui/x-date-pickers/models';
import dayjs from 'dayjs';
import { ceilRound } from '../../../../../service/Convert';
import { ALERT_TIMEOUT, ALERT_TYPE, DISCOUNT_TYPE } from '../../../../../constant/WebConstant';
import { Alert, Modal } from '../../../../../model/WebType';
import AlertComponent from '../../../../shared/functions/alert/Alert';
import ModalComponent from '../../../../shared/functions/modal/Modal';
import ScrollToTop from '../../../../shared/functions/scroll-to-top/ScrollToTop';
import { displayProductImage } from '../../../../../service/Image';
import { uploadProductImage } from '../../../../../api/ProductApi';

export default function AdminFoodComponent(){
    const location = useLocation();
    const [foodInfo, setFoodInfo] = useState<ProductDetails>();
    const [discountEditable, setDiscountEditable] = useState(false);
    const [description, setDescription] = useState("")
    const [foodSize, setFoodSize] = useState<ProductSize>()
    const [alert, setAlert] = useState<Alert>({
        message: '',
        type: ALERT_TYPE.INFO,
        isShowed: false
    });
    const [updateInfoModal, setUpdateInfoModal] = useState<Modal>({
        title: "Confirm action",
        message: "Do you want to update product?",
        isShowed: false
    })
    const [updateProductSizeModal, setUpdateProductSizeModal] = useState<Modal>({
        title: "Confirm action",
        message: "Do you want to update product size?",
        isShowed: false
    })
    const [updateDiscountModal, setupdateDiscountModal] = useState<Modal>({
        title: "Confirm action",
        message: "Do you want to update this discount?",
        isShowed: false
    })
    const [addDiscountModal, setAddDiscountModal] = useState<Modal>({
        title: "Confirm action",
        message: "Do you want to add a new discount?",
        isShowed: false
    })
    const [currentDiscount, setCurrentDiscount] = useState<DiscountDetail>({
        discountID: '',
        discountPercent: 0,
        discountCode: '',
        startDate: '',
        endDate: '',
        description: '',
        discountType: '',
        active: false
    });
    const [newDiscount, setNewDiscount] = useState<DiscountDetail>({
        discountID: '',
        discountPercent: 0,
        discountCode: '',
        startDate: '',
        endDate: '',
        description: '',
        discountType: '',
        active: false
    });
    const [discountTypes, setDiscountTypes] = useState<DiscountType[]>([])
    const [categories, setCategories] = useState<Category[]>([])
    const [editable, setEditable] = useState(false);
    const [chosenDiscountTab, setChosenDiscountTab] = useState(0);
    const [discounts, setDiscounts] = useState<DiscountDetail[]>([]);
    const uploadImageBtnRef = useRef<HTMLInputElement>();

    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        const productID = getProductID();
        if(productID){
            fetchProduct(productID);
            fetchCategories(0);
            fetchDiscountsByProduct(productID, 0);
            fetchDiscountTypes(0);
        }
    }

    const getProductID = ()=>{
        const queryParams = new URLSearchParams(location.search);
        const productID = queryParams.get('sp')
        return productID;
    }

    const fetchProduct = async (productID: string)=>{
        const res = await getProductDetail(productID);
        if(res.status){
            const data = res.data;
            console.log(data)
            const product = data.product;
            const size = data.size;
            setFoodSize(size)
            setFoodInfo(product);
            if(product.discountID){
                fetchDiscount(product.discountID);
            }
            setDescription(product.description);
        }
    }

    const fetchDiscountTypes = async (pageNumber: number)=>{
        const res = await getDiscountTypes(pageNumber);
        if(res.status){
            const data = res.data;
            setDiscountTypes(data.content);
        }
    }


    const fetchCategories = async (pageNumber: number)=>{
        const res = await getCategories(pageNumber);
        if(res.status){
            const data = res.data;
            setCategories(data.content);
        }
    }

    const fetchDiscountsByProduct = async (productID: string, pageNumber: number)=>{
        const res = await getDiscountsByProduct(productID, pageNumber);
        if(res.status){
            const data = res.data;
            setDiscounts(data.content);
        }
    }

    const fetchDiscount = async (discountID: string)=>{
        const res = await getDiscountDetail(discountID);
        if(res.status){
            const data = res.data;
            setCurrentDiscount(data);
        }
    }

    const price = (unitPrice: number)=>{
        if(currentDiscount.active){
            return ceilRound(unitPrice*(1-currentDiscount.discountPercent/100));
        }
        return ceilRound(unitPrice);
    }

    const onChange: ChangeEventHandler<any> = (event)=>{
        const name = event.target.name;
        const value = event.target.value;
        setFoodInfo({
            ...foodInfo,
            [name]: value
        })
    }

    const onChangeNumber = (event)=>{
        setFoodInfo({
            ...foodInfo,
            [event.target.name]: +event.target.value
        })
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
    // change product's picture
    const onChangePicture = (imageSrc: string)=>{
        setFoodInfo({...foodInfo, picture:imageSrc})

    }

    const handleCategoryChange = (event) => {
        const name = event.target.name;
        const value = event.target.value;
        setFoodInfo({
            ...foodInfo, 
            [name]:+value
        });
        // if(foodInfo){
        // }
    };
    const onClickUpdateDiscount:FormEventHandler<any>= async (event)=>{
        event.preventDefault()
        toggleUpdateDiscountModal()
    }

    const onClickUpdateProduct = (event)=>{
        event.preventDefault();
        toggleUpdateInfoModal();
    }

    function changeStateDiscountEditable(){
        setDiscountEditable(editable => !editable)
    }

    const onClickEditable:MouseEventHandler<HTMLButtonElement> = (event)=>{
        event.preventDefault();
        setEditable(editable => !editable)
    }

    const onClickDiscountEditable:MouseEventHandler<HTMLButtonElement> = (event)=>{
        event.preventDefault();
        changeStateDiscountEditable()
    }
    function handleChangeForStartDate(value: any, _context: PickerChangeHandlerContext<DateTimeValidationError>): void {
        const dateTime = value.format('YYYY-MM-DD HH:mm:ss');
        setCurrentDiscount({
            ...currentDiscount,
            startDate: dateTime
        })
    }

    function handleChangeForEndDate(value: any, _context: PickerChangeHandlerContext<DateTimeValidationError>): void {
        const dateTime = value.format('YYYY-MM-DD HH:mm:ss');
        setCurrentDiscount({
            ...currentDiscount,
            endDate: dateTime
        })
    }
    const onClickDiscountTab = (tab: number, tabPane: number)=>{
        setChosenDiscountTab(tab);
    }

    const onChangeDiscount: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const name = event.target.name;
        const value = event.target.value;
        setCurrentDiscount({
            ...currentDiscount,
            [name]: value
        })
    }

    const onChangeDiscountType = (event: SelectChangeEvent)=>{
        const name = event.target.name;
        const value = event.target.value;
        setCurrentDiscount({
            ...currentDiscount,
            [name]: value
        })
    }

    const onChangeActiveDiscount= (event)=>{
        const value = event.target.value;
        setCurrentDiscount({
            ...currentDiscount,
            active: value
        })
    }

    const date = (dateStr: string)=>{
        return new Date(dateStr);
    }

    const currentDiscountStatus = (startDate: string, endDate: string)=>{
        const currentDate = new Date();
        const startDateTime = date(startDate);
        const endDateTime = date(endDate);
        if(startDateTime>endDateTime) return "Invalid"
        if(endDateTime<currentDate) return "Expired";
        if(currentDate < startDateTime) return "Pre-effective";
        if(startDateTime<=currentDate && currentDate<=endDateTime) return "valid"
        return null;
    }

    const onClickSelectDiscount = (discount: DiscountDetail)=>{
        // console.log(discount)
        setCurrentDiscount(discount)
    }


    // add new Discount
    const onChangeNewDiscount: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const name = event.target.name;
        const value = event.target.value;
        setNewDiscount({
            ...newDiscount,
            [name]: value
        })
    }

    const onChangeNewDiscountType = (event: SelectChangeEvent)=>{
        const name = event.target.name;
        const value = event.target.value;
        setNewDiscount({
            ...newDiscount,
            [name]: value
        })
    }

    const onChangeActiveNewDiscount = (event)=>{
        const value = event.target.value;
        setNewDiscount({
            ...newDiscount,
            active: value
        })
    }
    function handleChangeForNewStartDate(value: any, _context: PickerChangeHandlerContext<DateTimeValidationError>): void {
        const dateTime = value.format('YYYY-MM-DD HH:mm:ss');
        setNewDiscount({
            ...newDiscount,
            startDate: dateTime
        })
    }

    function handleChangeForNewEndDate(value: any, _context: PickerChangeHandlerContext<DateTimeValidationError>): void {
        const dateTime = value.format('YYYY-MM-DD HH:mm:ss');
        setNewDiscount({
            ...newDiscount,
            endDate: dateTime
        })
    }
    const onClickAddNewDiscount = async ()=>{
        toggleAddDiscountModal()
    }

    // update product size
    const toggleUpdateProductSizeModal = ()=>{
        setUpdateProductSizeModal(modal=>({
            ...modal,
            isShowed:!modal.isShowed
        }))
    }
    const onClickUpdateProductSize = (e)=>{
        e.preventDefault();
        toggleUpdateProductSizeModal();
    }
    const handleClickCloseUpdateProductSizeModal = ()=>{
        toggleUpdateProductSizeModal();
    }
    const handleClickConfirmUpdateProductSizeModal = async ()=>{
        if(foodSize){
            const data = {
                productID: foodInfo.productID,
                height: foodSize.height,
                width: foodSize.width,
                length: foodSize.length,
                weight: foodSize.weight
            }
            try {
                const res = await updateProductSize(data);
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?'Product size has been updated successfully': 
                                        'Product size can not be updated',
                        type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                        isShowed: true
                    })  
                }
            } 
            catch (error) {
                setAlert({
                    message: 'Someting is wrong!',
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
        if(foodInfo){
            const data = {
                productID: foodInfo.productID,
                productName: foodInfo.productName,
                quantityPerUnit: foodInfo.quantityPerUnit,
                unitPrice: foodInfo.unitPrice,
                unitsInStock: foodInfo.unitsInStock,
                discontinued: foodInfo.discontinued,
                picture: foodInfo.picture,
                description: description,
                categoryID: foodInfo.categoryID,
            }
            try {
                const res = await updateProduct(data);
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?'Product has been updated successfully': 'Product can not be updated',
                        type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                        isShowed: true
                    })  
                }
            } 
            catch (error) {
                setAlert({
                    message: 'Someting is wrong!',
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
    // update discount
    const toggleUpdateDiscountModal = ()=>{
        setupdateDiscountModal(modal=>({
            ...modal,
            isShowed:!modal.isShowed
        }))
    }

    const handleClickCloseUpdateDiscountModal = ()=>{
        setupdateDiscountModal(modal=>({
            ...modal,
            isShowed:!modal.isShowed
        }))
    }
    const handleClickConfirmUpdateDiscountModal = async ()=>{
        if(currentDiscount){
            const data = {
                discountID: currentDiscount.discountID,
                discountPercent: currentDiscount.discountPercent,
                discountType: currentDiscount.discountType,
                discountCode: currentDiscount.discountCode,
                startDate: currentDiscount.startDate,
                endDate: currentDiscount.endDate,
                active: currentDiscount.active,
                productID: foodInfo.productID
            }
            try {
                const res = await updateDiscount(data);
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?'Discount has been updated successfully': 'Discount can not be updated',
                        type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                        isShowed: true
                    })  
                }
            } 
            catch (error) {
                setAlert({
                    message: 'Discount can not be updated',
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
    // add discount
    const toggleAddDiscountModal = ()=>{
        setAddDiscountModal(modal=>({
            ...modal,
            isShowed:!modal.isShowed
        }))
    }

    const handleClickCloseAddDiscountModal = ()=>{
        setAddDiscountModal(modal=>({
            ...modal,
            isShowed:!modal.isShowed
        }))
    }
    const handleClickConfirmAddDiscountModal = async ()=>{
        const productID = getProductID();
        if(newDiscount && productID){
            const data={
                discountPercent: newDiscount.discountPercent,
                discountType: newDiscount.discountType,
                discountCode: newDiscount.discountCode,
                startDate: newDiscount.startDate,
                endDate: newDiscount.endDate,
                active: newDiscount.active,
                productID: productID,
            }
            try {
                const res = await insertDiscount(data);
                if(res.status){
                    const data = res.data;
                    const status = data.status;
                    setAlert({
                        message: status?'New discount has been saved successfully': 'New discount can not be saved',
                        type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                        isShowed: true
                    })
                    
                    setTimeout(()=>{
                        setAlert({...alert, isShowed: false});
                    }, ALERT_TIMEOUT)
                }
            } catch (error) {
                setAlert({
                    message: 'New discount can not be saved',
                    type: ALERT_TYPE.DANGER,
                    isShowed: true
                }) 
            }finally{
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

    const onChangeFoodSize= (e)=>{
        const name = e.target.name;
        const value = e.target.value;
        setFoodSize({...foodSize, [name]:value})
    }

    return(
        <div id='product-modification'>
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
                                        Edit Product
                                    </h2>
                                    <button style={{marginBottom: ".5rem"}} className='btn btn-secondary float-end' 
                                        onClick={onClickEditable}>
                                        Edit Product
                                    </button>
                                </div>
                            </div>
                            <form action="" method='post' onSubmit={onClickUpdateProduct}>
                                <div className="row tm-edit-product-row">
                                    <div className="col-xl-6 col-lg-6 col-md-12">
                                        <div className="form-group mb-3">
                                            <label htmlFor="inputProductName">Product Name</label>
                                            <input
                                                id="inputProductName"
                                                name="productName"
                                                type="text" disabled={!editable}
                                                value={foodInfo.productName}
                                                onChange={onChange} required
                                                className="form-control validate"
                                            />
                                        </div>
                                        <div className="form-group mb-3">
                                            <label htmlFor="textareaDescription">Description</label>
                                            <textarea className="form-control validate tm-small" 
                                                rows={5} disabled={!editable} name='description'
                                                value={foodInfo.description} onChange={onChange}/>
                                        </div>

                                        <hr />

                                        <div className="form-group mb-3">
                                            <label htmlFor="category">Category</label>
                                            <select className="custom-select form-select tm-select-accounts w-100" 
                                                id="category" name='categoryID' value={foodInfo.categoryID}
                                                onChange={handleCategoryChange} disabled={!editable} required
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
                                                onChange={onChangeDiscontinuedStatus} 
                                                disabled={!editable} required
                                            >
                                                <option value={"true"}>True</option>
                                                <option value={"false"}>False</option>
                                            </select>
                                        </div>
                                        <div className="row">
                                            <div className="form-group mb-3 col-xs-12 col-sm-6">
                                                <label htmlFor="inputQuantityPerUnit">Quantity per Unit</label>
                                                <input id="inputQuantityPerUnit" name="quantityPerUnit"
                                                    type="text" onChange={onChange} disabled={!editable}
                                                    value={foodInfo.quantityPerUnit} required
                                                    className="form-control validate"
                                                />
                                            </div>
                                            <div className="form-group mb-3 col-xs-12 col-sm-6">
                                                <label htmlFor="inputUnitInStock">Units In Stock</label>
                                                <input id="inputUnitInStock" name="unitsInStock" 
                                                    type="number"value={foodInfo.unitsInStock}
                                                    disabled={!editable} onChange={onChangeNumber}
                                                    className="form-control validate" required
                                                />
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="form-group mb-3 col-xs-12 col-sm-6">
                                                <label htmlFor="inputUnitPrice">Unit Price</label>
                                                <input id="inputUnitPrice" name="unitPrice"
                                                    type="number" onChange={onChangeNumber}
                                                    disabled={!editable} value={foodInfo.unitPrice}
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
                                                type="button" disabled={!editable}
                                                className="btn btn-primary btn-block mx-auto w-100"
                                                value="CHANGE IMAGE NOW"
                                                onClick={onClickImage}
                                            />
                                        </div>
                                    </div>
                                    <div className="col-12">
                                        <button type="submit" disabled={!editable} className="btn btn-primary btn-block text-uppercase w-100">
                                            Update Now
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <hr />
                <div className="row">
                    <div className="col-xl-4 col-lg-4 col-md-4 col-sm-12 position-relative mx-auto">
                        <div className="border rounded-2 bg-white p-5 tm-block tm-block-h-auto">
                            <div className="row">
                                <div className="col-12">
                                    <h2 className="d-inline-block" style={{fontWeight: 700}}>
                                        Edit Size
                                    </h2>
                                    <button style={{marginBottom: ".5rem"}} className='btn btn-secondary float-end' 
                                        onClick={onClickEditable}>
                                        Edit Size
                                    </button>
                                </div>
                            </div>
                            <form action="" method='post' onSubmit={onClickUpdateProductSize}>
                                <div className="row tm-edit-product-row">
                                    <div className="form-group mb-3">
                                        <label htmlFor="inputHeight">Height (cm)</label>
                                        <input
                                            id="inputHeight"
                                            name="height"
                                            type="number" disabled={!editable}
                                            value={foodSize.height}
                                            onChange={onChangeFoodSize} required
                                            className="form-control validate"
                                        />
                                    </div>
                                    <div className="form-group mb-3">
                                        <label htmlFor="inputWidth">Width (cm)</label>
                                        <input
                                            id="inputWidth"
                                            name="width"
                                            type="number" disabled={!editable}
                                            value={foodSize.width}
                                            onChange={onChangeFoodSize} required
                                            className="form-control validate"
                                        />
                                    </div>
                                    <div className="form-group mb-3">
                                        <label htmlFor="inputLength">Length (cm)</label>
                                        <input
                                            id="inputLength"
                                            name="length"
                                            type="number" disabled={!editable}
                                            value={foodSize.length}
                                            onChange={onChangeFoodSize} required
                                            className="form-control validate"
                                        />
                                    </div>
                                    <div className="form-group mb-3">
                                        <label htmlFor="inputWeight">Weight (gram)</label>
                                        <input
                                            id="inputWeight"
                                            name="weight"
                                            type="number" disabled={!editable}
                                            value={foodSize.weight}
                                            onChange={onChangeFoodSize} required
                                            className="form-control validate"
                                        />
                                    </div>

                                    <div className="col-12">
                                        <button type="submit" disabled={!editable} className="btn btn-primary btn-block text-uppercase w-100">
                                            Update Now
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div className="col-xl-8 col-lg-8 col-md-8 col-sm-12 mx-auto">
                        <div className="border rounded-2 px-5 py-3 h-100 bg-white">
                            <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
                                <li className="nav-item" role="presentation">
                                    <span className={`nav-link text-dark ${chosenDiscountTab===0? 'active': ""}`} 
                                        onClick={(_event)=>onClickDiscountTab(0, 0)} id="current-discount-tab" role="tab">
                                            Current Discount
                                    </span>
                                </li>
                                <li className="nav-item" role="presentation">
                                    <span className={`nav-link text-dark ${chosenDiscountTab===2? 'active': ""}`} 
                                        onClick={(_event)=>onClickDiscountTab(2, 0)} id="other-discounts-tab" role="tab">
                                            Add New Discount
                                    </span>
                                </li>
                                <li className="nav-item" role="presentation">
                                    <span className={`nav-link text-dark ${chosenDiscountTab===1? 'active': ""}`} 
                                        onClick={(_event)=>onClickDiscountTab(1, 0)} id="other-discounts-tab" role="tab">
                                            Other Discounts
                                    </span>
                                </li>
                            </ul>
                            <div className={`fade show`}>
                                {chosenDiscountTab === 0 ?
                                    <div className={`tab-pane fade ${chosenDiscountTab===0?'show': ""}`} id='current-discount-tab-pane'>
                                        <div className="row my-3 justify-content-end">
                                            <div className="text-align-end">
                                                <button className="btn btn-secondary rounded-2 p-1" onClick={onClickDiscountEditable}>
                                                    Edit{`\u00A0`}Discount
                                                </button>
                                            </div>
                                        </div>

                                        <div className='row'>
                                            <div className="form-group mb-3 col-xl-6 col-lg-6 col-md-12">
                                                <label htmlFor="inputDiscountPercent">Percentage (%)</label>
                                                <input className="custom-select form-control tm-select-accounts w-100" 
                                                    id="inputDiscountPercent" min={0} max={100} name='discountPercent' 
                                                    value={currentDiscount.discountPercent}
                                                    onChange={onChangeDiscount} 
                                                    disabled={!discountEditable} required
                                                />
                                            </div>
                                            <div className="form-group mb-3 col-xl-6 col-lg-6 col-md-12">
                                                <label htmlFor="selectActive">Active</label>
                                                <select className="custom-select form-select w-100" 
                                                    id="selectActive" name='active' 
                                                    value={`${currentDiscount.active}`}
                                                    onChange={onChangeActiveDiscount} 
                                                    disabled={!discountEditable} required
                                                >
                                                    <option value={"true"}>True</option>
                                                    <option value={"false"}>False</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div className="row">
                                            <div className="col-md-12 d-flex justify-content-center">
                                                <FormControl sx={{ m: 1, minWidth: "100%" }} size="small">
                                                    <InputLabel id="discount-type-admin">Type</InputLabel>
                                                    <Select
                                                        labelId="discount-type-admin"
                                                        id="select-discount-type-admin"
                                                        value={`${currentDiscount.discountType}`}
                                                        label="Type"
                                                        name='discountType'
                                                        onChange={onChangeDiscountType}
                                                        disabled={!discountEditable} required
                                                    >
                                                        {discountTypes.map((discountType)=>(
                                                            <MenuItem value={discountType.discountType} key={discountType.discountTypeID}>
                                                                {discountType.discountType}
                                                            </MenuItem>
                                                        ))}
                                                    </Select>
                                                </FormControl>
                                            </div>
                                        </div>
                        
                                        {currentDiscount.discountType===DISCOUNT_TYPE.CODE &&
                                            <div className="form-group col-md-12 mb-3">
                                                <label htmlFor="discount-code-admin">Code</label>
                                                <input type="text" className="form-control text-dark" id="discount-code-admin" 
                                                    placeholder="Code" onChange={onChangeDiscount} name='discountCode' 
                                                    value={currentDiscount.discountCode} disabled={!discountEditable}/>
                                            </div>
                                        }
                                        
                                        <div className="row">
                                            <div className="col-xl-6 col-lg-6 col-md-12">
                                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                    <DemoContainer components={['DateTimePicker']}>
                                                        <DateTimePicker readOnly={!discountEditable}
                                                            label="Start Date"
                                                            slotProps={{
                                                                textField: {
                                                                    required: true,
                                                                }
                                                            }}
                                                            onChange={handleChangeForStartDate}
                                                            value={dayjs(currentDiscount.startDate)}
                                                        />
                                                    </DemoContainer>
                                                </LocalizationProvider>
                                            </div>
                                            <div className="col-xl-6 col-lg-6 col-md-12">
                                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                    <DemoContainer components={['DateTimePicker']}>
                                                        <DateTimePicker readOnly={!discountEditable}
                                                            label="End Date"
                                                            slotProps={{
                                                                textField: {required: true,}
                                                            }}
                                                            onChange={handleChangeForEndDate}
                                                            value={dayjs(currentDiscount.endDate)}
                                                        />
                                                    </DemoContainer>
                                                </LocalizationProvider>
                                            </div>
                                        </div>

                                        <div className="row">
                                            <div className="col-md-12 my-2">
                                                <button className="btn btn-primary" type="submit" onClick={onClickUpdateDiscount}
                                                    disabled={!discountEditable}>
                                                    Update{`\u00A0`}Discount
                                                </button>
                                            </div>
                                            <ModalComponent modal={updateDiscountModal} handleCloseButton={handleClickCloseUpdateDiscountModal}
                                                handleConfirmButton={handleClickConfirmUpdateDiscountModal}/>
                                        </div>
                                    </div>:
                                chosenDiscountTab === 1 ?
                                    <div className={`tab-pane fade ${chosenDiscountTab===1?'show': ""}`} id='other-discounts-tab-pane'>
                                        <div className="px-0 rounded-2 shadow-0">
                                            {discounts.map((discount) =>(
                                                <div className="mb-3" key={discount.discountID}>
                                                    <div className="container mt-5"> 
                                                        <div className="d-flex justify-content-center row"> 
                                                            <div className="col-md-12"> 
                                                                <div className={`coupon p-3 cursor-pointer ${currentDiscount.discountID!==discount.discountID?"bg-body-white":"bg-body-secondary"} border`}
                                                                    onClick={(_e)=>onClickSelectDiscount(discount)}> 
                                                                    <div className="row no-gutters"> 
                                                                        <div className="col-3 border-end"> 
                                                                            <div className="d-flex flex-column align-items-center">
                                                                                {/* <img src="https://i.imgur.com/XwBlVpS.png"/>
                                                                                <span className="d-block">T-labs</span> */}
                                                                                <span className="text-black-50">Foods</span>
                                                                                <span className="text-black-50">
                                                                                    <strong>{discount.active?'Active': 'Deactive'}</strong>
                                                                                </span>
                                                                                <span>
                                                                                    <strong>{currentDiscountStatus(discount.startDate, discount.endDate)}</strong>
                                                                                </span>
                                                                            </div> 
                                                                        </div> 
                                                                        <div className="col-9"> 
                                                                            <div> 
                                                                                <div className="d-flex flex-row off"> 
                                                                                    <div className='col-6 d-flex justify-content-start'>
                                                                                        <p className='text-black-50'>{discount.discountType}</p>
                                                                                    </div>
                                                                                    <div className='col-6 d-flex justify-content-end'>
                                                                                        <h3 className='d-inline-block'>{discount.discountPercent}%</h3><span>OFF</span>
                                                                                    </div>
                                                                                </div> 
                                                                                <div className="d-flex flex-row justify-content-between off px-3 p-2">
                                                                                    <span>Promo code:</span>
                                                                                    <span className="border border-success px-3 rounded code">{discount.discountCode}</span>
                                                                                </div> 
                                                                            </div> 
                                                                        </div> 
                                                                    </div> 
                                                                </div> 
                                                            </div> 
                                                        </div> 
                                                    </div>
                                                </div>
                                            ))}
                                        </div>                        
                                    </div>: 
                                chosenDiscountTab === 2 &&
                                    <div className={`fade ${chosenDiscountTab===2?'show':""}`}>
                                        <div className='row'>
                                            <div className="form-group mb-3 col-xl-6 col-lg-6 col-md-12">
                                                <label htmlFor="inputDiscountPercent">Percentage (%)</label>
                                                <input className="custom-select form-control tm-select-accounts w-100" 
                                                    id="inputDiscountPercent" min={0} max={100} name='discountPercent' 
                                                    value={newDiscount.discountPercent}
                                                    onChange={onChangeNewDiscount} required
                                                />
                                            </div>
                                            <div className="form-group mb-3 col-xl-6 col-lg-6 col-md-12">
                                                <label htmlFor="selectActive">Active</label>
                                                <select className="custom-select form-select w-100" 
                                                    id="selectActive" name='active' 
                                                    value={`${newDiscount.active}`}
                                                    onChange={onChangeActiveNewDiscount} required
                                                >

                                                    <option value={"true"}>True</option>
                                                    <option value={"false"}>False</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div className="row">
                                            <div className="col-md-12 d-flex justify-content-center">
                                                <FormControl sx={{ m: 1, minWidth: "100%" }} size="small">
                                                    <InputLabel id="new-discount-type-admin">Type</InputLabel>
                                                    <Select
                                                        labelId="new-discount-type-admin"
                                                        id="select-new-discount-type-admin"
                                                        value={`${newDiscount.discountType}`}
                                                        label="Type"
                                                        name='discountType'
                                                        onChange={onChangeNewDiscountType}
                                                    >
                                                        {discountTypes.map((discountType)=>(
                                                            <MenuItem value={discountType.discountType} key={discountType.discountTypeID}>
                                                                {discountType.discountType}
                                                            </MenuItem>
                                                        ))}
                                                    </Select>
                                                </FormControl>
                                            </div>
                                        </div>
                        
                                        {newDiscount.discountType===DISCOUNT_TYPE.CODE &&
                                            <div className="form-group col-md-12 mb-3">
                                                <label htmlFor="new-discount-code-admin">Code</label>
                                                <input type="text" className="form-control text-dark" id="new-discount-code-admin" 
                                                    placeholder="Code" onChange={onChangeNewDiscount} name='discountCode' 
                                                    value={newDiscount.discountCode}/>
                                            </div>
                                        }
                                        
                                        <div className="row">
                                            <div className="col-xl-6 col-lg-6 col-md-12">
                                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                    <DemoContainer components={['DateTimePicker']}>
                                                        <DateTimePicker
                                                            label="Start Date"
                                                            slotProps={{
                                                                textField: {
                                                                    required: true,
                                                                }
                                                            }}
                                                            onChange={handleChangeForNewStartDate}
                                                            value={dayjs(newDiscount.startDate)}
                                                        />
                                                    </DemoContainer>
                                                </LocalizationProvider>
                                            </div>
                                            <div className="col-xl-6 col-lg-6 col-md-12">
                                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                    <DemoContainer components={['DateTimePicker']}>
                                                        <DateTimePicker
                                                            label="End Date"
                                                            slotProps={{
                                                                textField: {required: true,}
                                                            }}
                                                            onChange={handleChangeForNewEndDate}
                                                            value={dayjs(newDiscount.endDate)}
                                                        />
                                                    </DemoContainer>
                                                </LocalizationProvider>
                                            </div>
                                        </div>


                                        <div className="row">
                                            <div className="col-md-12 my-2">
                                                {chosenDiscountTab===2 &&
                                                    <button className="btn btn-primary" type="submit" onClick={onClickAddNewDiscount}>
                                                        Add{`\u00A0`}Discount
                                                    </button>
                                                }
                                                <ModalComponent modal={addDiscountModal} handleCloseButton={handleClickCloseAddDiscountModal}
                                                    handleConfirmButton={handleClickConfirmAddDiscountModal}/>
                                            </div>
                                        </div>
                                    </div>
                                }
                            </div>
                        </div>
                    </div>
                </div>
            </>
            }
            <ModalComponent modal={updateProductSizeModal} 
                handleConfirmButton={handleClickConfirmUpdateProductSizeModal} 
                handleCloseButton={handleClickCloseUpdateProductSizeModal}/>
            <ModalComponent modal={updateInfoModal} handleConfirmButton={handleClickConfirmUpdateInfoModal} 
                handleCloseButton={handleClickCloseUpdateInfoModal}/>
        </div>

        // <div className="container-fluid container">
        //     <ScrollToTop/>
        //     <AlertComponent alert={alert}/>
        //     {foodInfo &&
        //         <div>
        //             <section className="py-5">
        //                 <div className="container">
        //                     <div className="row my-3 justify-content-end">
        //                         <div className="col-md-2 col-sm-3">
        //                             <button className="profile-edit-btn btn btn-secondary w-100 rounded-2 p-1" onClick={onClickEditable}>
        //                                 Edit{`\u00A0`}Product
        //                             </button>
        //                         </div>
        //                     </div>
        //                     <div className="row">
        //                         <div className="col-lg-4 py-2">
        //                             <div className="h-100 img-large position-relative w-100 bg-white px-3 py-2 border rounded-2">
        //                                 <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
        //                                     <li className="nav-item" role="presentation">
        //                                         <span className="nav-link text-dark active"
        //                                             id="product-img-tab" role="tab">Product's Img</span>
        //                                     </li>
        //                                 </ul>
        //                                 <ProductImageChangeInput imageSrc={foodInfo.picture} 
        //                                     disable={!editable} onChangePicture={onChangePicture}/>
        //                             </div>
        //                         </div>
        //                         <div className="col-lg-8 py-2">
        //                             <div className=" h-100 bg-white border rounded-2 px-3 py-2">
        //                                 <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
        //                                     <li className="nav-item" role="presentation">
        //                                         <span className="nav-link text-dark active"
        //                                             id="product-info-tab" role="tab">Product's Info</span>
        //                                     </li>
        //                                 </ul>
        //                                 <div className="row">
        //                                     <div className="form-group col-md-4">
        //                                         <label htmlFor="product-name-admin"><strong>Product Name</strong></label>
        //                                         <input type="text" className="form-control text-dark" id="product-name-admin" disabled={!editable} required 
        //                                             placeholder="Product Name" onChange={onChange} value={foodInfo.productName} name='productName'/>
        //                                     </div>
        //                                     <div className="form-check form-switch col-md-7">
        //                                         <label className="form-check-label" htmlFor="product-discontinued-status"><strong>Discontinued</strong></label>
        //                                         <div className='ms-3'>
        //                                             <input className="form-check-input discount-switch-button m-0" type="checkbox" id="product-discontinued-status"
        //                                                 checked={foodInfo.discontinued} onChange={onChangeDiscontinuedStatus} 
        //                                                 name='discontinued' disabled={!editable} required/>
        //                                         </div>
        //                                     </div>
        //                                 </div>

        //                                 <div className="mb-3">
        //                                     <div className="row">
        //                                         <div className="form-group col-md-2">
        //                                             <label htmlFor="unit-price-admin"><strong>Price</strong></label>
        //                                             <input type="number" className="form-control text-dark text-align-center" id="unit-price-admin" required
        //                                                 placeholder="Price" onChange={onChange} value={foodInfo.unitPrice} disabled={!editable}
        //                                                 name='unitPrice'/>
        //                                         </div>
        //                                         <div className="form-group col-md-4">
        //                                             <label htmlFor="quantity-per-unit-admin"><strong>Quantity Per Unit</strong></label>
        //                                             <input type="text" className="form-control text-dark" id="quantity-per-unit-admin" disabled={!editable}
        //                                                 placeholder="Quantity Per Unit" onChange={onChange} value={foodInfo.quantityPerUnit}
        //                                                 name='quantityPerUnit' required/>
        //                                         </div>

        //                                         <div className="form-group col-md-4">
        //                                             <label htmlFor="units-in-stock-admin"><strong>Units in stock</strong> <span className={`${foodInfo.unitsInStock>0?"text-success": 'text-danger'} ms-2`}>
        //                                                 {foodInfo.unitsInStock>0? "(In stock)": "(Out of stock)"}
        //                                             </span></label>
        //                                             <input type="number" className="form-control text-dark text-align-center" id="units-in-stock-admin" 
        //                                                 placeholder="Units in stock" onChange={onChange} value={foodInfo.unitsInStock} 
        //                                                 disabled={!editable} name='unitsInStock' min={0} required/>
        //                                         </div>
        //                                     </div>
        //                                 </div>

        //                                 <hr />

        //                                 <div className="row mb-3">
        //                                     <div className="col-md-4">
        //                                         <FormControl sx={{ m: 1, minWidth: "100%" }} size="small">
        //                                             <InputLabel id="category-product-admin">Category</InputLabel>
        //                                             <Select
        //                                                 labelId="category-product-admin"
        //                                                 id="select-category-product-admin"
        //                                                 value={`${foodInfo.categoryID}`}
        //                                                 label="Category"
        //                                                 name='categoryID'
        //                                                 onChange={handleCategoryChange}
        //                                                 disabled={!editable} required
        //                                             >
        //                                                 {categories.map((category)=>(
        //                                                     <MenuItem value={`${category.categoryID}`} key={category.categoryID}>
        //                                                         {category.categoryName}
        //                                                     </MenuItem>
        //                                                 ))}
        //                                             </Select>
        //                                         </FormControl>
        //                                     </div>
        //                                 </div>
        //                                 <div className="row mb-3 px-3">
        //                                     <div className="col-md-4">
        //                                         <h5>Current Price: {price(foodInfo.unitPrice)}</h5>
        //                                     </div>
        //                                 </div>
        //                             </div>
        //                         </div>
        //                     </div>
        //                 </div>
        //             </section>   

        //             <section className="bg-light border-top py-4">
        //                 <div className="container">
        //                     <div className="row">
        //                         <div className="col-lg-8 mb-4">
        //                             <div className="border rounded-2 px-3 py-2 bg-white">
        //                                 <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
        //                                     <li className="nav-item" role="presentation">
        //                                         <span className="nav-link text-dark active"
        //                                             id="description-tab" role="tab">Description</span>
        //                                     </li>
        //                                 </ul>
        //                                 {/* <!-- Pills navs --> */}

        //                                 {/* <!-- Pills content --> */}
        //                                 <div className="tab-content" id="product-content">
        //                                     {/* {foodInfo.description} */}
        //                                     <div id="editor">
        //                                         <TextEditor content={description} 
        //                                             onChangeContent={onChangeDescription} 
        //                                             editable={editable} height='100%'/>
        //                                     </div>   
 
        //                                 </div>
        //                                 {/* <!-- Pills content --> */}
        //                             </div>
        //                         </div>

        //                         <div className="col-lg-4 mb-4">
        //                             <div className="border rounded-2 px-3 py-2 bg-white">
        //                                 <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
        //                                     <li className="nav-item" role="presentation">
        //                                         <span className={`nav-link text-dark ${chosenDiscountTab===0&& selectedDiscountTabPane===0? 'active': null}`} 
        //                                             onClick={(_event)=>onClickDiscountTab(0, 0)} id="current-discount-tab" role="tab">
        //                                                 Current Discount
        //                                         </span>
        //                                     </li>
        //                                     <li className="nav-item" role="presentation">
        //                                         <span className={`nav-link text-dark ${chosenDiscountTab===1&& selectedDiscountTabPane===0? 'active': null}`} 
        //                                             onClick={(_event)=>onClickDiscountTab(1, 0)} id="other-discounts-tab" role="tab">
        //                                                 Other Discounts
        //                                         </span>
        //                                     </li>
        //                                 </ul>
        //                                {selectedDiscountTabPane===0 &&
        //                                     <div className={`fade ${selectedDiscountTabPane===0?'show':null}`}>
        //                                         {chosenDiscountTab === 0 ?
        //                                             <div className={`tab-pane fade ${chosenDiscountTab===0?'show': null}`} id='current-discount-tab-pane'>
        //                                                 <div className="row my-3 justify-content-end">
        //                                                     <div className="col-md-7 text-align-end">
        //                                                         <button className="profile-edit-btn btn btn-secondary rounded-2 p-1" onClick={onClickDiscountEditable}>
        //                                                             Edit{`\u00A0`}Discount
        //                                                         </button>
        //                                                     </div>
        //                                                 </div>
        
        //                                                 <div className='row'>
        //                                                     <div className="form-group col-md-5">
        //                                                         <label htmlFor="discount-percentage-admin">Percentage (%)</label>
        //                                                         <input type="number" className="form-control text-dark text-align-center" id="discount-percentage-admin" 
        //                                                             placeholder="Percentage (%)" max={100} min={0} onChange={onChangeDiscount} name='discountPercent' 
        //                                                             value={currentDiscount.discountPercent} disabled={!discountEditable} required/>
        //                                                     </div>
        //                                                     <div className="form-check form-switch col-md-7">
        //                                                         <label className="form-check-label" htmlFor="discount-active-status">Active</label>
        //                                                         <div className='ms-3'>
        //                                                             <input className="form-check-input discount-switch-button m-0" type="checkbox" id="discount-active-status"
        //                                                                 checked={currentDiscount.active} onChange={onChangeActiveDiscount} name='active' 
        //                                                                 disabled={!discountEditable} required/>
        //                                                         </div>
        //                                                     </div>
        //                                                 </div>
        
        //                                                 <div className="row">
        //                                                     <div className="col-md-12 d-flex justify-content-center">
        //                                                         <FormControl sx={{ m: 1, minWidth: "100%" }} size="small">
        //                                                             <InputLabel id="discount-type-admin">Type</InputLabel>
        //                                                             <Select
        //                                                                 labelId="discount-type-admin"
        //                                                                 id="select-discount-type-admin"
        //                                                                 value={`${currentDiscount.discountType}`}
        //                                                                 label="Type"
        //                                                                 name='discountType'
        //                                                                 onChange={onChangeDiscountType}
        //                                                                 disabled={!discountEditable} required
        //                                                             >
        //                                                                 {discountTypes.map((discountType)=>(
        //                                                                     <MenuItem value={discountType.discountType} key={discountType.discountTypeID}>
        //                                                                         {discountType.discountType}
        //                                                                     </MenuItem>
        //                                                                 ))}
        //                                                             </Select>
        //                                                         </FormControl>
        //                                                     </div>
        //                                                 </div>
                                        
        //                                                 {currentDiscount.discountType===DISCOUNT_TYPE.CODE &&
        //                                                     <div className="form-group col-md-12 mb-3">
        //                                                         <label htmlFor="discount-code-admin">Code</label>
        //                                                         <input type="text" className="form-control text-dark" id="discount-code-admin" 
        //                                                             placeholder="Code" onChange={onChangeDiscount} name='discountCode' 
        //                                                             value={currentDiscount.discountCode} disabled={!discountEditable}/>
        //                                                     </div>
        //                                                 }
                                                        
        
        //                                                 <LocalizationProvider dateAdapter={AdapterDayjs}>
        //                                                     <DemoContainer components={['DateTimePicker']}>
        //                                                         <DateTimePicker readOnly={!discountEditable}
        //                                                             label="Start Date"
        //                                                             slotProps={{
        //                                                                 textField: {required: true,}
        //                                                             }}
        //                                                             onChange={handleChangeForStartDate}
        //                                                             value={dayjs(currentDiscount.startDate)}
        //                                                         />
        //                                                         <DateTimePicker readOnly={!discountEditable}
        //                                                             label="End Date"
        //                                                             slotProps={{
        //                                                                 textField: {required: true,}
        //                                                             }}
        //                                                             onChange={handleChangeForEndDate}
        //                                                             value={dayjs(currentDiscount.endDate)}
        //                                                         />
        //                                                     </DemoContainer>
        //                                                 </LocalizationProvider>
        
        //                                                 <div className="row">
        //                                                     <div className="col-md-12 my-2">
        //                                                         <button className="btn btn-primary" type="submit" onClick={onClickUpdateDiscount}
        //                                                             disabled={!discountEditable}>
        //                                                             Update{`\u00A0`}Discount
        //                                                         </button>
        //                                                     </div>
        //                                                     <ModalComponent modal={updateDiscountModal} handleCloseButton={handleClickCloseUpdateDiscountModal}
        //                                                         handleConfirmButton={handleClickConfirmUpdateDiscountModal}/>
        //                                                 </div>
        //                                             </div>
        //                                             :
        //                                         chosenDiscountTab === 1 ?
        //                                             <div className={`tab-pane fade ${chosenDiscountTab===1?'show': null}`} id='other-discounts-tab-pane'>
        //                                                 <div className="px-0 rounded-2 shadow-0">
        //                                                     {discounts.map((discount) =>(
        //                                                         <div className="mb-3" key={discount.discountID}>
        //                                                             <div className="container mt-5"> 
        //                                                                 <div className="d-flex justify-content-center row"> 
        //                                                                     <div className="col-md-12"> 
        //                                                                         <div className={`coupon p-3 cursor-pointer ${currentDiscount.discountID!==discount.discountID?"bg-body-white":"bg-body-secondary"} border`}
        //                                                                             onClick={(_e)=>onClickSelectDiscount(discount)}> 
        //                                                                             <div className="row no-gutters"> 
        //                                                                                 <div className="col-3 border-end"> 
        //                                                                                     <div className="d-flex flex-column align-items-center">
        //                                                                                         {/* <img src="https://i.imgur.com/XwBlVpS.png"/>
        //                                                                                         <span className="d-block">T-labs</span> */}
        //                                                                                         <span className="text-black-50">Foods</span>
        //                                                                                         <span className="text-black-50">
        //                                                                                             <strong>{discount.active?'Active': 'Deactive'}</strong>
        //                                                                                         </span>
        //                                                                                         <span>
        //                                                                                             <strong>{currentDiscountStatus(discount.startDate, discount.endDate)}</strong>
        //                                                                                         </span>
        //                                                                                     </div> 
        //                                                                                 </div> 
        //                                                                                 <div className="col-9"> 
        //                                                                                     <div> 
        //                                                                                         <div className="d-flex flex-row off"> 
        //                                                                                             <div className='col-6 d-flex justify-content-start'>
        //                                                                                                 <p className='text-black-50'>{discount.discountType}</p>
        //                                                                                             </div>
        //                                                                                             <div className='col-6 d-flex justify-content-end'>
        //                                                                                                 <h3 className='d-inline-block'>{discount.discountPercent}%</h3><span>OFF</span>
        //                                                                                             </div>
        //                                                                                         </div> 
        //                                                                                         <div className="d-flex flex-row justify-content-between off px-3 p-2">
        //                                                                                             <span>Promo code:</span>
        //                                                                                             <span className="border border-success px-3 rounded code">{discount.discountCode}</span>
        //                                                                                         </div> 
        //                                                                                     </div> 
        //                                                                                 </div> 
        //                                                                             </div> 
        //                                                                         </div> 
        //                                                                     </div> 
        //                                                                 </div> 
        //                                                             </div>
        //                                                         </div>
        //                                                     ))}
        //                                                 </div>                        
        //                                             </div>
        //                                             : null
        //                                         }
        //                                     </div>
        //                                }
        //                             </div>
        //                             <div className='border rounded-2 px-3 py-2 bg-white mt-3'>
        //                                 <div id='add-discounts-tab-pane'>
        //                                     <div className="px-0 rounded-2 shadow-0">
        //                                         <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
        //                                             <li className="nav-item" role="presentation">
        //                                                 <span className={`nav-link text-dark ${chosenDiscountTab===0 && selectedDiscountTabPane===1?'active':null}`} 
        //                                                     onClick={(_event)=>onClickDiscountTab(0, 1)} id="add-new-discount-tab" role="tab">
        //                                                         Add New Discount
        //                                                 </span>
        //                                             </li>
        //                                         </ul>
        //                                         {selectedDiscountTabPane===1 &&
        //                                             <div className={`fade ${chosenDiscountTab===0?'show':null}`}>
        //                                                 <div className='row'>
        //                                                     <div className="form-group col-md-5">
        //                                                         <label htmlFor="new-discount-percentage-admin">Percentage (%)</label>
        //                                                         <input type="number" className="form-control text-dark text-align-center" id="new-discount-percentage-admin" 
        //                                                             placeholder="Percentage (%)" max={100} min={0} onChange={onChangeNewDiscount} name='discountPercent' 
        //                                                             value={newDiscount.discountPercent} required/>
        //                                                     </div>
        //                                                     <div className="form-check form-switch col-md-7">
        //                                                         <label className="form-check-label" htmlFor="new-discount-active-status">Active</label>
        //                                                         <div className='ms-3'>
        //                                                             <input className="form-check-input discount-switch-button m-0" type="checkbox" id="new-discount-active-status"
        //                                                                 checked={newDiscount.active} onChange={onChangeActiveNewDiscount} name='active' required/>
        //                                                         </div>
        //                                                     </div>
        //                                                 </div>

        //                                                 <div className="row">
        //                                                     <div className="col-md-12 d-flex justify-content-center">
        //                                                         <FormControl sx={{ m: 1, minWidth: "100%" }} size="small">
        //                                                             <InputLabel id="new-discount-type-admin">Type</InputLabel>
        //                                                             <Select
        //                                                                 labelId="new-discount-type-admin"
        //                                                                 id="select-new-discount-type-admin"
        //                                                                 value={`${newDiscount.discountType}`}
        //                                                                 label="Type"
        //                                                                 name='discountType'
        //                                                                 onChange={onChangeNewDiscountType}
        //                                                             >
        //                                                                 {discountTypes.map((discountType)=>(
        //                                                                     <MenuItem value={discountType.discountType} key={discountType.discountTypeID}>
        //                                                                         {discountType.discountType}
        //                                                                     </MenuItem>
        //                                                                 ))}
        //                                                             </Select>
        //                                                         </FormControl>
        //                                                     </div>
        //                                                 </div>
                                        
        //                                                 {newDiscount.discountType===DISCOUNT_TYPE.CODE &&
        //                                                     <div className="form-group col-md-12 mb-3">
        //                                                         <label htmlFor="new-discount-code-admin">Code</label>
        //                                                         <input type="text" className="form-control text-dark" id="new-discount-code-admin" 
        //                                                             placeholder="Code" onChange={onChangeNewDiscount} name='discountCode' 
        //                                                             value={newDiscount.discountCode}/>
        //                                                     </div>
        //                                                 }
                                                        

        //                                                 <LocalizationProvider dateAdapter={AdapterDayjs}>
        //                                                     <DemoContainer components={['DateTimePicker']}>
        //                                                         <DateTimePicker
        //                                                             label="Start Date"
        //                                                             slotProps={{
        //                                                                 textField: {required: true,}
        //                                                             }}
        //                                                             onChange={handleChangeForNewStartDate}
        //                                                             value={dayjs(newDiscount.startDate)}
        //                                                         />
        //                                                         <DateTimePicker
        //                                                             label="End Date"
        //                                                             slotProps={{
        //                                                                 textField: {required: true,}
        //                                                             }}
        //                                                             onChange={handleChangeForNewEndDate}
        //                                                             value={dayjs(newDiscount.endDate)}
        //                                                         />
        //                                                     </DemoContainer>
        //                                                 </LocalizationProvider>

        //                                                 <div className="row">
        //                                                     <div className="col-md-12 my-2">
        //                                                         {selectedDiscountTabPane===1 && chosenDiscountTab==0 &&
        //                                                             <button className="btn btn-primary" type="submit" onClick={onClickAddNewDiscount}>
        //                                                                 Add{`\u00A0`}Discount
        //                                                             </button>
        //                                                         }
        //                                                         <ModalComponent modal={addDiscountModal} handleCloseButton={handleClickCloseAddDiscountModal}
        //                                                             handleConfirmButton={handleClickConfirmAddDiscountModal}/>
        //                                                     </div>
        //                                                 </div>
        //                                             </div>
        //                                         }
        //                                     </div>                        
        //                                 </div>
        //                             </div>
        //                         </div>
        //                     </div>
        //                 </div>
        //             </section>

        //             <div className="row">
        //                 <div className="col-md-5 my-2">
        //                     <button className="btn btn-primary" type="submit" onClick={onClickUpdateProduct}
        //                         disabled={!editable}>
        //                         Update{`\u00A0`}Product
        //                     </button>
        //                 </div>
        //             </div>
        //             <ModalComponent modal={updateInfoModal} handleConfirmButton={handleClickConfirmUpdateInfoModal} 
        //                 handleCloseButton={handleClickCloseUpdateInfoModal}/>
        //         </div>
        //     }       
        // </div>
    );
}