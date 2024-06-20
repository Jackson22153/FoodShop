import { ChangeEventHandler, FormEventHandler, useEffect, useState } from 'react';
import { Category } from '../../../../../model/Type';
import { addCategory } from '../../../../../api/AdminApi';
import AlertComponent from '../../../../shared/functions/alert/Alert';
import { ALERT_TIMEOUT, ALERT_TYPE } from '../../../../../constant/WebConstant';
import { Alert, Modal } from '../../../../../model/WebType';
import ModalComponent from '../../../../shared/functions/modal/Modal';
import { CategoryImageChangeInput } from '../../../../shared/functions/category-image-change/CategoryImageChangeInput';

export default function AdminAddCategoryComponent(){
    const [category, setCategory] = useState<Category>({
        categoryID: 0,
        categoryName: '',
        description: '',
        picture: ''
    });
    const [alert, setAlert] = useState<Alert>({
        message: '',
        type: ALERT_TYPE.INFO,
        isShowed: false
    });
    const [modal, setModal] = useState<Modal>({
        title: "Confirm action",
        message: "Do you want to continue?",
        isShowed: false
    })
    useEffect(()=>{
    }, [])

    const onChange:ChangeEventHandler<any> = (event)=>{
        if(category){
            // console.log(event.currentTarget.name)
            const name = event.currentTarget.name;
            const value = event.currentTarget.value;
            setCategory({...category, [name]:value})
        }
    }

    const onClickAddCategory:FormEventHandler<any> = async (event)=>{
        event.preventDefault();
        toggleModal()
    }   
    const toggleModal = ()=>{
        setModal(modal=>({
            ...modal,
            isShowed:!modal.isShowed
        }))
    }

    const handleClickCloseModal = ()=>{
        setModal(modal=>({
            ...modal,
            isShowed:!modal.isShowed
        }))
    }
    const handleClickConfirmModal = async ()=>{
        if(category){
            try {
                const res = await addCategory(category);
                if(res.status){
                    const data = res.data
                    const status = data.status
                    setAlert({
                        message: status?`New Category ${category.categoryName} has been added successfully`: `New Category ${category.categoryName} can not be added`,
                        type: status?ALERT_TYPE.SUCCESS:ALERT_TYPE.DANGER,
                        isShowed: true
                    })  
                }
            } 
            catch (error) {
                setAlert({
                    message: `New Category ${category.categoryName} can not be added`,
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
        <div className="container-fluid container mb-5">
            <AlertComponent alert={alert}/>
            <form action="" className='box-shadow-default bg-white py-4 px-3 rounded-4'>
                <div className="row">
                    <div className="col-md-5">
                        <CategoryImageChangeInput imageSrc={category.picture} disable={false}
                            onChangePicture={onChange}/>
                    </div>
                    <div className="col-md-7">
                        <div className="row">
                            <div className="form-group col-md-8">
                                <label htmlFor="category-name-admin">Category Name</label>
                                <input type="text" className="form-control" id="category-name-admin" name='categoryName'
                                    placeholder="Category" onChange={onChange} value={category.categoryName} 
                                    required />
                            </div>
                        </div>

                        <div className="form-group col-md-8 my-3">
                            <label htmlFor="category-description-admin">Description</label>
                            <textarea className="form-control" id="category-description-admin" rows={3} 
                                onChange={onChange} value={category.description} name='description'
                                required placeholder='Description' />
                        </div>
                        <button type="submit" className="btn btn-primary"  
                            onClick={onClickAddCategory}>
                            Add New Category
                        </button>
                    </div>
                </div>
            </form>
            <ModalComponent modal={modal} handleCloseButton={handleClickCloseModal}
                handleConfirmButton={handleClickConfirmModal}/>
        </div>
    );
}