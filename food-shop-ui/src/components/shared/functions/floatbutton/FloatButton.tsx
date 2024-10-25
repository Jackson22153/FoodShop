import { useRef, useState } from "react"

interface Prop{
    toggleProductSizesModal: any
}
export default function FloatButton(prop: Prop){
    const [isShowed, setIsShowed] = useState(false)
    const inputRef = useRef<HTMLInputElement>()

    const toggleIsShowed = ()=>{
        setIsShowed(show => !show)
    }

    const onChangeBtn = (e)=>{
        e.preventDefault();
        toggleIsShowed()
    }

    const onClick = (e)=>{
        e.preventDefault();
        if(inputRef.current){
            inputRef.current.click();
        }
    }

    const onClickToggleProductSizesModal = (e)=>{
        e.preventDefault();
        prop.toggleProductSizesModal()
    }

    return(
        <div className="float-btn-container">
            <input type="checkbox" id="toggle" checked={isShowed} onChange={onChangeBtn} ref={inputRef}/>
            <label className="button" htmlFor="toggle" onClick={onClick}></label>
            <nav className={`nav`}>
                <ul className="w-100 px-2">
                    <button onClick={onClickToggleProductSizesModal}>Update Product Size</button>
                    {/* <a href="https://codepen.io/sashatran/pens/public/" target="_blank">Home</a>
                    <a href="https://www.linkedin.com/in/sasha-tran-23498989/" target="_blank">Hire Me</a>
                    <a href="https://www.youtube.com/channel/UCCATAa8MWoBuH-sR_Jlx29A" target="_blank">Subscribe</a> */}
                </ul>
            </nav>
        </div>
    )
}