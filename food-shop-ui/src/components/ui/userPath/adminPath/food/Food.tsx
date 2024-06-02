import { ChangeEventHandler, FormEventHandler, MouseEventHandler, useEffect, useState } from 'react';
import { Category, DiscountDetail, DiscountType, ProductDetails } from '../../../../../model/Type';
import { getDiscountDetail, getDiscountTypes, getDiscountsByProduct, getProductDetail, insertDiscount, updateDiscount, updateProduct } from '../../../../../api/AdminApi';
import { useLocation } from 'react-router-dom';
import { getCategories } from '../../../../../api/SearchApi';
import { FormControl, InputLabel, Select, MenuItem, SelectChangeEvent } from '@mui/material';
import TextEditor from '../../../../shared/functions/editor/Editor';
import { ProductImageChangeInput } from '../../../../shared/functions/product-image-change-input/ProductImageChangeInput';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { PickerChangeHandlerContext, DateTimeValidationError } from '@mui/x-date-pickers/models';
import dayjs from 'dayjs';
import { ceilRound } from '../../../../../service/convert';
import { ALERT_TIMEOUT, ALERT_TYPE, DISCOUNT_TYPE } from '../../../../../constant/config';
import { Alert, Modal } from '../../../../../model/WebType';
import AlertComponent from '../../../../shared/functions/alert/Alert';
import ModalComponent from '../../../../shared/functions/modal/Modal';

export default function AdminFoodComponent(){
    const location = useLocation();
    const [foodInfo, setFoodInfo] = useState<ProductDetails>();
    const [discountEditable, setDiscountEditable] = useState(false);
    const [foodInfoChange, setFoodInfoChange] = useState<ProductDetails>();
    const [selectedDiscountTabPane, setSelectedDiscountTabPane] = useState(0);
    const [description, setDescription] = useState("")
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
    const [updateDiscountModal, setupdateDiscountModal] = useState<Modal>({
        title: "Confirm action",
        message: "Do you want to continue?",
        isShowed: false
    })
    const [addDiscountModal, setAddDiscountModal] = useState<Modal>({
        title: "Confirm action",
        message: "Do you want to continue?",
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
            setFoodInfo(data);
            setFoodInfoChange(data);
            fetchDiscount(data.discountID);
            setDescription(data.description);
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
        if(foodInfoChange){
            setFoodInfoChange({
                ...foodInfoChange,
                [event.target.name]: event.target.value
            })
        }
    }

    const onChangeDiscontinuedStatus: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const value = event.target.checked;
        if(foodInfoChange){
            setFoodInfoChange({
                ...foodInfoChange,
                discontinued: value
            })
        }
    }

    const onChangeDescription = (content: string)=>{
        if(foodInfoChange){
            setDescription(content)
        }
    }
    // change product's picture
    const onChangePicture = (imageSrc: string)=>{
        setFoodInfoChange({...foodInfoChange, ['picture']:imageSrc})

    }

    const handleCategoryChange = (event: SelectChangeEvent) => {
        if(foodInfoChange){
            const name = event.target.name;
            const value = event.target.value;
            setFoodInfoChange({
                ...foodInfoChange, 
                [name]:+value
            });
        }
    };
    const onClickUpdateDiscount:FormEventHandler<any>= async (event)=>{
        event.preventDefault()
        toggleUpdateDiscountModal()
    }

    const onClickUpdateProduct:FormEventHandler<HTMLButtonElement> = async (event)=>{
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
        onClickDiscountTabPane(tabPane);
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

    const onChangeActiveDiscount: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const value = event.target.checked;
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

    const onClickDiscountTabPane = (discountTabPane: number)=>{
        setSelectedDiscountTabPane(discountTabPane);
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

    const onChangeActiveNewDiscount: ChangeEventHandler<HTMLInputElement> = (event)=>{
        const value = event.target.checked;
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
        if(foodInfoChange && foodInfo){
            const data = {
                productID: foodInfo.productID,
                productName: foodInfoChange.productName || foodInfo.productName,
                quantityPerUnit: foodInfoChange.quantityPerUnit || foodInfo.quantityPerUnit,
                unitPrice: foodInfoChange.unitPrice || foodInfo.unitPrice,
                unitsInStock: foodInfoChange.unitsInStock || foodInfo.unitsInStock,
                unitsOnOrder: foodInfoChange.unitsOnOrder || foodInfo.unitsOnOrder,
                reorderLevel: foodInfoChange.reorderLevel || foodInfo.reorderLevel,
                discontinued: foodInfoChange.discontinued,
                picture: foodInfoChange.picture,
                description: description,
                categoryID: foodInfoChange.categoryID || foodInfo.categoryID,
                supplierID: foodInfoChange.supplierID || foodInfo.supplierID
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
                    message: 'Product has been updated successfully',
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
                active: currentDiscount.active
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

    return(
        <div className="container-fluid container">
            <AlertComponent alert={alert}/>
            {foodInfoChange &&
                <div>
                    <section className="py-5">
                        <div className="container">
                            <div className="row my-3 justify-content-end">
                                <div className="col-md-2 col-sm-3">
                                    <button className="profile-edit-btn btn btn-secondary w-100 rounded-2 p-1" onClick={onClickEditable}>
                                        Edit{`\u00A0`}Product
                                    </button>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-lg-4 py-2">
                                    <div className="h-100 img-large position-relative w-100 bg-white px-3 py-2 border rounded-2">
                                        <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
                                            <li className="nav-item" role="presentation">
                                                <span className="nav-link text-dark active"
                                                    id="product-img-tab" role="tab">Product's Img</span>
                                            </li>
                                        </ul>
                                        <ProductImageChangeInput imageSrc={foodInfoChange.picture} 
                                            disable={!editable} onChangePicture={onChangePicture}/>
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
                                                <input type="text" className="form-control text-dark" id="product-name-admin" disabled={!editable} required 
                                                    placeholder="Product Name" onChange={onChange} value={foodInfoChange.productName} name='productName'/>
                                            </div>
                                            <div className="form-check form-switch col-md-7">
                                                <label className="form-check-label" htmlFor="product-discontinued-status"><strong>Discontinued</strong></label>
                                                <div className='ms-3'>
                                                    <input className="form-check-input discount-switch-button m-0" type="checkbox" id="product-discontinued-status"
                                                        checked={foodInfoChange.discontinued} onChange={onChangeDiscontinuedStatus} 
                                                        name='discontinued' disabled={!editable} required/>
                                                </div>
                                            </div>
                                        </div>

                                        <div className="mb-3">
                                            <div className="row">
                                                <div className="form-group col-md-2">
                                                    <label htmlFor="unit-price-admin"><strong>Price</strong></label>
                                                    <input type="number" className="form-control text-dark text-align-center" id="unit-price-admin" required
                                                        placeholder="Price" onChange={onChange} value={foodInfoChange.unitPrice} disabled={!editable}
                                                        name='unitPrice'/>
                                                </div>
                                                <div className="form-group col-md-4">
                                                    <label htmlFor="quantity-per-unit-admin"><strong>Quantity Per Unit</strong></label>
                                                    <input type="text" className="form-control text-dark" id="quantity-per-unit-admin" disabled={!editable}
                                                        placeholder="Quantity Per Unit" onChange={onChange} value={foodInfoChange.quantityPerUnit}
                                                        name='quantityPerUnit' required/>
                                                </div>

                                                <div className="form-group col-md-4">
                                                    <label htmlFor="units-in-stock-admin"><strong>Units in stock</strong> <span className={`${foodInfoChange.unitsInStock>0?"text-success": 'text-danger'} ms-2`}>
                                                        {foodInfoChange.unitsInStock>0? "(In stock)": "(Out of stock)"}
                                                    </span></label>
                                                    <input type="number" className="form-control text-dark text-align-center" id="units-in-stock-admin" 
                                                        placeholder="Units in stock" onChange={onChange} value={foodInfoChange.unitsInStock} 
                                                        disabled={!editable} name='unitsInStock' min={0} required/>
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
                                                        value={`${foodInfoChange.categoryID}`}
                                                        label="Category"
                                                        name='categoryID'
                                                        onChange={handleCategoryChange}
                                                        disabled={!editable} required
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
                                        <div className="row mb-3 px-3">
                                            <div className="col-md-4">
                                                <h5>Current Price: {price(foodInfoChange.unitPrice)}</h5>
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
                                <div className="col-lg-8 mb-4">
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
                                            {/* {foodInfoChange.description} */}
                                            <div id="editor">
                                                <TextEditor content={description} 
                                                    onChangeContent={onChangeDescription} 
                                                    editable={editable} height='100%'/>
                                            </div>   
 
                                        </div>
                                        {/* <!-- Pills content --> */}
                                    </div>
                                </div>

                                <div className="col-lg-4 mb-4">
                                    <div className="border rounded-2 px-3 py-2 bg-white">
                                        <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
                                            <li className="nav-item" role="presentation">
                                                <span className={`nav-link text-dark ${chosenDiscountTab===0&& selectedDiscountTabPane===0? 'active': null}`} 
                                                    onClick={(_event)=>onClickDiscountTab(0, 0)} id="current-discount-tab" role="tab">
                                                        Current Discount
                                                </span>
                                            </li>
                                            <li className="nav-item" role="presentation">
                                                <span className={`nav-link text-dark ${chosenDiscountTab===1&& selectedDiscountTabPane===0? 'active': null}`} 
                                                    onClick={(_event)=>onClickDiscountTab(1, 0)} id="other-discounts-tab" role="tab">
                                                        Other Discounts
                                                </span>
                                            </li>
                                        </ul>
                                       {selectedDiscountTabPane===0 &&
                                            <div className={`fade ${selectedDiscountTabPane===0?'show':null}`}>
                                                {chosenDiscountTab === 0 ?
                                                    <div className={`tab-pane fade ${chosenDiscountTab===0?'show': null}`} id='current-discount-tab-pane'>
                                                        <div className="row my-3 justify-content-end">
                                                            <div className="col-md-7 text-align-end">
                                                                <button className="profile-edit-btn btn btn-secondary rounded-2 p-1" onClick={onClickDiscountEditable}>
                                                                    Edit{`\u00A0`}Discount
                                                                </button>
                                                            </div>
                                                        </div>
        
                                                        <div className='row'>
                                                            <div className="form-group col-md-5">
                                                                <label htmlFor="discount-percentage-admin">Percentage (%)</label>
                                                                <input type="number" className="form-control text-dark text-align-center" id="discount-percentage-admin" 
                                                                    placeholder="Percentage (%)" max={100} min={0} onChange={onChangeDiscount} name='discountPercent' 
                                                                    value={currentDiscount.discountPercent} disabled={!discountEditable} required/>
                                                            </div>
                                                            <div className="form-check form-switch col-md-7">
                                                                <label className="form-check-label" htmlFor="discount-active-status">Active</label>
                                                                <div className='ms-3'>
                                                                    <input className="form-check-input discount-switch-button m-0" type="checkbox" id="discount-active-status"
                                                                        checked={currentDiscount.active} onChange={onChangeActiveDiscount} name='active' 
                                                                        disabled={!discountEditable} required/>
                                                                </div>
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
                                                        
        
                                                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                            <DemoContainer components={['DateTimePicker']}>
                                                                <DateTimePicker readOnly={!discountEditable}
                                                                    label="Start Date"
                                                                    slotProps={{
                                                                        textField: {required: true,}
                                                                    }}
                                                                    onChange={handleChangeForStartDate}
                                                                    value={dayjs(currentDiscount.startDate)}
                                                                />
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
                                                    </div>
                                                    :
                                                chosenDiscountTab === 1 ?
                                                    <div className={`tab-pane fade ${chosenDiscountTab===1?'show': null}`} id='other-discounts-tab-pane'>
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
                                                    </div>
                                                    : null
                                                }
                                            </div>
                                       }
                                    </div>
                                    <div className='border rounded-2 px-3 py-2 bg-white mt-3'>
                                        <div id='add-discounts-tab-pane'>
                                            <div className="px-0 rounded-2 shadow-0">
                                                <ul className="nav nav-tabs p-0 mb-3 cursor-pointer" role="tablist">
                                                    <li className="nav-item" role="presentation">
                                                        <span className={`nav-link text-dark ${chosenDiscountTab===0 && selectedDiscountTabPane===1?'active':null}`} 
                                                            onClick={(_event)=>onClickDiscountTab(0, 1)} id="add-new-discount-tab" role="tab">
                                                                Add New Discount
                                                        </span>
                                                    </li>
                                                </ul>
                                                {selectedDiscountTabPane===1 &&
                                                    <div className={`fade ${chosenDiscountTab===0?'show':null}`}>
                                                        <div className='row'>
                                                            <div className="form-group col-md-5">
                                                                <label htmlFor="new-discount-percentage-admin">Percentage (%)</label>
                                                                <input type="number" className="form-control text-dark text-align-center" id="new-discount-percentage-admin" 
                                                                    placeholder="Percentage (%)" max={100} min={0} onChange={onChangeNewDiscount} name='discountPercent' 
                                                                    value={newDiscount.discountPercent} required/>
                                                            </div>
                                                            <div className="form-check form-switch col-md-7">
                                                                <label className="form-check-label" htmlFor="new-discount-active-status">Active</label>
                                                                <div className='ms-3'>
                                                                    <input className="form-check-input discount-switch-button m-0" type="checkbox" id="new-discount-active-status"
                                                                        checked={newDiscount.active} onChange={onChangeActiveNewDiscount} name='active' required/>
                                                                </div>
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
                                                        

                                                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                            <DemoContainer components={['DateTimePicker']}>
                                                                <DateTimePicker
                                                                    label="Start Date"
                                                                    slotProps={{
                                                                        textField: {required: true,}
                                                                    }}
                                                                    onChange={handleChangeForNewStartDate}
                                                                    value={dayjs(newDiscount.startDate)}
                                                                />
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

                                                        <div className="row">
                                                            <div className="col-md-12 my-2">
                                                                {selectedDiscountTabPane===1 && chosenDiscountTab==0 &&
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
                            </div>
                        </div>
                    </section>

                    <div className="row">
                        <div className="col-md-5 my-2">
                            <button className="btn btn-primary" type="submit" onClick={onClickUpdateProduct}
                                disabled={!editable}>
                                Update{`\u00A0`}Product
                            </button>
                        </div>
                    </div>
                    <ModalComponent modal={updateInfoModal} handleConfirmButton={handleClickConfirmUpdateInfoModal} 
                        handleCloseButton={handleClickCloseUpdateInfoModal}/>
                </div>
            }       
        </div>
    );
}