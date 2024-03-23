import { getCategories, getProductByID } from '../../../../../api/SearchApi';
import { MouseEventHandler, useEffect, useState } from 'react';
import { Category, Pageable, ProductDetails } from '../../../../../model/Type';
import { getDefaulImage } from '../../../../../service/image';
import { updateProduct } from '../../../../../api/AdminApi';
import AlertComponent from '../../../../shared/functions/alert/Alert';
import { Alert, Modal } from '../../../../../model/WebType';
import { DANGER_ALERT, SUCCESS_ALERT } from '../../../../../constant/config';
import ModalComponent from '../../../../shared/functions/modal/Modal';

export default function FoodComponent(){
    const [foodDetails, setFoodDetails] = useState<ProductDetails>({
        productID: 0,
        productName: '',
        quantityPerUnit: 0,
        unitPrice: 0,
        unitsInStock: 0,
        unitsOnOrder: 0,
        reorderLevel: 0,
        discontinued: true,
        picture: '',
        categoryID: 0,
        categoryName: '',
        supplierID: 0,
        companyName: '',
    });
    const [categoryList, setCategoryList] = useState<Category[]>([]);
    const [pageable, setPageable] = useState<Pageable>({
        first: true,
        last: true,
        number: 0,
        totalPages: 0
    });
    const [readOnly, setReadOnly] = useState(true);
    const [alert, setAlert] = useState<Alert>({
        message: '',
        type: '',
        isShowed: false
    })
    const [modal, setModal] = useState<Modal>({
        message: 'Do you want to continue?',
        title: "Confirm",
        isShowed: false 
    });

    const updateFood : MouseEventHandler<HTMLButtonElement> = async () =>{
        const response = await updateProduct(foodDetails);
        try {
            if(response.status===200){
                setAlert({
                    message: 'Update successfully!',
                    type: SUCCESS_ALERT,
                    isShowed: true
                })
            }
        } catch (error) {
            setAlert({
                message: 'Update failed!',
                type: DANGER_ALERT,
                isShowed: true
            })
        }finally{
            alertTrigger();
        }
    }


    function getFoodID(){
        const urlParams = new URLSearchParams(window.location.search);
        const  foodId = urlParams.get('sp');
        const foodIDStr = foodId?foodId:'0';
        return !isNaN(+foodIDStr)?+foodIDStr:0;
    }
    async function getAndHandleCategories(pageNumber: number){
        const response = await getCategories(pageNumber);
        if(response.status === 200){
            const data = response.data;
            setCategoryList(data.content);
            setPageable({
                first: data.first,
                last: data.last,
                number: data.number,
                totalPages: data.totalPages,
            })
        }
    }

    async function getAndHandleFood(foodID: number){
        const response = await getProductByID(foodID);
        if(response.status===200){
            const data = response.data;
            setFoodDetails({
                productID: data.productID,
                productName: data.productName,
                quantityPerUnit: data.quantityPerUnit,
                unitPrice: data.unitPrice,
                unitsInStock: data.unitsInStock,
                unitsOnOrder: data.unitsOnOrder,
                reorderLevel: data.reorderLevel,
                discontinued: data.discontinued,
                picture: data.picture,
                categoryID: data.categoryID,
                categoryName: data.categoryName,
                supplierID: data.supplierID,
                companyName: data.companyName,
            })
            console.log(data)
        }
    }



    useEffect(()=>{
        initial();
    }, [])

    function alertTrigger(){
        setTimeout(()=>{
            toggleShowAlert()
        }, 3000);
    }
    function toggleShowAlert(){
        setAlert(alert => ({...alert, isShowed: !alert.isShowed }));
    }
    function saveChangeButton(event: any){
        event.preventDefault();
        toggleConfirmModal();
    }
    function toggleConfirmModal(){
        setModal(modal=>({...modal, isShowed: !modal.isShowed}));
    }
    function initial(){
        const foodID = getFoodID();
        getAndHandleFood(foodID);
        getAndHandleCategories(0);
    }

    function foodImage(image: string){
        return image!=null?image: getDefaulImage();
    }
    
    const convertBooleanToStr = (value: boolean)=>{
        if(value) return 'true';
        else return 'false';
    }
    const handleChangeForString: React.ChangeEventHandler<any> = (event) => {
        const name = event.currentTarget.name;
        const value = event.currentTarget.value;
        setFoodDetails({...foodDetails, [name]: value})
    };
    const handleChangeForNumber: React.ChangeEventHandler<any> = (event) => {
        const name = event.currentTarget.name;
        const value = +event.currentTarget.value;
        setFoodDetails({...foodDetails, [name]: value})
    };
    const handleChangeForBoolean: React.ChangeEventHandler<any> = (event) => {
        const name = event.currentTarget.name;
        var value = event.currentTarget.value;
        const initialValue = foodDetails.discontinued;
        if(value.toLowerCase()==='true') value = true;
        else if(value.toLowerCase()==='false') value = false;
        const discontinuedValue = (value===true || value===false)?value: initialValue;
        setFoodDetails({...foodDetails, [name]: discontinuedValue});
    };

    const handleImageChange = (event: any)=>{
        const imageFile = event.target.files[0];
        const reader = new FileReader();
        reader.onloadend = ()=>{
            setFoodDetails({...foodDetails, picture: reader.result as string});
        }
        reader.readAsDataURL(imageFile);
    }

    function editProfile(event: any){
        event.preventDefault();
        setReadOnly(readOnly => !readOnly);
    }
      

    return(
        <div className="container-fluid container">
            <div className="row">
                <form className='mb-4 d-flex'>
                    <div className="profile-img">
                        <img className='w-75 h-auto' alt=""
                            src={foodImage(foodDetails.picture)}/>
                        {!readOnly &&
                            <div className="file btn btn-lg btn-primary">
                                Change Photo
                                <input type="file" name="file" onChange={handleImageChange} accept='image/*'/>
                            </div>
                        }
                    </div>
                    <div className='col-5'>
                        <div className="form-group row">
                            <label htmlFor="productName" className="col-sm-4 col-form-label">
                                {`Product\u00A0Name:`}
                            </label>
                            <div className="col-sm-8">
                                <input type="text" readOnly={readOnly} onChange={handleChangeForString}
                                    className={`${readOnly? 'form-control-plaintext': 'form-control'}`} 
                                    name="productName" value={foodDetails.productName} />
                            </div>
                        </div>
                        <div className="form-group row mt-2">
                            <label htmlFor="quantityPerUnit" className="col-sm-4 col-form-label">
                                {'Quantity\u00A0Per\u00A0Unit'}
                            </label>
                            <div className="col-sm-8">
                            <input type="text" readOnly={readOnly} onChange={handleChangeForString}
                                className={`${readOnly? 'form-control-plaintext': 'form-control'}`} 
                                value={foodDetails.quantityPerUnit} name="quantityPerUnit" 
                                placeholder="Quantity Per Unit" />
                            </div>
                        </div>
                        <div className="form-group row mt-2">
                            <label htmlFor="unitPrice" className="col-sm-4 col-form-label">
                                {'Unit\u00A0Price'}
                            </label>
                            <div className="col-sm-8">
                            <input type="number" value={foodDetails.unitPrice} readOnly={readOnly}
                                className={`${readOnly? 'form-control-plaintext': 'form-control'}`} 
                                name="unitPrice" placeholder="Quantity Per Unit" 
                                onChange={handleChangeForNumber}/>
                            </div>
                        </div>
                        <div className="form-group row mt-2">
                            <label htmlFor="unitsInStock" className="col-sm-4 col-form-label">
                                {'Unit\u00A0in\u00A0Stock'}
                            </label>
                            <div className="col-sm-8">
                            <input type="number" value={foodDetails.unitsInStock} readOnly={readOnly}
                                className={`${readOnly? 'form-control-plaintext': 'form-control'}`}  
                                name="unitsInStock" placeholder="Quantity Per Unit" 
                                onChange={handleChangeForNumber}/>
                            </div>
                        </div>
                        <div className="form-group row mt-2">
                            <label htmlFor="unitsOnOrder" className="col-sm-4 col-form-label">
                                {'Unit\u00A0on\u00A0Order'}
                            </label>
                            <div className="col-sm-8">
                                <input type="number" value={foodDetails.unitsOnOrder} readOnly={readOnly}
                                    className={`${readOnly? 'form-control-plaintext': 'form-control'}`}  
                                    name="unitsOnOrder" placeholder="Quantity Per Unit" 
                                    onChange={handleChangeForNumber}/>
                            </div>
                        </div>
                        <div className="form-group row mt-2">
                            <label htmlFor="reorderLevel" className="col-sm-4 col-form-label">
                                {'Reorder\u00A0Level'}
                            </label>
                            <div className="col-sm-8">
                                <input type="number" value={foodDetails.reorderLevel} readOnly={readOnly}
                                    className={`${readOnly? 'form-control-plaintext': 'form-control'}`}  
                                    name="reorderLevel" placeholder="Reorder Level" 
                                    onChange={handleChangeForNumber}/>
                            </div>
                        </div>
                        <div className="form-group row mt-2">
                            <label htmlFor="discontinued" className="col-sm-4 col-form-label">
                                Discontinued
                            </label>
                            <div className="col-sm-8">
                                <select name="discontinued" onChange={handleChangeForBoolean} disabled={readOnly}
                                    className={`${readOnly? 'form-control-plaintext': 'form-control'}`}  
                                    value={convertBooleanToStr(foodDetails.discontinued)} >
                                    <option value="false" >False</option>
                                    <option value="true">True</option>
                                </select>
                            </div>
                        </div>
                        <hr />
                        <div className="form-group row mt-2">
                            <label className="col-sm-4 col-form-label" htmlFor="category">Category</label>
                            <div className="col-sm-8">
                                <select name="category" onChange={handleChangeForString}
                                    className={`${readOnly? 'form-control-plaintext': 'form-control'}`}  
                                    value={foodDetails.categoryName} disabled={readOnly}>
                                    {categoryList.map((category)=>(
                                        <option key={category.categoryID} 
                                            value={category.categoryName}>
                                            {category.categoryName}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>
                        {!readOnly && 
                            <>
                                <hr />
                                <div className="form-group row">                 
                                    <button type='submit' className='mt-2 w-auto btn btn-primary' 
                                        onClick={saveChangeButton}>
                                        Save Change
                                    </button>
                                </div>
                            </>
                        }
                    </div>
                    <div className="col-md-2 d-flex flex-column justify-content-start align-items-center">
                        <button className="profile-edit-btn btn btn-secondary" name="btnAddMore" onClick={editProfile}>
                            Edit profile
                        </button>
                    </div>
                    {modal.isShowed &&
                        <ModalComponent message={modal.message} title={modal.title}
                            handleCloseButton={toggleConfirmModal} isShowed={modal.isShowed}
                            handleConfirmButton={updateFood}/>

                    }
                    {alert.isShowed &&
                        <AlertComponent message={alert.message} type={alert.type}/>
                    }
                </form>
            </div>
        </div>
    );
}