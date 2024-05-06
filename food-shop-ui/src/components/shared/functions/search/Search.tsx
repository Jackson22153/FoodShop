import { ChangeEventHandler, FormEventHandler, MouseEventHandler, useState } from "react";
import { onUserInput } from "../../../../service/search";
import { Product } from "../../../../model/Type";
import { FoodPath, SearchFoodsPath } from "../../../../constant/FoodShoppingURL";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import { searchProducts } from "../../../../api/SearchApi";

interface Props{
    searchInputValue: string,
    searchResult: Product[],
    handleSearchResult: any,
    handleInputSearchChange: any
}
export default function Search(prop: Props){
    // const searchDropdownRef = useRef(null);
    const [isShowed, setIsShowed] = useState(false);
    const searchInputValue = prop.searchInputValue;
    const searchResult = prop.searchResult;

    // setter for search result
    function handleSearchResult(searcResult: Product[]){
        prop.handleSearchResult(searcResult);
    }
    // update value for searchbar
    const handleInputSearchChange : ChangeEventHandler<HTMLInputElement> = event =>{
        prop.handleInputSearchChange(event);
    }
    // add value to search bar
    const handleInputChange: ChangeEventHandler<HTMLInputElement> = (event) =>{
        const searchValue = event.target.value;
        if(searchValue === ''){
            handleSearchResult([]);
        }
       handleInputSearchChange(event);
    }
    // searching products
    const onKeyUpSearch: FormEventHandler<HTMLInputElement> = ()=>{
        onUserInput(searchInputValue, fetchProducts);
    }
    // fetch products
    const fetchProducts = async (userInput: string)=>{
        const res = await searchProducts(userInput, 0)
        if(res.status){
            const data = res.data;
            handleSearchResult(data.content);
            // return data;
        }
    }
    // view searched products
    const onClickSearch: MouseEventHandler<HTMLButtonElement> = (event) =>{
        event.preventDefault();
        if(searchInputValue.length >2){
            const url = SearchFoodsPath(searchInputValue);
            window.location.href= url
        }
    }

    const onFocus = ()=>{
        setIsShowed(true);
    }

    return(
        <div className="input-group rounded search-container dropdown">
            <input type="search" className="form-control rounded searchbar dropdown-toggle" placeholder="Search" 
                aria-label="Search" aria-describedby="search-addon" value={searchInputValue}
                onChange={handleInputChange} onKeyUp={onKeyUpSearch} onFocus={onFocus}/>
            <ul className={`dropdown-menu search-dropdown ${isShowed?'show': ''} ${searchResult.length===0?'p-0': ''}`}>
                {searchResult.map((product) =>(
                    <li className="dropdown-item" key={product.productID}>
                        <a href={FoodPath(product.productName, product.productID)} 
                            className="dropdown-link">{product.productName}</a>
                    </li>
                ))}
            </ul>
            <button className="input-group-text search-button btn btn-primary" 
                id="search-addon" onClick={onClickSearch}>
                <FontAwesomeIcon icon={faMagnifyingGlass}/>
            </button>
        </div>
    );
}