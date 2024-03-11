import { useEffect, useState } from "react"
import { Category } from "../../../../model/Type"
import { getCategories } from "../../../../api/SearchApi"
import CategoriesDetailbox from "../../../shared/functions/categoriesDetailBox/CategoriesDetailbox"

export default function CategoriesComponent(){
    const [categories, setCategories] = useState<Category[]>([])

    useEffect(()=>{
        initial();
    }, []);

    function initial(){
        getCategories(0).then(res =>{
            if(res.status===200){
                const data = res.data;
                setCategories(data.content);
                // console.log(res.data);
            }
        })
    }

    return(
        <>
            {/* <!-- service section --> */}
            <section className="service_section layout_padding ">
                <div className="container">
                    <h2 className="custom_heading">Categories</h2>
                    <p className="custom_heading-text">
                        There are many variations of passages of Lorem Ipsum available, but
                        the majority have
                    </p>

                    {categories.map((category)=>(
                        <div className="row layout_padding2" key={category.categoryName}>
                            <CategoriesDetailbox categoryTitle={category.categoryName}
                                categoryDetail={category.description}
                                categoryImageSrc={category.picture}
                            />
                        </div>
                    ))}

                </div>
            </section>

            {/* <!-- end service section --> */}
        </>
    )
}