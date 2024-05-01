import { useParams } from 'react-router-dom';
import { ChangeEventHandler, FormEventHandler, MouseEventHandler, useEffect, useState } from 'react';
import { getCategoryByID } from '../../../../../api/SearchApi';
import { Category } from '../../../../../model/Type';
import { ProductImageChangeInput } from '../../../../shared/functions/product-image-change-input/ProductImageChangeInput';
import { updateCategory } from '../../../../../api/AdminApi';
import AlertComponent from '../../../../shared/functions/alert/Alert';
import { ALERT_TIMEOUT, ALERT_TYPE } from '../../../../../constant/config';
import { Alert } from '../../../../../model/WebType';

export default function AdminCategoryComponent(){
    const { categoryID } = useParams();
    const [category, setCategory] = useState<Category>();
    const [categoryChange, setCategoryChange] = useState<Category>();
    const [editable, setEditable] = useState(false)
    const [alert, setAlert] = useState<Alert>({
        message: '',
        type: ALERT_TYPE.INFO,
        isShowed: false
    });
    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        fetchCategory()
    }

    const fetchCategory = async ()=>{
        const res = await getCategoryByID(categoryID);
        if(res.status===200){
            const data = res.data;
            setCategory(data);
            setCategoryChange(data);
            console.log(data);
        }
    }

    const onChange:ChangeEventHandler<any> = (event)=>{
        if(categoryChange){
            // console.log(event.currentTarget.name)
            const name = event.currentTarget.name;
            const value = event.currentTarget.value;
            setCategoryChange({...categoryChange, [name]:value})
        }
    }

    const onClickEditCategory: MouseEventHandler<HTMLButtonElement> = (event)=>{
        event.preventDefault();
        setEditable(editable => !editable)
    }

    const onClickUpdateCategory:FormEventHandler<any> = async (event)=>{
        event.preventDefault();
        if(categoryChange && category){
            const data = {
                categoryID: categoryChange.categoryID,
                categoryName: categoryChange.categoryName || category.categoryName,
                description: categoryChange.description,
                picture: categoryChange.picture
            }
            const res = await updateCategory(data);
            if(res.status===200){
                const data = res.data;
                const status = data.status;
                // console.log(status);

                setAlert({
                    message: status?'Category has been updated successfully': 'Category can not be updated',
                    type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                    isShowed: true
                })

                setTimeout(()=>{
                    setAlert({...alert, isShowed: false});
                }, ALERT_TIMEOUT)
            }
        }
    }   

    return(
        <div className="container-fluid container mb-5">
            <AlertComponent alert={alert}/>
            {category && categoryChange &&
                <form action="" className='box-shadow-default bg-white py-4 px-3 rounded-4'>
                    <div className="row">
                        <div className="col-md-5">
                            <ProductImageChangeInput imageSrc={category.picture} disable={!editable}
                                onChangePicture={onChange}/>
                        </div>
                        <div className="col-md-7">
                            <div className="row justify-content-end">
                                <div className="col-md-3 col-sm-4">
                                    <button className="profile-edit-btn btn btn-secondary w-100 rounded-2 p-1" 
                                        onClick={onClickEditCategory}>
                                        Edit{`\u00A0`}Category
                                    </button>
                                </div>
                            </div>
                            <div className="row">
                                <div className="form-group col-md-8">
                                    <label htmlFor="category-name-admin">Category Name</label>
                                    <input type="text" className="form-control" id="category-name-admin" name='categoryName'
                                        placeholder="Category" onChange={onChange} value={categoryChange.categoryName} 
                                        required disabled={!editable}/>
                                </div>
                            </div>

                            <div className="form-group col-md-8 my-3">
                                <label htmlFor="category-description-admin">Description</label>
                                <textarea className="form-control" id="category-description-admin" rows={3} 
                                    onChange={onChange} value={categoryChange.description} name='description'
                                    required disabled={!editable}/>
                            </div>
                            <button type="submit" className="btn btn-primary" disabled={!editable} onClick={onClickUpdateCategory}>
                                Update Category</button>
                        </div>
                    </div>
                </form>
            }
        </div>
    );
}