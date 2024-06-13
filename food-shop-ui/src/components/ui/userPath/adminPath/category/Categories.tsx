// import './sb-admin-2.min.css';
import { getCategories } from '../../../../../api/SearchApi';
import { useEffect, useState } from 'react';
import { Category } from '../../../../../model/Type';
import { displayProductImage } from '../../../../../service/Image';
import { adminCategories } from '../../../../../constant/FoodShoppingURL';

export default function AdminCategoriesComponent(){
    const [categories, setCategories] = useState<Category[]>([]);


    useEffect(()=>{
        initial();
    }, [])

    const initial = ()=>{
        fetchCategories(0);
    }

    const fetchCategories = async (pageNumber: number)=>{
        const res = await getCategories(pageNumber);
        if(res.status===200){
            const data = res.data;
            setCategories(data.content);
            console.log(data);
        }
    }


    return(
        <div className="container-fluid container">
           <div className="projcard-container">
                {categories.map((category) =>(
                    <div key={category.categoryID} className="projcard projcard-blue">
                        <a href={`${adminCategories}/${category.categoryID}`}>
                            <div className="projcard-innerbox">
                                <img className="projcard-img" src={displayProductImage(category.picture)} />
                                <div className="projcard-textbox">
                                    <div className="projcard-title">{category.categoryName}</div>
                                    <div className="projcard-subtitle">This explains the card in more detail</div>
                                    <div className="projcard-bar"></div>
                                    <div className="projcard-description">{category.description}</div>
                                </div>
                            </div>
                        </a>
                    </div>
                ))}
            </div>
        </div>
    );
}