import { useState } from "react";
import { SellingProduct } from "../../../../../model/Type";
import { displayProductImage } from "../../../../../service/Image";

type Prop = {
    products: SellingProduct[] 
}
export default function TopSellingProducts(prop: Prop){
    const [currentPage, setCurrentPage] = useState(1);
    const productsPerPage = 2;

    // Calculate the index of the first and last product on the current page
    const indexOfLastProduct = currentPage * productsPerPage;
    const indexOfFirstProduct = indexOfLastProduct - productsPerPage;

    // Slice the products array to get the products for the current page
    const currentProducts = prop.products.slice(indexOfFirstProduct, indexOfLastProduct);

    // Calculate total pages
    const totalPages = Math.ceil(prop.products.length / productsPerPage);

    // Function to handle page change
    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };
    // Function to go to the next page
    const handleNextPage = () => {
        if (currentPage < totalPages) {
            setCurrentPage(currentPage + 1);
        }
    };

    // Function to go to the previous page
    const handlePreviousPage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };
    return(
        <div className="card shadow mb-4">
            <div className="card-header py-3">
                <h6 className="m-0 font-weight-bold text-primary">Top selling Products</h6>
            </div>
            <div className="card-body">
                {currentProducts.length > 0 ? (
                    <div className="top-product">
                    {currentProducts.map((product) => (
                        <div key={product.productID} className="product-card">
                        <img
                            className="product-image rounded-3"
                            src={displayProductImage(product.picture)}
                            alt="Product Image"
                        />
                        <div className="product-details">
                            <h2 className="product-title">{product.productName}</h2>
                            <div className="product-rating">
                            <p>Quantity: {product.quantity}</p>
                            </div>
                            <p className="product-price">${product.unitPrice}</p>
                        </div>
                        </div>
                    ))}
                    </div>
                ) : (
                    <p>No products found.</p>
                )}

                {/* Pagination Controls */}
                <div className="Page navigation float-end">
                    <ul className="pagination">
                        <li className="page-item">
                            <button className="rounded-0 page-link" aria-label="Previous"
                                onClick={()=> handlePreviousPage()}>
                                <span aria-hidden="true">&laquo;</span>
                                <span className="sr-only">Previous</span>
                            </button>
                        </li>
                        {Array.from({ length: totalPages }, (_, index) => (
                            <li className="page-item" key={index}>
                                <button
                                    className={`page-link rounded-0 ${currentPage === index + 1?'active':''}`}
                                    key={index + 1}
                                    onClick={() => handlePageChange(index + 1)}
                                    disabled={currentPage === index + 1}
                                >
                                    {index + 1}
                                </button>

                            </li>
                        ))}
                        <li className="page-item">
                            <button className="rounded-0 page-link" aria-label="Next"
                                onClick={()=> handleNextPage()}>
                                <span aria-hidden="true">&raquo;</span>
                                <span className="sr-only">Next</span>
                            </button>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    )
}